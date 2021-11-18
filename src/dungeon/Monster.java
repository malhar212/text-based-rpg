package dungeon;

//package-private

/*
 * Represents a monster in the cave.
 */
interface Monster {

  int getHealth();

  void arrowHit();

  boolean isInjured();

  boolean isDead();

}
