package dungeon;
//package-private

/*
 * Represents a monster
 */
class UrukHai implements Monster {

  private int health;
  private static final int DEFAULT_HEALTH = 2;

  public UrukHai() {
    this.health = DEFAULT_HEALTH;
  }

  @Override
  public int getHealth() {
    return health;
  }

  @Override
  public void arrowHit() {
    health--;
  }

  @Override
  public boolean isInjured() {
    return health != 2;
  }

  @Override
  public boolean isDead() {
    return health == 0;
  }
}
