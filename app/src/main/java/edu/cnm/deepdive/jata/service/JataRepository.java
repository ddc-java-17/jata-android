package edu.cnm.deepdive.jata.service;

import edu.cnm.deepdive.jata.model.Board;
import edu.cnm.deepdive.jata.model.Game;
import edu.cnm.deepdive.jata.model.Ship;
import edu.cnm.deepdive.jata.model.entity.User;
import edu.cnm.deepdive.jata.model.Ship;
import edu.cnm.deepdive.jata.model.Shot;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.List;
import javax.inject.Inject;

/**
 * This class is the link between the view model and the {@link JataServiceProxy} that talks to the
 * server. Its methods are meant to be invoked by the view model, then invoke code that sends the
 * relevant Json packet to the server.
 */
public class JataRepository {

  private static final String TAG = JataRepository.class.getSimpleName();

  private final JataServiceProxy proxy;
  private final UserRepository userRepository;
  private final GoogleSignInService signInService;
  private final Scheduler scheduler;
  private Game game;
  private Board board;
  private Shot shot;

  /**
   * This constructor initializes
   *
   * @param proxy          This is {@link JataServiceProxy} and its job is to talk to the server
   *                       when it is asked to.
   * @param userRepository this...
   * @param signInService  This is {@link GoogleSignInService} and it allows our users to log into
   *                       our app and service.
   */
  @Inject
  JataRepository(JataServiceProxy proxy, UserRepository userRepository,
      GoogleSignInService signInService) {
    this.proxy = proxy;
    this.userRepository = userRepository;
    this.signInService = signInService;
    scheduler = Schedulers.single();
  }

  /**
   * This method is part of the bridge between the UI and the {@link JataServiceProxy}. When the
   * user taps the Start Game button, it will, through a chain of events, invoke this method, then
   * it will invoke the method {@link JataServiceProxy#startGame(Game, String)} to send the request
   * to the service.
   *
   * @param game A {@link Game} object.
   * @return {@link Single<Game>} A single {@link Game} object that the user can join whether it is
   * a new {@link Game} or another instance with the same game preferences with room for another
   * player.
   */
  public Single<Game> startGame(Game game) {
//    return signInService
//        .refreshBearerToken()
//        .observeOn(scheduler)
//        .flatMap((token) -> proxy.startGame(game, token))
//        .doOnSuccess(this::setGame);
    int[] origin = {1,1};
    return Single.fromSupplier(() -> {
         List<Ship> ships = Stream.generate(() -> {
            int x = origin[0] ;
                int y = origin[1];
            boolean vertical = (x < y);
            int length = 3;
            if (vertical) {
              origin[0]++;
            } else {
              origin[1]++;
            }
            return new Ship(x, y, length, vertical);
          })
              .limit(4)
              .collect(Collectors.toList());
          User user = new User();
          user.setDisplayName("ducky");
          Board board = new Board(user, List.of(), ships, true, false);
          return new Game(null, game.getBoardSize(), game.getPlayerCount(), List.of(board));
    })
        .subscribeOn(scheduler);
  }

  public Single<List<Ship>> submitShips(List<Ship> ships) {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> proxy.submitShips(game.getKey(), ships, token));
    // TODO: 4/4/2024 Write code to prevent users from placing ships when we don't want them to.
  }

  public Single<List<Shot>> submitShots(List<Shot> shots) {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> proxy.submitShots(game.getKey(), shots, token)); // TODO: 4/4/2024 Remove semicolon at the end of this line when we are ready to uncomment the following code.
//        .flatMap((shots) -> {
//          board.getShots().add(shot);
//          return (game.isTurn && (!board.isFleetSunk() && (game.isStart && !game.isFinished)))
//              ? Single.just(shots)
//              : // NO SHOTS FOR YOU!!!;
//        });
    // TODO: 4/4/2024 prevent people from submitting shots when fleetSunk = true or when game is over.
    // TODO: 4/4/2024 check to see if less than n-1 fleets are sunk.
    // TODO: 4/4/2024 prevent people from submitting shots when game is over.
  }

  public Single<Game> getGame(String key) {
    return signInService
        .refreshBearerToken()
        .flatMap((token) -> proxy.getGame(key, token).doOnSuccess(this::setGame));
  }


  private void setGame(Game game) {
    this.game = game;
  }

}
