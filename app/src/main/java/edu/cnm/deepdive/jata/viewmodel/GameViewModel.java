package edu.cnm.deepdive.jata.viewmodel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.jata.model.Board;
import edu.cnm.deepdive.jata.model.Game;
import edu.cnm.deepdive.jata.model.Ship;
import edu.cnm.deepdive.jata.model.Shot;
import edu.cnm.deepdive.jata.model.entity.User;
import edu.cnm.deepdive.jata.service.JataRepository;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

@HiltViewModel
public class GameViewModel extends ViewModel implements DefaultLifecycleObserver {

  private static final String TAG = GameViewModel.class.getSimpleName();

  private final JataRepository jataRepository;
  private final MutableLiveData<Game> game;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;
  private Observable<Game> gamePoll;
  private Observable<Throwable> throwablePoll;
  private final MutableLiveData<Map<Integer, boolean[][]>> pendingShots;
  private LiveData<Integer> shotLimit;
  private MutableLiveData<Integer> shotCounter;
  private MediatorLiveData<Integer> shotsRemaining;

  /**
   * Constructor for the GameViewModel, implements all private fields.
   *
   * @param jataRepository JataRepository
   */
  /** @noinspection DataFlowIssue*/
  @Inject
  public GameViewModel(JataRepository jataRepository) {
    this.jataRepository = jataRepository;
    game = new MutableLiveData<>();
    shotLimit = Transformations.map(game, (g) ->
        (int) (g.getBoards()
            .stream()
            .filter((board) -> !board.isFleetSunk())
            .count() - 1));
    shotCounter = new MutableLiveData<>(0);
    shotsRemaining = new MediatorLiveData<>();
    shotsRemaining.addSource(shotLimit,
        (limit) -> shotsRemaining.setValue(limit - shotCounter.getValue()));
    shotsRemaining.addSource(shotCounter,
        (count) -> shotsRemaining.setValue(shotLimit.getValue() - count));
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    gamePoll = jataRepository.pollGameStatus();
    throwablePoll = jataRepository.pollThrowable();
    pendingShots = new MutableLiveData<>(new HashMap<>());
    pollGame();
    pollThrowable();
  }

  /**
   * Gets the game object.
   *
   * @return game
   */
  public LiveData<Game> getGame() {
    return game;
  }

  /**
   * Gets all pending shots.
   *
   * @return pendingShots
   */
  public LiveData<Map<Integer, boolean[][]>> getPendingShots() {
    return pendingShots;
  }

  public LiveData<Integer> getShotLimit() {
    return shotLimit;
  }

  public LiveData<Integer> getShotCounter() {
    return shotCounter;
  }

  public LiveData<Integer> getShotsRemaining() {
    return shotsRemaining;
  }

  /**
   * Gets any throwable needed.
   *
   * @return throwable
   */
  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  /**
   * Method used to start a game.
   *
   * @param boardSize   int
   * @param playerCount int
   */
  public void startGame(int boardSize, int playerCount) {
    jataRepository.startGame(new Game(boardSize, playerCount));
  }

  /**
   * Sends a poll to check current game for any updates.
   */
  public void pollGame() {
    gamePoll
        .subscribe(
            game::postValue,
            this::postThrowable,
            () -> {
            },
            pending
        );
  }

  /**
   * Sends a poll to check for any throwable messages needed.
   */
  public void pollThrowable() {
    throwablePoll
        .subscribe(
            this::postThrowable,
            this::postThrowable,
            () -> {
            },
            pending
        );
  }

  /**
   * method used to change the placement of a ship before game has started.
   *
   * @param ships      List<Ship>
   * @param boardIndex int
   */
  public void changePlacement(List<Ship> ships, int boardIndex) {
    jataRepository.changePlacement(ships, boardIndex);
  }

  /**
   * Method used to change the placement of a ship before game has started.
   *
   * @param ships      List<Ship>
   * @param boardIndex int
   * @param ship       Ship
   */
  public void changePlacement(List<Ship> ships, int boardIndex, Ship ship) {
    jataRepository.changePlacement(ships, boardIndex, ship);
  }

  /**
   * Method used to submit your ships and lock them in for a game.
   *
   * @param boardIndex int
   */
  public void submitShips(int boardIndex) {
    //noinspection DataFlowIssue
    jataRepository.submitShips(game.getValue().getBoards().get(boardIndex).getShips());
  }
  /**
  * Method used to place a shot before firing, on tap it will show a red circle where your shot would
  * be fired, same spot can be tapped again to remove that shot so it can be placed somewhere else.
  */

  /**
   * @noinspection DataFlowIssue
   */
  public void toggleShots(int boardIndex, int gridX, int gridY) {
    int limit = shotLimit.getValue();
    int count = shotCounter.getValue();
    Map<Integer, boolean[][]> pendingShots = this.pendingShots.getValue();
    int boardSize = game.getValue().getBoardSize();
    boolean[][] boardPendingShots = pendingShots.computeIfAbsent(boardIndex,
        (index) -> new boolean[boardSize][boardSize]);
    if (boardPendingShots[gridY - 1][gridX - 1]) {
      boardPendingShots[gridY - 1][gridX - 1] = false;
      shotCounter.setValue(count - 1);
    } else if (count < limit) {
      boardPendingShots[gridY - 1][gridX - 1] = true;
      shotCounter.setValue(count +1);
    }
    this.pendingShots.setValue(pendingShots);
  }
  /**
   * Method used to submit your shots against other users.
   */
  /**
   * @noinspection DataFlowIssue
   */
  public void submitShots() {
    Map<Integer, boolean[][]> pendingShots = this.pendingShots.getValue();
    List<Board> boards = game.getValue().getBoards();
    List<Shot> shotsToSubmit = pendingShots.entrySet()
        .stream()
        .flatMap((entry) -> {
          int boardIndex = entry.getKey();
          User user = boards.get(boardIndex).getPlayer();
          int[] rowIndex = {0};
          return Arrays.stream(entry.getValue())
              .flatMap((row) -> {
                rowIndex[0]++;
                return IntStream.range(0, row.length)
                    .filter((columnIndex) -> row[columnIndex])
                    .mapToObj((columnIndex) -> new Shot(user, columnIndex + 1, rowIndex[0], false));
              });
        })
        .collect(Collectors.toList());
    pendingShots.clear();
    shotCounter = 0;
    jataRepository.submitShots(shotsToSubmit);
  }

  @Override
  public void onStart(@NotNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onStart(owner);
    gamePoll = jataRepository.pollGameStatus();
    throwablePoll = jataRepository.pollThrowable();
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    pending.clear();
    jataRepository.stopPolling();
    DefaultLifecycleObserver.super.onStop(owner);
  }

  private void postThrowable(Throwable throwable) {
    Log.e(TAG, throwable.getMessage(), throwable);
    this.throwable.postValue(throwable);
  }
}
