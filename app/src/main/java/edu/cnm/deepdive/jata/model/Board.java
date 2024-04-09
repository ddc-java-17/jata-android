package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.cnm.deepdive.jata.model.entity.User;
import java.util.List;

/**
 * Domain class handing Board object.
 */
public class Board {

  @Expose
  private final User player;
  @Expose
  @SerializedName("toShots")
  private final List<Shot> shots;
  @Expose
  private final List<Ship> ships;
  @Expose
  private final boolean placed;
  @Expose
  private final boolean fleetSunk;
  @Expose
  private boolean mine;

  /**
   * Constructor that initiates final fields.
   * @param player The specified player for a certain board.
   * @param shots A list of shots against a specified board.
   * @param ships A list of ships placed on a specified board.
   * @param placed A boolean flag for when the ships have been locked into place at the start of a game.
   * @param fleetSunk A boolean flag testing if a player has had all their ships sunk, eliminating them from the game.
   */
  public Board(User player, List<Shot> shots, List<Ship> ships, boolean placed, boolean fleetSunk) {
    this.player = player;
    this.shots = shots;
    this.ships = ships;
    this.placed = placed;
    this.fleetSunk = fleetSunk;
  }

  /**
   * gets the User for a board.
   * @return player.
   */
  public User getPlayer() {
    return player;
  }

  /**
   * Gets the List<Shots> against a board.
   * @return shots.
   */
  public List<Shot> getShots() {
    return shots;
  }

  /**
   * Gets the ships on a board.
   * @return ships.
   */
  public List<Ship> getShips() {
    return ships;
  }

  /**
   * tests whether ships have been locked in or not.
   * @return placed.
   */
  public boolean isPlaced() {
    return placed;
  }

  /**
   * Tests whether all your ships have been sunk or not.
   * @return fleetSunk.
   */
  public boolean isFleetSunk() {
    return fleetSunk;
  }

  /**
   * Tests to find which board is the current users.
   * @return mine.
   */
  public boolean isMine() {
    return mine;
  }

  /**
   * Sets the board to a specific user.
   * @param mine boolean mine.
   */
  public void setMine(boolean mine) {
    this.mine = mine;
  }
}


