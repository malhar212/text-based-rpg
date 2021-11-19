package dungeon;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the package private Monster class.
 */
public class MonsterTest {

  private Monster urukhai;

  @Before
  public void setUp() {
    urukhai = new Otyugh();
  }

  @Test
  public void testGetHealth() {
    assertEquals(2, urukhai.getHealth());
  }

  @Test
  public void testArrowHit() {
    assertEquals(2, urukhai.getHealth());
    urukhai.arrowHit();
    assertEquals(1, urukhai.getHealth());
    urukhai.arrowHit();
    assertEquals(0, urukhai.getHealth());
  }

  @Test
  public void isInjured() {
    assertEquals(2, urukhai.getHealth());
    assertFalse(urukhai.isInjured());
    urukhai.arrowHit();
    assertTrue(urukhai.isInjured());
    assertEquals(1, urukhai.getHealth());
    urukhai.arrowHit();
    assertEquals(0, urukhai.getHealth());
    assertTrue(urukhai.isInjured());
  }

  @Test
  public void isDead() {
    assertEquals(2, urukhai.getHealth());
    assertFalse(urukhai.isDead());
    urukhai.arrowHit();
    assertFalse(urukhai.isDead());
    assertEquals(1, urukhai.getHealth());
    urukhai.arrowHit();
    assertEquals(0, urukhai.getHealth());
    assertTrue(urukhai.isDead());
  }
}