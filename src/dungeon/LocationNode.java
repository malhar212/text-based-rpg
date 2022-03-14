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
  private Monster monster;
  private int arrows;

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
    this.monster = null;
    this.arrows = 0;
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

  /**
   * Checks whether the current location has a Monster.
   *
   * @return true if a monster is present at this location.
   */
  @Override
  public boolean hasMonster() {
    if (monster != null) {
      return !monster.isDead();
    }
    return false;
  }

  /**
   * Returns the monster at this location.
   *
   * @return the monster at this location.
   */
  //package-private due to interface
  @Override
  public Monster getMonster() {
    return monster;
  }

  /**
   * Returns whether the location is a cave or tunnel.
   *
   * @return true if it is a cave, false if it is a tunnel.
   */
  @Override
  public boolean isCave() {
    return nextMoves.size() != 2;
  }

  /**
   * Checks whether this LocationNode has arrows.
   *
   * @return true if there are arrows at this location.
   */
  @Override
  public boolean hasArrows() {
    return arrows > 0;
  }

  /**
   * Returns the number of arrows at this location.
   *
   * @return number of arrows at this location.
   */
  @Override
  public int getArrows() {
    return arrows;
  }

  //package-private due to interface.
  @Override
  public void hitMonster() {
    if (monster != null) {
      monster.arrowHit();
    }
  }

  //package-private due to interface
  @Override
  public void setMonster() {
    if (monster == null) {
      monster = new Otyugh();
    }
  }

  //package-private due to interface
  @Override
  public int pickArrows() throws IllegalStateException {
    if (hasArrows()) {
      int arrows = this.arrows;
      this.arrows = 0;
      return arrows;
    }
    throw new IllegalStateException("There are no arrows to pick");
  }

  //package-private due to interface
  @Override
  public void setArrows(int numberOfArrows) {
    arrows = numberOfArrows;
  }
}
