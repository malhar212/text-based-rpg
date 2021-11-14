package randomizer;

/**
 * Randomizer provides method to generate random integer values.
 */
public interface Randomizer {

  /**
   * Returns a random integer value in the given range or
   *        from the sequence of the randomizer sequence.
   * If a randomizer sequence is provided the result of
   *      getRandomValue will be in order of the given sequence.
   * @param minBound the minimum integer value of range.(inclusive)
   * @param maxBound the maximum integer value of range.(inclusive)
   * @return a random integer value in the given range.
   */
  int getRandomValue(int minBound, int maxBound);
}
