package edu.cnm.deepdive.jata.model;

import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import java.util.Objects;

/**
 * Encapsulates the data elements and logic for ships in local data store.
 */
public class Ship {

  @Expose
  private int x;

  @Expose
  private int y;

  @Expose
  private final int length;

  @Expose
  private boolean vertical;

  private boolean selected;

  /**
   * Initializes the fields in {@link Ship}.
   *
   * @param x        The x value of the ship coordinate.
   * @param y        The y value of the ship coordinate.
   * @param length   The length of a ship, and also the number of hits it can take.
   * @param vertical The boolean flag indicating the orientation of the ship on the board.
   */
  public Ship(int x, int y, int length, boolean vertical) {
    this.x = x;
    this.y = y;
    this.length = length;
    this.vertical = vertical;
  }

  /**
   * Initiates the final fields in this class.
   *
   * @param length Indicates the length of a particular {@link Ship}. This also
   *               indicates the total number of hits the {@code ship} can take, and the type of
   *               {@code ship} it is as well (Trireme vs Galley vs Canoe; basically flavor text).
   */
  public Ship(int length) {
    this.length = length;
  }

  /**
   * Gets the X coordinate of a {@code ship} on a board for other methods to access.
   * {@code x} makes up half of a ship location on the board.
   */
  public int getX() {
    return x;
  }

  /**
   * Sets the X coordinate of a {@code ship} on a board for other methods to access.
   * {@code x} makes up half of a ship location on the board.
   *
   * @param x The X coordinate of a {@code ship} on a board.
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * Gets the Y coordinate of a {@code ship} on a board for other methods to access.
   * {@code y} makes up half of a ship location on the board.
   */
  public int getY() {
    return y;
  }

  /**
   * Sets the Y coordinate of a {@code ship} on a board for other methods to access.
   * This makes up half of a ship location on the board.
   *
   * @param y Y coordinate of a {@code ship} on a board.
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * Gets {@code length}. The {@code length} is how many points on a board that a
   * {@code ship} occupies on a board. It also indicates how many hits a ship can take and what type
   * of {@code ship} it is.
   */
  public int getLength() {
    return length;
  }

  /**
   * Gets {@code Vertical}. {@code Vertical} is a boolean flag indicating if a ship has
   * been oriented vertically or horizontally on the board.
   */
  public boolean isVertical() {
    return vertical;
  }

  /**
   * Sets {@code Vertical} for other methods to access.
   *
   * @param vertical This is a boolean flag indicating if a ship has been oriented vertically or
   *                 horizontally on the board.
   */
  public void setVertical(boolean vertical) {
    this.vertical = vertical;
  }

  /**
   * Gets {@code selected}. {@code selected} is a boolean flag indicating if a ship has
   * been selected in the UI.
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * Sets {@code selected} for other methods to access.
   *
   * @param selected This is a boolean flag indicating if a ship has been selected in the UI.
   */
  public void setSelected(boolean selected) {
    this.selected = selected;
  }

  /**
   * Calculates if a ship includes a particular point on the board
   *
   * @param gridX The X coordinate of a point on the board's grid.
   * @param gridY The Y coordinate of a point on the board's grid.
   * @return includesPoint This is a boolean flag indicating whether a ship includes a point on the
   * board.
   */
  public boolean includesPoint(int gridX, int gridY) {
    return vertical
        ? x == gridX && gridY >= y && gridY < y + length
        : y == gridY && gridX >= x && gridX < x + length;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y, length, vertical);
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    boolean equals;
    if (obj == this) {
      equals = true;
    } else if (obj instanceof Ship other) {
      equals = this.x == other.x
          && this.y == other.y
          && this.length == other.length
          && this.vertical == other.vertical;
    } else {
      equals = false;
    }
    return equals;
  }
}


