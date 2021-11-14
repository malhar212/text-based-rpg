package dungeon;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents an instance of {@link Location} in the dungeon maze.
 * {@link Location} can be a cave/tunnel. If Location had 2 next moves then it's a tunnel,
 * otherwise it
 * is a cave.
 */
public final class LocationNode implements LocationPrivate {

  private final Set<Move> nextMoves;
  private final Map<Treasure, Integer> treasure;
  private final int row;
  private final int column;

  /**
   * Creates an instance of LocationNode with the given row and column value.
   *
   * @param row    the row position of this location node in maze grid.
   * @param column the column position of this location node in maze grid.
   */
  public LocationNode(int row, int column) {
    this.row = row;
    this.column = column;
    this.nextMoves = new TreeSet<>();
    this.treasure = new HashMap<>();
  }

  /**
   * Add the provided move to the list of possible next moves from this LocationNode.
   *
   * @param move {@link Move} to be added.
   * @throws IllegalArgumentException if the provided move is null.
   */
  //package-private due to interface
  @Override
  public void setNextMove(Move move) throws IllegalArgumentException {
    if (move == null) {
      throw new IllegalArgumentException("Move cannot be null");
    }
    nextMoves.add(move);
  }

  /**
   * Adds the provided treasure and quantity to the treasure in the cave.
   *
   * @param treasure         treasure type to be added.
   * @param treasureQuantity treasure quantity to be added.
   * @throws IllegalArgumentException if provided treasure type is null.
   * @throws IllegalArgumentException if provided treasure quantity is negative.
   */
  //package-private due to interface
  @Override
  public void setTreasure(Treasure treasure, int treasureQuantity) throws IllegalArgumentException {
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

  /**
   * Picks the provided quantity of treasure from the treasure in the location.
   *
   * @param treasure         treasure type to be picked.
   * @param treasureQuantity treasure quantity to be added.
   * @throws IllegalArgumentException if provided treasure type is null.
   * @throws IllegalArgumentException if no such treasure or treasure quantity insufficient in
   *                                  location or treasure to be picked is negative.
   */
  //package-private due to interface
  @Override
  public void pickTreasure(Treasure treasure, int treasureQuantity)
          throws IllegalArgumentException {
    if (treasure == null) {
      throw new IllegalArgumentException("Treasure cannot be null");
    }
    Integer integer = this.treasure.get(treasure);
    if (integer == null || treasureQuantity < 0 || integer - treasureQuantity < 0) {
      throw new IllegalArgumentException(
              "No such treasure or treasure quantity insufficient or treasure to be picked is "
                      + "negative");
    } else {
      integer -= treasureQuantity;
    }
    if (integer == 0) {
      this.treasure.remove(treasure);
    } else {
      this.treasure.put(treasure, integer);
    }
  }

  /**
   * Returns the possible next moves from this LocationNode.
   *
   * @return a set of {@link Move}s
   */
  @Override
  public Set<Move> getNextMoves() {
    return new TreeSet<>(nextMoves);
  }

  /**
   * Returns the treasure in this LocationNode.
   *
   * @return a map of {@link Treasure} and its quantity.
   */
  @Override
  public Map<Treasure, Integer> getTreasure() {
    return new HashMap<>(treasure);
  }

  /**
   * Returns the row position of this location node in maze grid.
   *
   * @return row number as integer value.
   */
  @Override
  public int getRow() {
    return row;
  }

  /**
   * Returns the column position of this location node in maze grid.
   *
   * @return column number as integer value.
   */
  @Override
  public int getColumn() {
    return column;
  }

  /**
   * Checks whether this LocationNode has treasure.
   *
   * @return true if treasure is present in the location.
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
}
