package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.cnm.deepdive.jata.model.entity.User;
import java.util.List;

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


  public Board(User player, List<Shot> shots, List<Ship> ships, boolean placed, boolean fleetSunk) {
    this.player = player;
    this.shots = shots;
    this.ships = ships;
    this.placed = placed;
    this.fleetSunk = fleetSunk;
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

  public boolean isFleetSunk() {
    return fleetSunk;
  }

  public boolean isMine() {
    return mine;
  }

  public void setMine(boolean mine) {
    this.mine = mine;
  }
}


