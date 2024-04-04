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

//  @Expose(serialize = false, deserialize = true)
//  private final boolean isStart;
//
//  @Expose(serialize = false, deserialize = true)
//  private final boolean isFinished;
//
//  @Expose(serialize = false, deserialize = true)
//  private final boolean isTurn;
  // TODO: 4/4/2024 Uncomment when I can figure out how to initialize this stuff without upsetting the constructor situation.

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
