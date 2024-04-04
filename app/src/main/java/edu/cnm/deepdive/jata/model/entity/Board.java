package edu.cnm.deepdive.jata.model.entity;

import com.google.gson.annotations.Expose;
import edu.cnm.deepdive.jata.model.Ship;
import edu.cnm.deepdive.jata.model.Shot;
import java.util.List;

public class Board {

  @Expose
  private final User player;
  @Expose
  private final List<Shot> shots;
  @Expose
  private final List<Ship> ships;
  @Expose
  private final boolean placed;
  @Expose
  private final boolean sunk;


  public Board(User player, List<Shot> shots, List<Ship> ships, boolean placed, boolean sunk) {
    this.player = player;
    this.shots = shots;
    this.ships = ships;
    this.placed = placed;
    this.sunk = sunk;
  }

  public User getPlayer() {
    return player;
  }

  public List<Shot> getShots() {
    return shots;
  }

  public List<Ship> getShips() {
    return ships;
  }

  public boolean isPlaced() {
    return placed;
  }

  public boolean isSunk() {
    return sunk;
  }
}
