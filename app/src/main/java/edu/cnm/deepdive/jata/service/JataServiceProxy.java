package edu.cnm.deepdive.jata.service;

import edu.cnm.deepdive.jata.model.Game;
import edu.cnm.deepdive.jata.model.Ship;
import edu.cnm.deepdive.jata.model.Shot;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Invoked by the repository to make {@link HTTP} requests to the server.
 */
public interface JataServiceProxy {

  /**
   * Sends an HTTP {@link POST} request to the server to initialize a {@code game}. The game object
   * the player sends will have game parameters that the user wants for their game
   * ({@code boardSize} and {@code playerCount}) and the server will return either a new game object
   * or a game object that has been created by another player that the current user can join.
   *
   * @param game        {@link Game} object the user will send containing the preferred parameters
   *                    for their game.
   * @param bearerToken Randomly generated authentication token we use to confirm that the user is
   *                    who they say they are.
   * @return {@link Single} updated {@link Game} object that the server sends back.
   */
  @POST("games")
  Single<Game> startGame(
      @Body Game game,
      @Header("Authorization") String bearerToken);

  /**
   * Sends an HTTP {@link GET} request to the server for a particular {@link Game} object.
   *
   * @param key         Unique key for the {@code game}.
   * @param bearerToken Randomly generated authentication token we use to confirm that the user is
   *                    who they say they are.
   * @return {@link Single} updated {@link Game} object that the server sends back.
   */
  @GET("games/{key}")
  Single<Game> getGame(
      @Path("key") String key,
      @Header("Authorization") String bearerToken);

  /**
   * Sends a {@link PUT} request to the server to submitthe current placement of {@code ships} on
   * the board.
   *
   * @param key         Unique identifying {@link String} that identifies the current {@link Game}
   *                    object.
   * @param ships       {@link List} of {@link Ship} that the player has placed on their board in a
   *                    valid formation.
   * @param bearerToken Randomly generated authentication token we use to confirm that the user is
   *                    who they say they are.
   * @return {@link Single} updated {@link Game} object that the server sends back.
   */
  @PUT("games/{gameKey}/ships")
  Single<Game> submitShips(
      @Path("gameKey") String key,
      @Body List<Ship> ships,
      @Header("Authorization") String bearerToken);

  /**
   * Sends a {@link POST} request to the server to submit the current {@code shots} on the board.
   *
   * @param key         Unique identifying {@link String} that identifies the current {@link Game}
   *                    object.
   * @param shots       {@link List} of valid {@link Shot} that the player has fired at other
   *                    players or themselves.
   * @param bearerToken Randomly generated authentication token we use to confirm that the user is
   *                    who they say they are.
   * @return {@link Single} updated {@link Game} object that the server sends back.
   */
  @POST("games/{gameKey}/shots")
  Single<Game> submitShots(
      @Path("gameKey") String key,
      @Body List<Shot> shots,
      @Header("Authorization") String bearerToken);

}
