package edu.cnm.deepdive.jata.service;

import edu.cnm.deepdive.jata.model.Game;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;

public class JataRepository {

  private static final String TAG = JataRepository.class.getSimpleName();

  private final JataServiceProxy proxy;
  private final GoogleSignInService signInService;
  private final Scheduler scheduler;
  private Game game;

  /**
   * This constructor
   *
   * @param proxy
   * @param signInService
   * @param scheduler
   */
  @Inject
  JataRepository(JataServiceProxy proxy, GoogleSignInService signInService, Scheduler scheduler) {
    this.proxy = proxy;
    this.signInService = signInService;
    scheduler = Schedulers.single();
  }

  public Single<Game> startGame(Game game) {
    return signInService
        .refreshBearerToken()
        .observeOn(scheduler)
        .flatMap((token) -> proxy.startGame(game, token))
        .doOnSuccess(this::setGame);
  }

  private void setGame(Game game) {
    this.game = game;
  }

}
