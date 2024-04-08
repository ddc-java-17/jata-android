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

  /**
   * Sends an HTTP {@link GET} request to the server for a particular {@link Game} object.
   *
   * @param key         This is the unique key for the {@code game}.
   * @param bearerToken This is a randomly generated authentication token we use to confirm that the
   *                    user is who they say they are.
   * @return {@link Single} task that will return the server's {@code game} object.
   */
  @GET("games/{key}")
  Single<Game> getGame(
      @Path("key") String key,
      @Header("Authorization") String bearerToken);

}
