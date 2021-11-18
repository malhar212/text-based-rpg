package dungeon;

/**
 * Represents the direction of movement possible from a cave.
 */
public enum Move {
  NORTH("N","NORTH"),
  SOUTH("S","SOUTH"),
  EAST("E","EAST"),
  WEST("W","WEST");

  private final String shortForm;
  private final String fullForm;

  Move(String shortForm, String fullForm) {
    this.shortForm = shortForm;
    this.fullForm = fullForm;
  }

  /**
   * Provides the opposite move direction of the current move.
   * @return the opposite move direction of the current move.
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

  /**
   * Returns the shorthand representation for this move.
   *
   * @return the shorthand representation of this move.
   */
  public String getShortForm() {
    return shortForm;
  }

  /**
   * Returns the full name of this move value.
   *
   * @return the name representation of this move.
   */
  public String getFullForm() {
    return fullForm;
  }
}
