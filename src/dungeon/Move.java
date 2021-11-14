package dungeon;

/**
 * Represents the direction of movement possible from a cave.
 */
public enum Move {
  NORTH, SOUTH, EAST, WEST;

  /**
   * Provides the opposite move direction of the current move.
   * @return he opposite move direction of the current move.
   */
  public Move getOpposite() {
    switch (this) {
      case NORTH:
        return SOUTH;
      case SOUTH:
        return NORTH;
      case EAST:
        return WEST;
      case WEST:
        return EAST;
      default:
        throw new IllegalStateException("Error getting opposite of Move"); //Case never happens
    }
  }
}
