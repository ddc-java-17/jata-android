package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

  public Ship(int shipNumber, int length) {
    this.shipNumber = shipNumber;
    this.length = length;
  }

  /**
   * This getter gets the {@code shipNumber}
   * @return
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
