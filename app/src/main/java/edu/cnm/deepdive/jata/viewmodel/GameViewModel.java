package edu.cnm.deepdive.jata.viewmodel;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.jata.model.Game;
import edu.cnm.deepdive.jata.model.Ship;
import edu.cnm.deepdive.jata.service.JataRepository;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import java.util.List;
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

  @Inject
  public GameViewModel(JataRepository jataRepository) {
    this.jataRepository = jataRepository;
    game = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    gamePoll = jataRepository.pollGameStatus();
    throwablePoll = jataRepository.pollThrowable();
    pollGame();
    pollThrowable();
  }

  public LiveData<Game> getGame() {
    return game;
  }

  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  public void startGame(int boardSize, int playerCount) {
    jataRepository.startGame(new Game(boardSize, playerCount));
  }

  public void pollGame() {
    gamePoll
        .subscribe(
            game::postValue,
            this::postThrowable,
            () -> {},
            pending
        );
  }

  public void pollThrowable() {
    throwablePoll
        .subscribe(
            this::postThrowable,
            this::postThrowable,
            () -> {},
            pending
        );
  }

  public void changePlacement(List<Ship> ships, int boardIndex) {
    jataRepository.changePlacement(ships, boardIndex);
  }

  public void changePlacement(List<Ship> ships, int boardIndex, Ship ship) {
    jataRepository.changePlacement(ships, boardIndex, ship);
  }

  @Override
  public void onStart(@NotNull LifecycleOwner owner) {
    DefaultLifecycleObserver.super.onStart(owner);
//    gamePoll = jataRepository.pollGameStatus();
//    throwablePoll = jataRepository.pollThrowable();
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
