package dungeon;

import java.util.List;
import java.util.Set;

/**
 * Represents the Dungeon game.
 * It contains the player, maze, treasure and maintains game state.
 */
public interface Dungeon {

  /**
   * Returns the maze of the dungeon.
   *
   * @return the maze as List of Lists of {@link Location}.
   */
  List<List<Location>> getMaze();

  /**
   * Returns the starting location/cave in the maze.
   *
   * @return {@link Location} representing the starting location.
   */
  Location getStartLocation();

  /**
   * Returns the end location/cave in the maze.
   *
   * @return {@link Location} representing the end location.
   */
  Location getEndLocation();

  /**
   * Returns the current location of player in the maze.
   *
   * @return {@link Location} representing the player's location.
   */
  Location getPlayerCurrentLocation();

  /**
   * Returns the moves that are possible from the current location.
   *
   * @return a Set of {@link Move}s
   */
  Set<Move> getAvailableMoves();

  /**
   * Checks whether the player has visited the end cave.
   *
   * @return true if {@link Player} has visited the end cave.
   */
  boolean playerVisitedEnd();

  /**
   * Moves the player in the maze in the provided Move direction.
   *
   * @param move {@link Move} to be executed.
   * @throws IllegalArgumentException if provided move is not a valid move.
   */
  void movePlayer(Move move) throws IllegalArgumentException;

  /**
   * Makes the player pick the treasure at the current location.
   *
   * @throws IllegalStateException if current location has no treasure.
   */
  void playerPickTreasure() throws IllegalStateException;

  /**
   * Provides the description of a player.
   * @return player in the maze as a {@link Player}.
   */
  Player getPlayerDescription();
}
