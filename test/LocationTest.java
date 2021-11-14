import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.Set;
import dungeon.Location;
import dungeon.LocationNode;
import dungeon.Move;
import dungeon.Treasure;

/**
 * Tests the Location class and it's methods.
 */
public class LocationTest {
  private Location location;

  @Before
  public void setUp() {
    location = new LocationNode(1,0);
  }

  @Test
  public void testGetNextMoves() {
    Set<Move> nextMoves = location.getNextMoves();
    assertEquals(0, nextMoves.size());
  }

  @Test
  public void getTreasure() {
    Map<Treasure, Integer> treasure = location.getTreasure();
    assertEquals(0, treasure.size());
  }

  @Test
  public void getRow() {
    int expected = 1;
    int row = location.getRow();
    assertEquals(expected, row);
  }

  @Test
  public void getColumn() {
    int expected = 0;
    int column = location.getColumn();
    assertEquals(expected, column);
  }

  @Test
  public void hasTreasure() {
    assertFalse(location.hasTreasure());
  }
}