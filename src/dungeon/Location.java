package dungeon;

import java.util.Map;
import java.util.Set;

/**
 * Represents a location in the dungeon maze.
 * Location can be a cave/tunnel. If Location had 2 next moves then it's a tunnel, otherwise it
 * is a cave.
 */
public interface Location {

  /**
   * Returns the possible next moves from this LocationNode.
   *
   * @return a set of {@link Move}s
   */
  Set<Move> getNextMoves();

  /**
   * Returns the treasure in this LocationNode.
   *
   * @return a map of {@link Treasure} and its quantity.
   */
  Map<Treasure, Integer> getTreasure();

  /**
   * Returns the row position of this location node in maze grid.
   *
   * @return row number as integer value.
   */
  int getRow();

  /**
   * Returns the column position of this location node in maze grid.
   *
   * @return column number as integer value.
   */
  int getColumn();

  /**
   * Checks whether this LocationNode has treasure.
   *
   * @return true if treasure is present in the location.
   */
  boolean hasTreasure();

  /**
   * Checks whether the current location has a Monster.
   *
   * @return true if a monster is present at this location.
   */
  boolean hasMonster();

  /**
   * Returns whether the location is a cave or tunnel.
   *
   * @return true if it is a cave, false if it is a tunnel.
   */
  boolean isCave();

  /**
   * Checks whether this LocationNode has arrows.
   *
   * @return true if there are arrows at this location.
   */
  boolean hasArrows();

  /**
   * Returns the number of arrows at this location.
   *
   * @return number of arrows at this location.
   */
  int getArrows();

}
