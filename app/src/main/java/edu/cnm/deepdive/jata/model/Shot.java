package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import edu.cnm.deepdive.jata.model.entity.User;

public class Shot {

  @Expose(serialize = true, deserialize = false)
  private final User toUser;

  @Expose
  private final int x;

  @Expose
  private final int y;


  public Shot(User toUser, int x, int y) {
    this.toUser = toUser;
    this.x = x;
    this.y = y;
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
