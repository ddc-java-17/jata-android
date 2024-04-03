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
  private final int playerCount;

  // UserGame -> Board magic




  public Game(String id, int boardSize, int playerCount, List<Ship> ships, Date start) {
    this.id = id;
    this.boardSize = boardSize;
    this.playerCount = playerCount;
  }

  public String getId() {
    return id;
  }

  public int getBoardSize() {
    return boardSize;
  }

  public int getPlayerCount() {
    return playerCount;
  }
}
