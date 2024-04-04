package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Game {

  @SerializedName("key")
  @Expose(serialize = false, deserialize = true)
  private final String key;

  @Expose
  private final int boardSize;

  @Expose
  private final int playerCount;

  // TODO: 4/4/2024 Add isFinished boolean flag.

  public Game(String key, int boardSize, int playerCount) {
    this.key = key;
    this.boardSize = boardSize;
    this.playerCount = playerCount;
  }

  public String getKey() {
    return key;
  }

  public int getBoardSize() {
    return boardSize;
  }

  public int getPlayerCount() {
    return playerCount;
  }
}
