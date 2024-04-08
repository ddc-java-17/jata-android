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
 * This interface is what allows the client to regularly request updates to the {@link Game} object.
 * These long polls allow users to get updates as people fire shots, and it also updates whose turn
 * it is.
 */
public interface JataLongPollServiceProxy {

  /**
   * This method sends an HTTP {@link GET} request to the server for a particular {@link Game}
   * object when invoked. The server will respond with the {@link Game} object with that {@code key}.
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
