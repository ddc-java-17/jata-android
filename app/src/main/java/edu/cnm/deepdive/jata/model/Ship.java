package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;

/**
 * This is the domain class that represents {@link Ship} instances in the game. This class will
 * store and transfer relevant data for the client side of the game.
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
}
