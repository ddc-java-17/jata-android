package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.LinkedList;
import java.util.List;

public class Game {

  @SerializedName("key")
  @Expose(serialize = false, deserialize = true)
  private final String key;

  @Expose
  private final int boardSize;

  @Expose
  private final int playerCount;

  @Expose
  private final List<Board> boards;

  public Game(String key, int boardSize, int playerCount, List<Board> boards) {
    this.key = key;
    this.boardSize = boardSize;
    this.playerCount = playerCount;
    this.boards = boards;
  }

  public Game(int boardSize, int playerCount) {
    this(null, boardSize, playerCount, new LinkedList<>());
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

  public List<Board> getBoards() {
    return boards;
  }

}
