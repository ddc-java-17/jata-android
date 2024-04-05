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
   *
   * @param game
   * @param bearerToken
   * @return
   */
  @POST("games")
  Single<Game> startGame(
      @Body Game game,
      @Header("Authorization") String bearerToken);

  @GET("games/{gameKey}")
  Single<Game> getGame(
      @Path("gameKey") String key,
      @Header("Authorization") String bearerToken);

  // SHIP
  @POST("games/{gameKey}/ships") // may be a PUT, talk to Reed bout it when he isn't working on getting the server to build. POST does work
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
