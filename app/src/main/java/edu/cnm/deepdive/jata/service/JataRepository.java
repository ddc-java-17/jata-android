package edu.cnm.deepdive.jata.service;

import androidx.lifecycle.ViewModel;
import edu.cnm.deepdive.jata.model.Board;
import edu.cnm.deepdive.jata.model.Game;
import edu.cnm.deepdive.jata.model.Ship;
import edu.cnm.deepdive.jata.model.Shot;
import edu.cnm.deepdive.jata.viewmodel.GameViewModel;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.Subject;
import java.util.List;
import javax.inject.Inject;
import retrofit2.http.PUT;

/**
 * Links the {@link ViewModel} and the {@link JataServiceProxy} that talks to the server. Its
 * methods are invoked by the view model, then they invoke code that sends the relevant Json
 * packet to the server.
 */
public class JataRepository {

  private static final String TAG = JataRepository.class.getSimpleName();

  private final JataServiceProxy proxy;
  private final JataLongPollServiceProxy longPollProxy;
  private final UserRepository userRepository;
  private final GoogleSignInService signInService;
  private final Scheduler scheduler;
  private Subject<Game> gamePoller;
  private Subject<Throwable> throwablePoller;
  private CompositeDisposable pending;
  private Game game;
  private Board board;
  private Shot shot;

  @Inject
  JataRepository(JataServiceProxy proxy, JataLongPollServiceProxy longPollProxy,
      UserRepository userRepository,
      GoogleSignInService signInService) {
    this.proxy = proxy;
    this.longPollProxy = longPollProxy;
    this.userRepository = userRepository;
    this.signInService = signInService;
    scheduler = Schedulers.single();
    pending = new CompositeDisposable();
  }

  /**
   * Authenticates a user, invokes the {@link JataServiceProxy#startGame(Game, String)} to send an
   * HTTP request to the server, and then begins listening for the long poll responses from the
   * server.
   *
   * @param game A {@link Game} object to be sent to the server.
   */
  public void startGame(Game game) {
    signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> proxy.startGame(game, token))
        .doOnSuccess(this::setGame)
        .subscribe(
            this::updateGame,
            this::updateThrowable,
            pending
        );
//    int[] origin = {1, 1};
//    List<Ship> ships = Stream.generate(() -> {
//          int x = origin[0];
//          int y = origin[1];
//          boolean vertical = (x < y);
//          int length = 3;
//          if (vertical) {
//            origin[0]++;
//          } else {
//            origin[1]++;
//          }
//          return new Ship(x, y, length, vertical);
//        })
//        .limit(4)
//        .collect(Collectors.toList());
//    User user = new User();
//    user.setDisplayName("ducky");
//    user.setKey(""); // FIXME: 4/6/2024 use the current users key
//    Board board = new Board(user, List.of(), ships, false, false);
//    this.game = new Game(null, game.getBoardSize(), game.getPlayerCount(), List.of(board), false,
//        false, false);
//    updateGame(this.game);
  }

  /**
   * Authenticates a user, invokes the {@link JataServiceProxy#submitShips(String, List, String)}
   * method to send an HTTP request to the server, and then listens for the long poll
   * responses from the server.
   *
   * @param ships {@link List} of {@link Ship} objects that will be sent to the server.
   */
  public void submitShips(List<Ship> ships) {
    signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> proxy.submitShips(game.getKey(), ships, token))
        .subscribe(
            this::updateGame,
            this::updateThrowable,
            pending
        );
  }

  /**
   * Authenticates a user, invokes {@link JataServiceProxy#submitShots(String, List, String)} to
   * send an HTTP request to the server, then listens for the long poll responses from the server.
   *
   * @param shots
   */
  public void submitShots(List<Shot> shots) {
    signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> proxy.submitShots(game.getKey(), shots, token))
        .subscribe(
            this::updateGame,
            this::updateThrowable,
            pending
        );

  }

  /**
   * Processes the long poll responses from the server holding a {@link Game} object.
   *
   * @return Reactive stream of the {@link Game} object that can be subscribed to.
   */
  public Observable<Game> pollGameStatus() {
    if (gamePoller != null) {
      gamePoller.onComplete();
    }
    gamePoller = BehaviorSubject.create();
    return gamePoller
        .subscribeOn(scheduler)
        .flatMapSingle((game) -> userRepository.getCurrent()
                .doOnSuccess((user) -> game.getBoards().forEach((board) ->
//                    board.setMine(true))) // FIXME: 4/6/2024
//            board.setMine(user.getKey().equals(board.getPlayer().getKey()))
                        board.isMine()
                ))
                .map((user) -> game)
        )
        .doOnNext((game) -> {
          if (game.isStarted() && !game.isFinished() && !game.isYourTurn()) {
            getGame(game.getKey());
          }
        });
  }

  public Observable<Throwable> pollThrowable() {
    if (throwablePoller != null) {
      throwablePoller.onComplete();
    }
    throwablePoller = BehaviorSubject.create();
    return throwablePoller
        .subscribeOn(scheduler);
  }

  public void stopPolling() {
    gamePoller.onComplete();
    throwablePoller.onComplete();
  }

  public void changePlacement(List<Ship> ships, int boardIndex) {
    if (isPlacementValid(ships)) {
      List<Ship> boardShips = game.getBoards().get(boardIndex).getShips();
      boardShips.clear();
      boardShips.addAll(ships);
    } else {
      throwablePoller.onNext(new InvalidShipPlacementException());
    }
    gamePoller.onNext(game);
  }

  public void changePlacement(List<Ship> ships, int boardIndex, Ship ship) {
    if (isPlacementValid(ships)) {
      List<Ship> boardShips = game.getBoards().get(boardIndex).getShips();
      boardShips.clear();
      boardShips.addAll(ships);
    } else {
      ship.setVertical(!ship.isVertical());
      changePlacement(ships, boardIndex);
    }
    gamePoller.onNext(game);
  }

  /**
   * Validates the placement of ships when a
   *
   * @param ships
   * @return
   */
  public boolean isPlacementValid(List<Ship> ships) {
    int size = game.getBoardSize();
    boolean[][] placement = new boolean[size][size];
    boolean valid = true;
    outer:
    for (Ship ship : ships) {
      int x = ship.getX();
      int y = ship.getY();
      int length = ship.getLength();
      boolean vertical = ship.isVertical();
      if (x < 1 || y < 1
          || (vertical && y + length > size + 1)
          || (!vertical && x + length > size + 1)) {
        valid = false;
        break outer;
      }
      int stepX = vertical ? 0 : 1;
      int stepY = vertical ? 1 : 0;
      for (
          int checkY = y - 1, checkX = x - 1, step = 0;
          step < length;
          checkY += stepY, checkX += stepX, step++
      ) {
        if (placement[checkY][checkX]) {
          valid = false;
          break outer;
        }
        placement[checkY][checkX] = true;
      }
    }
    return valid;
  }

  private void getGame(String key) {
    signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> longPollProxy.getGame(key, token))
        .doOnSuccess(this::setGame)
        .subscribe(
            this::updateGame,
            this::updateThrowable,
            pending
        );
  }

  private void updateGame(Game game) {
    if (gamePoller != null) {
      gamePoller.onNext(game);
    }
  }

  private void updateThrowable(Throwable throwable) {
    if (throwablePoller != null) {
      throwablePoller.onNext(throwable);
    }
  }

  private void setGame(Game game) {
    this.game = game;
  }

  public static class InvalidShipPlacementException extends IllegalArgumentException {

    public InvalidShipPlacementException() {
      super();
    }

    public InvalidShipPlacementException(String message) {
      super(message);
    }

    public InvalidShipPlacementException(String message, Throwable cause) {
      super(message, cause);
    }

    public InvalidShipPlacementException(Throwable cause) {
      super(cause);
    }
  }

}
