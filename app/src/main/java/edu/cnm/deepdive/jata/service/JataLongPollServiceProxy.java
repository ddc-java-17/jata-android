package edu.cnm.deepdive.jata.service;

import edu.cnm.deepdive.jata.model.Game;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Path;

/**
 * Requests updates to the {@link Game} object by making {@link HTTP} requests at regular intervals.
 * These long polls allow users to get updates as players fire shots, and updates whose turn it is,
 * whose fleets are sunk, and when the game is finished.
 */
public interface JataLongPollServiceProxy {

  @GET("games/{key}")
  Single<Game> getGame(
      @Path("key") String key,
      @Header("Authorization") String bearerToken);

}
