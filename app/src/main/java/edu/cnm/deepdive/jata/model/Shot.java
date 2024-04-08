package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import edu.cnm.deepdive.jata.model.entity.User;

/**
 * Encapsulates the data and logic for shots fired during the game.
 */
public class Shot {

  @Expose(serialize = true, deserialize = false)
  private final User toUser;

  @Expose
  private final int x;

  @Expose
  private final int y;

  @Expose
  private final boolean isHit;

  /**
   * Initializes final fields in absence of Gson initialization.
   *
   * @param toUser User who is being fired at.
   * @param x      X coordinate of where on the grid the shot is being fired.
   * @param y      Y coordinate of where on the grid the shot is being fired.
   * @param isHit  Indicates if a shot has hit a ship.
   */
  public Shot(User toUser, int x, int y, boolean isHit) {
    this.toUser = toUser;
    this.x = x;
    this.y = y;
    this.isHit = isHit;
  }

  /**
   * Gets the X coordinate of where on the grid the shot is being fired.
   */
  public int getX() {
    return x;
  }

  /**
   * Gets the Y coordinate of where in the grid the shot is being fired.
   */
  public int getY() {
    return y;
  }

  /**
   * Gets the {@link User} object who is being fired at.
   */
  public User getToUser() {
    return toUser;
  }

  /**
   * Gets the boolean flag indicating whether a shot has hit a ship.
   */
  public boolean isHit() {
    return isHit;
  }
}
