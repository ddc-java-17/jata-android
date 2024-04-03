package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import edu.cnm.deepdive.jata.model.entity.User;
import java.util.Date;
import java.util.List;

public class Game {

  @SerializedName("key")
  @Expose(serialize = false, deserialize = true)
  private final String id;

  @Expose
  private final int boardSize;

  @Expose
  private final int numPlayers;
  @Expose()
  private final List<Ship> ships;

  @SerializedName("created")
  @Expose(serialize = false, deserialize = true)
  private final Date start;

  public Game(String id, int boardSize, int numPlayers, List<Ship> ships, Date start) {
    this.id = id;
    this.boardSize = boardSize;
    this.numPlayers = numPlayers;
    this.ships = ships;
    this.start = start;
  }
}
