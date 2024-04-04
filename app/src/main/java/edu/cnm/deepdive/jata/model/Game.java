package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import java.util.LinkedList;
import java.util.List;

public class Game {

  @Expose(serialize = false, deserialize = true)
  private final String key;

  @Expose
  private final int boardSize;

  @Expose
  private final int playerCount;

  @Expose
  private final List<Board> boards;

  @Expose(serialize = false, deserialize = true)
  private final boolean isStart;

  @Expose(serialize = false, deserialize = true)
  private final boolean isFinished;

  public Game(String key, int boardSize, int playerCount, List<Board> boards, boolean isStart,
      boolean isFinished) {
    this.key = key;
    this.boardSize = boardSize;
    this.playerCount = playerCount;
    this.boards = boards;
    this.isStart = isStart;
    this.isFinished = isFinished;
  }

  public Game(int boardSize, int playerCount, boolean isStart, boolean isFinished) {
    this(null, boardSize, playerCount, new LinkedList<>(), isFinished, isStart);
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

  public boolean isStart() {
    return isStart;
  }

  public boolean isFinished() {
    return isFinished;
  }
}
