package edu.cnm.deepdive.jata.model;

import androidx.annotation.Nullable;
import com.google.gson.annotations.Expose;
import java.util.Objects;

/**
 * This is the domain class that represents {@link Ship} instances in the game. This class will
 * store and transfer relevant data for the client side of the game, in addition to holding
 * business logic regarding
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
   * This constructor initializes the fields in {@link Ship}.
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
   * This constructor initiates the final fields in this class.
   *
   * @param length The number that indicates the length of a particular {@link Ship}. This also
   *               indicates the total number of hits the {@code ship} can take, and the type of
   *               {@code ship} it is as well (Trireme vs Galley vs Canoe; basically flavor text).
   */
  public Ship(int length) {
    this.length = length;
  }

  /**
   * This method gets the X coordinate of a {@code ship} on a board. This makes up half of a ship
   * location on the board.
   */
  public int getX() {
    return x;
  }

  /**
   * This method sets the X coordinate of a {@code ship} on a board. This makes up half of a ship
   * location on the board.
   *
   * @param x The X coordinate of a {@code ship} on a board.
   */
  public void setX(int x) {
    this.x = x;
  }

  /**
   * This method gets the Y coordinate of a {@code ship} on a board. This makes up half of a ship
   * location on the board.
   */
  public int getY() {
    return y;
  }

  /**
   * This method sets the Y coordinate of a {@code ship} on a board. This makes up half of a ship
   * location on the board.
   *
   * @param y The Y coordinate of a {@code ship} on a board.
   */
  public void setY(int y) {
    this.y = y;
  }

  /**
   * This method gets {@code length}. The {@code length} is how many points on a board that a
   * {@code ship} occupies on a board. It also indicates how many hits a ship can take and what type
   * of {@code ship} it is.
   */
  public int getLength() {
    return length;
  }

  /**
   * This method gets {@code isVertical}. {@code isVertical} is a boolean flag indicating if a ship
   * has been oriented vertically or horizontally on the board.
   */
  public boolean isVertical() {
    return vertical;
  }

  /**
   * This method sets {@code isVertical}.
   *
   * @param vertical This is a boolean flag indicating if a ship has been oriented vertically or
   *                 horizontally on the board.
   */
  public void setVertical(boolean vertical) {
    this.vertical = vertical;
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }

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


