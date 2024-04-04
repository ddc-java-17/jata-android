package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.cnm.deepdive.jata.model.entity.Board;

/**
 * This is the data structure for Ship in the game. This class will store relevant data for the
 * client side of the game.
 */
public class Ship {

  @Expose
  private final int shipNumber;

  @Expose
  private int shipCoordX;

  @Expose
  private int shipCoordY;

  @Expose
  private final int length;

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
   * {@code shipNumber} is the length of a particular {@link Ship}.
   *
   * @return shipNumber
   */
  public int getShipNumber() {
    return shipNumber;
  }

  /**
   *
   * @return
   */
  public int getShipCoordX() {
    return shipCoordX;
  }

  /**
   *
   * @param shipCoordX
   */
  public void setShipCoordX(int shipCoordX) {
    this.shipCoordX = shipCoordX;
  }

  /**
   *
   * @return
   */
  public int getShipCoordY() {
    return shipCoordY;
  }

  /**
   *
   * @param shipCoordY
   */
  public void setShipCoordY(int shipCoordY) {
    this.shipCoordY = shipCoordY;
  }

  /**
   *
   * @return
   */
  public int getLength() {
    return length;
  }

  /**
   *
   * @return
   */
  public boolean isVertical() {
    return isVertical;
  }

  public void setVertical(boolean vertical) {
    isVertical = vertical;
  }
}
