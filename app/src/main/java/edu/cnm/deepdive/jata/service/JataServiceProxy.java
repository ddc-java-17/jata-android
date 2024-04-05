package edu.cnm.deepdive.jata.service;

import edu.cnm.deepdive.jata.model.Game;
import edu.cnm.deepdive.jata.model.Ship;
import edu.cnm.deepdive.jata.model.Shot;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * This interface is what allows the client or app side logic to speak to the server side logic.
 */
public interface JataServiceProxy {

  /**
   * This method sends an HTTP {@link POST} request to the server to initialize a {@code game}. The
   * game object the player sends will have game parameters that the user wants for their game
   * ({@code boardSize} and {@code playerCount}) and the server will return either a new game object
   * or a game object that has been created by another player that the current user can join.
   *
   * @param game        This is the {@link Game} object the user will send containing the preferred
   *                    parameters for their game.
   * @param bearerToken This is a randomly generated authentication token we use to confirm that the
   *                    user is who they say they are.
   * @return
   */
  @POST("games")
  Single<Game> startGame(
      @Body Game game,
      @Header("Authorization") String bearerToken);

  /**
   * This method sends an HTTP {@link GET} request to the server for a particular {@link Game}
   * object. The server will respond with the {@code game} object with that {@code key}.
   *
   * @param key         This is the unique key for the {@code game}.
   * @param bearerToken This is a randomly generated authentication token we use to confirm that the
   *                    user is who they say they are.
   * @return {@link Single} task that will return the server's {@code game} object.
   */
  @GET("games/{gameKey}")
  Single<Game> getGame(
      @Path("gameKey") String key,
      @Header("Authorization") String bearerToken);

  /**
   *
   *
   * @param key
   * @param ships
   * @param bearerToken
   * @return
   */
  @POST("games/{gameKey}/ships")
  // may be a PUT, talk to Reed bout it when he isn't working on getting the server to build. POST does work
  Single<List<Ship>> submitShips(
      @Path("gameKey") String key,
      @Body List<Ship> ships,
      @Header("Authorization") String bearerToken);

  @POST("games/{gameKey}/shots")
  Single<List<Shot>> submitShots(
      @Path("gameKey") String key,
      @Body List<Shot> shots,
      @Header("Authorization") String bearerToken);

  // TODO: 4/3/2024 users/me endpoint working first.

  // TODO: 4/4/2024 Long poll will go here.


}
