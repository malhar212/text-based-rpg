package dungeon;

/**
 * Represents the smell level sensed at a location.
 */
public enum SmellLevel {
  NONE, LESS, MORE;

  /**
   * Gets the SmellLevel corresponding to the provided argument.
   * @param arg the integer argument to get the SmellLevel.
   * @return NONE if arg is 0, LESS if arg is 1, MORE if arg is 2.
   * @throws IllegalStateException if an invalid arg is passed.
   */
  public static SmellLevel getSmellLevel(int arg) throws IllegalStateException {
    switch (arg) {
      case 0: {
        return NONE;
      }
      case 1: {
        return LESS;
      }
      case 2: {
        return MORE;
      }
      default: {
        throw new IllegalStateException("Smell cannot be less than 0 or more than 2");
      }
    }
  }
}
