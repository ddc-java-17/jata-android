package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.cnm.deepdive.jata.model.entity.User;

public class Shot {

  @Expose(serialize = false, deserialize = true)
  private final String key;

  @Expose
  private final User toUser;

  @Expose
  private final int x;

  @Expose
  private final int y;


  public Shot(String key, User toUser, int x, int y) {
    this.key = key;
    this.toUser = toUser;
    this.x = x;
    this.y = y;
  }

  public Shot(User toUser, int x, int y) {
    this(null, toUser, x, y);
  }

  public String getKey() {
    return key;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public User getToUser() {
    return toUser;
  }

}
