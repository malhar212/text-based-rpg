package dungeon;

import java.util.Map;

/**
 * Represents a player in th dungeon game.
 * It holds the player's properties like the treasure and treasure quantity that the player has
 * accumulated.
 */
public interface Player {

  /**
   * Returns the treasure this Player holds.
   *
   * @return a map of {@link Treasure} and its quantity.
   */
  Map<Treasure, Integer> getTreasure();

  /**
   * Checks whether this Player has treasure.
   *
   * @return true if Player has treasure.
   */
  boolean hasTreasure();
}
