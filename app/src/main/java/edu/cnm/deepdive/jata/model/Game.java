package edu.cnm.deepdive.jata.model;

import com.google.gson.annotations.Expose;
import java.util.LinkedList;
import java.util.List;

/**
 * Encapsulates data and logic regarding the {@link Game} object.
 */
public class Game {

  @Expose(serialize = false, deserialize = true)
  private final String key;

  @Expose
  private final int boardSize;

  @Expose
  private final int playerCount;

  @Expose(serialize = false, deserialize = true)
  private final List<Board> boards;

  @Expose(serialize = false, deserialize = true)
  private final boolean started;

  @Expose(serialize = false, deserialize = true)
  private final boolean finished;

  @Expose(serialize = false, deserialize = true)
  private final boolean yourTurn;

  /**
   * Initializes final fields in the absence of Gson initialization.
   *
   * @param key Unique {@code key} for the {@link Game} instance.
   * @param boardSize Selected grid size that the game will be played on.
   * @param playerCount Number of players in a game.
   * @param boards {@link List} of every player's board.
   * @param started Indicates whether game has started.
   * @param finished Indicates whether game has finished.
   * @param yourTurn Indicates whether it is the user's turn.
   */
  public Game(String key, int boardSize, int playerCount, List<Board> boards,  boolean started,
      boolean finished, boolean yourTurn) {
    this.key = key;
    this.boardSize = boardSize;
    this.playerCount = playerCount;
    this.boards = boards;
    this.started = started;
    this.finished = finished;
    this.yourTurn = yourTurn;
  }

  /**
   * Constructs a game instance with the two parameters that the server requires to start a game.
   *
   * @param boardSize Selected grid size that the game will be played on.
   * @param playerCount Number of players in a game.
   */
  public Game(int boardSize, int playerCount) {
    this(null, boardSize, playerCount, new LinkedList<>(), false, false, false);
  }

  /**
   * Gets unique key for the {@link Game} instance.
   */
  public String getKey() {
    return key;
  }

  /**
   * Gets the selected grid size that the game will be played on.
   */
  public int getBoardSize() {
    return boardSize;
  }

  /**
   * Gets the number of players in a game.
   */
  public int getPlayerCount() {
    return playerCount;
  }

  /**
   * Gets the {@link List} of every player's board.
   */
  public List<Board> getBoards() {
    return boards;
  }

  /**
   * Gets the boolean flag indicating whether a game has started.
   */
  public boolean isStarted() {
    return started;
  }

  /**
   * Gets the boolean flag indicating whether a game has finished.
   */
  public boolean isFinished() {
    return finished;
  }

  /**
   * Gets the boolean flag indicating whether it is the user's turn.
   */
  public boolean isYourTurn() {
    return yourTurn;
  }
}
