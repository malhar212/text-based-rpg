package dungeon;

/**
 * Represents the commands available at a location.
 */
public enum Commands {
  MOVE("M"), PICKUP("P"), SHOOT("S");

  private final String shortHand;

  Commands(String shortHand) {
    this.shortHand = shortHand;
  }

  /**
   * Returns the shorthand representation of this command.
   *
   * @return shorthand representation of this command.
   */
  public String getShortHand() {
    return this.shortHand;
  }

  /**
   * Returns the command for the provided shorthand.
   *
   * @param shortHand the shorthand whose command needs to be retrieved.
   * @return the Commands for the provided shorthand representation
   * @throws IllegalArgumentException if the provided shorthand is invalid.
   */
  public static Commands getByShortHand(String shortHand) throws IllegalArgumentException {
    switch (shortHand) {
      case "M": {
        return MOVE;
      }
      case "P": {
        return PICKUP;
      }
      case "S": {
        return SHOOT;
      }
      default: {
        throw new IllegalArgumentException("No value for this shorthand");
      }
    }
  }
}
