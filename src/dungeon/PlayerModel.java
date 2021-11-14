package dungeon;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an instance of {@link Player} in th dungeon game.
 * It holds the player's properties like the treasure and treasure quantity that the player has
 * accumulated.
 */
public class PlayerModel implements PlayerPrivate {
  private final Map<Treasure, Integer> treasure;

  /**
   * Creates a new instance of PlayerModel.
   */
  public PlayerModel() {
    this.treasure = new HashMap<>();
  }

  /**
   * Returns the treasure this Player holds.
   *
   * @return a map of {@link Treasure} and its quantity.
   */
  @Override
  public Map<Treasure, Integer> getTreasure() {
    return new HashMap<>(treasure);
  }

  /**
   * Checks whether this Player has treasure.
   *
   * @return true if Player has treasure.
   */
  @Override
  public boolean hasTreasure() {
    if (treasure.size() > 0) {
      for (Integer quantity : treasure.values()) {
        if (quantity > 0) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Adds the provided treasure and quantity to the player's treasure.
   *
   * @param treasure         treasure type to be added.
   * @param treasureQuantity treasure quantity to be added.
   * @throws IllegalArgumentException if provided treasure type is null.
   * @throws IllegalArgumentException if provided treasure quantity is negative.
   */
  //package-private due to interface
  @Override
  public void addToTreasure(Treasure treasure, int treasureQuantity)
          throws IllegalArgumentException {
    if (treasure == null) {
      throw new IllegalArgumentException("Treasure cannot be null");
    }
    if (treasureQuantity < 0) {
      throw new IllegalArgumentException("Treasure quantity cannot be less than 0");
    }
    Integer integer = this.treasure.get(treasure);
    if (integer == null) {
      integer = treasureQuantity;
    } else {
      integer += treasureQuantity;
    }
    this.treasure.put(treasure, integer);
  }
}
