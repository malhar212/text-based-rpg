package randomizer;

import java.util.Random;

/**
 * Represents an instance of the GameRandomizer which is used to generate random integer values.
 */
public final class GameRandomizer implements Randomizer {

  private final Random random;
  private int[] randomSequence;
  private int count;

  /**
   * Creates a new instance of GameRandomizer.
   * If a randomizer sequence is passed as the argument then the
   * returned values of getRandomValue are from that sequence in the specified order if sequence
   * is exhausted it defaults to true random values.
   *
   * @param randomizerSequence The sequence of numbers to be returned.
   */
  public GameRandomizer(int... randomizerSequence) {
    if (randomizerSequence.length == 0) {
      randomSequence = new int[0];
    } else {
      randomSequence = randomizerSequence;
    }
    random = new Random();
  }

  /**
   * Returns a random integer value in the given range or
   * from the sequence of the randomizer sequence.
   * If a randomizer sequence is provided the result of
   * getRandomValue will be in order of the given sequence.
   * If sequence is exhausted it defaults to true random values.
   *
   * @param minBound the minimum integer value of range.(inclusive)
   * @param maxBound the maximum integer value of range.(inclusive)
   * @return a random integer value in the given range.
   */
  @Override
  public int getRandomValue(int minBound, int maxBound) {
    if (randomSequence.length == 0) {
      int i = random.nextInt(maxBound + 1 - minBound) + minBound;
      return i;
    } else {
      if (count >= randomSequence.length) {
        int i = random.nextInt(maxBound + 1 - minBound) + minBound;
        randomSequence = new int[]{};
        return i;
      }
      int nextInt = randomSequence[count];
      count++;
      return nextInt;
    }
  }
}

