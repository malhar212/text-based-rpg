package dungeon;

import java.util.Locale;

/**
 * Represents the commands available at a location.
 */
public enum Commands {
  MOVE("M"), PICKUP("P"), SHOOT("S"), QUIT("Q"),
  PICKARROWS("A"), PICKTREASURE("T");

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
    shortHand = shortHand.toUpperCase(Locale.ROOT);
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
      case "Q": {
        return QUIT;
      }
      case "T": {
        return PICKTREASURE;
      }
      case "A": {
        return PICKARROWS;
      }
      default: {
        throw new IllegalArgumentException("No value for this shorthand");
      }
    }
  }
}
