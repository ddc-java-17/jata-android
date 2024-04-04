package edu.cnm.deepdive.jata.service;

import edu.cnm.deepdive.jata.model.Game;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;

/**
 * This class is the link between the view model and the {@link JataServiceProxy} that talks to the
 * server. Its methods are meant to be invoked by the view model, then invoke code that sends the
 * relevant Json packet to the server.
 */
public class JataRepository {

  private static final String TAG = JataRepository.class.getSimpleName();

  private final JataServiceProxy proxy;
  private final GoogleSignInService signInService;
  private final Scheduler scheduler;
  private Game game;

  /**
   * This constructor initializes
   *
   * @param proxy This is {@link JataServiceProxy} and its job is to talk to the server when it is
   *             asked to.
   * @param signInService This is {@link GoogleSignInService} and it allows our users to log into our
   *                      app and service.
   */
  @Inject
  JataRepository(JataServiceProxy proxy, GoogleSignInService signInService) {
    this.proxy = proxy;
    this.signInService = signInService;
    scheduler = Schedulers.single();
  }

  /**
   * This method is the bridge between the UI and the {@link JataServiceProxy}. When the user taps
   * the Start Game button, it will invoke this method, then it will invoke the method
   * {@link JataServiceProxy#startGame(Game, String)} to send the request to the service.
   *
   * @param game A {@link Game} object.
   * @return {@link Single<Game>} A single {@link Game} object that the user can join whether it is
   *                              a new {@link Game} or another instance with the same game
   *                              preferences with room for another player.
   */
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
