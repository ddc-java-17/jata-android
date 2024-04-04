package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.cnm.deepdive.jata.model.entity.Board;

/**
 * This is the domain class that represents {@link Ship} instances in the game. This class will
 * store and transfer relevant data for the client side of the game.
 */
public class Ship {

  @Expose
  private final int shipNumber;

  @SerializedName("x")
  @Expose
  private int shipCoordX;

  @SerializedName("y")
  @Expose
  private int shipCoordY;

  @Expose
  private final int length;

  @SerializedName("vertical")
  @Expose
  private boolean isVertical;

  /**
   * This constructor initiates the final fields in this class.
   *
   * @param shipNumber The identifying number of a {@link Ship} on any given {@link Board} instance.
   *                   Every {@code shipNumber} is unique to the {@link Board} instance, but not the
   *                   {@link Game} instance.
   * @param length The number that indicates the length of a particular {@link Ship}. This also
   *               indicates the total number of hits the {@code ship} can take, and the type of
   *               {@code ship} it is as well (Trireme vs Galley vs Canoe; basically flavor text).
   */
  public Ship(int shipNumber, int length) {
    this.shipNumber = shipNumber;
    this.length = length;
  }

  /**
   * This method gets the {@code shipNumber} for a particular {@code ship}. This is the identifying
   * number of a {@link Ship} on any given {@link Board} instance. Every {@code shipNumber} is
   * unique to the {@link Board} instance, but not the {@link Game} instance.
   */
  public int getShipNumber() {
    return shipNumber;
  }

  /**
   * This method gets the X coordinate of a {@code ship} on a board. This makes up half of a ship
   * location on the board.
   */
  public int getShipCoordX() {
    return shipCoordX;
  }

  /**
   * This method sets the X coordinate of a {@code ship} on a board. This makes up half of a ship
   * location on the board.
   *
   * @param shipCoordX The X coordinate of a {@code ship} on a board.
   */
  public void setShipCoordX(int shipCoordX) {
    this.shipCoordX = shipCoordX;
  }

  /**
   * This method gets the Y coordinate of a {@code ship} on a board. This makes up half of a ship
   * location on the board.
   */
  public int getShipCoordY() {
    return shipCoordY;
  }

  /**
   * This method sets the Y coordinate of a {@code ship} on a board. This makes up half of a ship
   * location on the board.
   *
   * @param shipCoordY The Y coordinate of a {@code ship} on a board.
   */
  public void setShipCoordY(int shipCoordY) {
    this.shipCoordY = shipCoordY;
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
    return isVertical;
  }

  /**
   * This method sets {@code isVertical}.
   *
   * @param vertical This is a boolean flag indicating if a ship has been oriented vertically or
   *                 horizontally on the board.
   */
  public void setVertical(boolean vertical) {
    isVertical = vertical;
  }
}
