import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import dungeon.Player;
import dungeon.PlayerModel;
import org.junit.Before;
import org.junit.Test;


/**
 * Tests the Player class and it's methods.
 */
public class PlayerTest {

  private Player player;

  @Before
  public void setUp() throws Exception {
    player = new PlayerModel();
  }

  @Test
  public void getTreasure() {
    int expected = 0;
    assertEquals(expected,player.getTreasure().size());
  }

  @Test
  public void hasTreasure() {
    assertFalse(player.hasTreasure());
  }
}