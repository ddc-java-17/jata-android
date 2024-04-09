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


  @POST("games")
  Single<Game> startGame(
      @Body Game game,
      @Header("Authorization") String bearerToken);

  @GET("games/{key}")
  Single<Game> getGame(
      @Path("key") String key,
      @Header("Authorization") String bearerToken);

  @PUT("games/{gameKey}/ships")
  Single<Game> submitShips(
      @Path("gameKey") String key,
      @Body List<Ship> ships,
      @Header("Authorization") String bearerToken);

  @POST("games/{gameKey}/shots")
  Single<Game> submitShots(
      @Path("gameKey") String key,
      @Body List<Shot> shots,
      @Header("Authorization") String bearerToken);

}
