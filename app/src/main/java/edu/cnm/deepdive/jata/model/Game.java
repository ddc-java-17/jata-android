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
  private final boolean fleetSunk;

  @Expose(serialize = false, deserialize = true)
  private final boolean started;

  @Expose(serialize = false, deserialize = true)
  private final boolean finished;

  @Expose(serialize = false, deserialize = true)
  private final boolean yourTurn;
  // TODO: 4/4/2024 Uncomment when I can figure out how to initialize this stuff without upsetting the constructor situation.

  public Game(String key, int boardSize, int playerCount, List<Board> boards, boolean fleetSunk, boolean started,
      boolean finished, boolean yourTurn) {
    this.key = key;
    this.boardSize = boardSize;
    this.playerCount = playerCount;
    this.boards = boards;
    this.fleetSunk = fleetSunk;
    this.started = started;
    this.finished = finished;
    this.yourTurn = yourTurn;
  }

  public Game(int boardSize, int playerCount) {
    this(null, boardSize, playerCount, new LinkedList<>(), false, false, false, false);
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
