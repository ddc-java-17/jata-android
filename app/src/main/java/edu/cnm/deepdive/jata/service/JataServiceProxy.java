package edu.cnm.deepdive.jata.service;

import edu.cnm.deepdive.jata.model.Game;
import edu.cnm.deepdive.jata.model.Ship;
import edu.cnm.deepdive.jata.model.Shot;
import edu.cnm.deepdive.jata.model.entity.User;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.UUID;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * This interface is what allows the client or app side logic to speak to the server side logic.
 */
public interface JataServiceProxy {

  // GAME
  @POST("games")
  Single<Game> startGame(
      @Body Game game,
      @Header("Authorization") String bearerToken);

  @GET("games/{id}")
  Single<Game> getGame(
      @Path("id") String id,
      @Header("Authorization") String bearerToken);

  // SHIP
  @POST("games/{gameKey}/ships")
  List<Ship> submitShips(
      @Path("gameKey") String key,
      @Body List<Ship> ships
      @Header("Authorization") String bearerToken);

  // TODO: 4/3/2024 users/me endpoint working first.

}
