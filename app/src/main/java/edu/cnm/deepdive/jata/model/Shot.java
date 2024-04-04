package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.cnm.deepdive.jata.model.entity.Board;
import edu.cnm.deepdive.jata.model.entity.User;

/**
 *
 */
public class Shot {

  @SerializedName("key")
  @Expose(serialize = false, deserialize = true)
  private final String id;

  @Expose
  private final User toUser;

  @Expose
  private final int shotCoordX;

  @Expose
  private final int shotCoordY;


  public Shot(String id, User toUser, int shotCoordX, int shotCoordY) {
    this.id = id;
    this.toUser = toUser;
    this.shotCoordX = shotCoordX;
    this.shotCoordY = shotCoordY;
  }

  /**
   *
   * @return
   */
  public String getId() {
    return id;
  }

  /**
   *
   * @return
   */
  public User getToUser() {
    return toUser;
  }

  /**
   *
   * @return
   */
  public int getShotCoordX() {
    return shotCoordX;
  }

  /**
   *
   * @return
   */
  public int getShotCoordY() {
    return shotCoordY;
  }
}
