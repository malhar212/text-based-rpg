import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import dungeon.Dungeon;
import dungeon.DungeonConsoleController;
import dungeon.DungeonController;
import dungeon.DungeonModel;
import dungeon.Location;
import dungeon.Move;
import dungeon.Player;
import dungeon.Treasure;
import randomizer.GameRandomizer;
import randomizer.Randomizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test the Dungeon controller.
 */
public class DungeonControllerTest {

  private Dungeon dungeonMonster;

  private static Location getNextLocation(Move move, Dungeon dungeon)
          throws IllegalArgumentException, IllegalStateException {
    int currentX = dungeon.getPlayerCurrentLocation().getRow();
    int currentY = dungeon.getPlayerCurrentLocation().getColumn();
    List<List<Location>> maze = dungeon.getMaze();
    List<Integer> coordinates = new ArrayList<>();
    switch (move) {
      case NORTH: {
        int x = currentX - 1;
        if (x < 0) {
          x = maze.size() - 1;
        }
        coordinates.add(x);
        coordinates.add(currentY);
        break;
      }
      case SOUTH: {
        int x = currentX + 1;
        if (x == maze.size()) {
          x = 0;
        }
        coordinates.add(x);
        coordinates.add(currentY);
        break;
      }
      case WEST: {
        int y = currentY + 1;
        if (y == maze.get(currentX).size()) {
          y = 0;
        }
        coordinates.add(currentX);
        coordinates.add(y);
        break;
      }
      case EAST: {
        int y = currentY - 1;
        if (y < 0) {
          y = maze.get(currentX).size() - 1;
        }
        coordinates.add(currentX);
        coordinates.add(y);
        break;
      }
      default: {
        throw new IllegalStateException("getNextLocation should never be in default condition");
      }
    }
    return maze.get(coordinates.get(0)).get(coordinates.get(1));
  }

  @Before
  public void setUp() {
    Randomizer monsterRandomizer = new GameRandomizer(35, 33, 6, 6, 31, 4, 0, 32, 28, 12, 14, 7,
            14, 20, 4, 23, 11, 12, 11, 2, 10, 4, 4, 0, 2, 3, 1, 3, 9, 6, 7, 1, 4, 5, 2, 2, 1, 2, 1,
            0, 16, 1, 2, 4, 2, 1, 1, 4, 3, 2, 2, 5, 0, 1, 1, 1, 2, 1, 6, 2, 0, 5, 0, 1, 1, 2, 2, 3,
            1, 3, 0, 2, 2, 1, 0, 4, 5, 2, 1, 5, 0, 3, 11, 2, 0, 3, 13, 3, 6, 1, 0, 2, 11, 2, 12, 2,
            11, 1, 8, 3, 9, 2, 0, 2, 0, 7, 0);
    dungeonMonster = new DungeonModel(
            5, 4, true, 4, 50, 3,
            monsterRandomizer);
  }

  @Test
  public void testMoveEast() {
    StringReader input = new StringReader("M E q");
    StringBuilder gameLog = new StringBuilder();
    Location nextLocation = getNextLocation(Move.EAST, dungeonMonster);
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    Location playerCurrentLocation = dungeonMonster.getPlayerCurrentLocation();
    assertFalse(nextLocation.getRow() == playerCurrentLocation.getRow()
            && nextLocation.getColumn() == playerCurrentLocation.getColumn());
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    playerCurrentLocation = dungeonMonster.getPlayerCurrentLocation();
    assertEquals(nextLocation.getRow(), playerCurrentLocation.getRow());
    assertEquals(nextLocation.getColumn(), playerCurrentLocation.getColumn());
    assertTrue(gameLog.toString().contains("Where do we go?\n\nYou are in a cave,"));
    assertTrue(gameLog.toString().contains("very strong rancid smell"));
  }

  @Test
  public void testMoveSouth() {
    StringReader input = new StringReader("M N M S q");
    StringBuilder gameLog = new StringBuilder();
    Location expectedLocation = dungeonMonster.getPlayerCurrentLocation();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    Location playerCurrentLocation = dungeonMonster.getPlayerCurrentLocation();
    assertEquals(expectedLocation.getRow(), playerCurrentLocation.getRow());
    assertEquals(expectedLocation.getColumn(), playerCurrentLocation.getColumn());
    System.out.println(gameLog);
    assertTrue(gameLog.toString().contains("Where do we go?"));
  }

  @Test
  public void testMoveNorth() {
    StringReader input = new StringReader("M N q");
    StringBuilder gameLog = new StringBuilder();
    Location nextLocation = getNextLocation(Move.NORTH, dungeonMonster);
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    Location playerCurrentLocation = dungeonMonster.getPlayerCurrentLocation();
    assertFalse(nextLocation.getRow() == playerCurrentLocation.getRow()
            && nextLocation.getColumn() == playerCurrentLocation.getColumn());
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    playerCurrentLocation = dungeonMonster.getPlayerCurrentLocation();
    assertEquals(nextLocation.getRow(), playerCurrentLocation.getRow());
    assertEquals(nextLocation.getColumn(), playerCurrentLocation.getColumn());
    System.out.println(gameLog);
    assertTrue(gameLog.toString().contains("Where do we go?"));
  }

  @Test
  public void testMoveWest() {
    StringReader input = new StringReader("M W q");
    StringBuilder gameLog = new StringBuilder();
    Location nextLocation = getNextLocation(Move.WEST, dungeonMonster);
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    Location playerCurrentLocation = dungeonMonster.getPlayerCurrentLocation();
    assertFalse(nextLocation.getRow() == playerCurrentLocation.getRow()
            && nextLocation.getColumn() == playerCurrentLocation.getColumn());
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    playerCurrentLocation = dungeonMonster.getPlayerCurrentLocation();
    assertEquals(nextLocation.getRow(), playerCurrentLocation.getRow());
    assertEquals(nextLocation.getColumn(), playerCurrentLocation.getColumn());
    System.out.println(gameLog);
    assertTrue(gameLog.toString().contains("Where do we go?"));
  }

  @Test
  public void testShootMiss() {
    StringReader input = new StringReader("S E 1 q");
    StringBuilder gameLog = new StringBuilder();
    Location nextLocation = getNextLocation(Move.EAST, dungeonMonster);
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    Location playerCurrentLocation = dungeonMonster.getPlayerCurrentLocation();
    assertFalse(nextLocation.getRow() == playerCurrentLocation.getRow()
            && nextLocation.getColumn() == playerCurrentLocation.getColumn());
    assertFalse(nextLocation.hasMonster());
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    assertTrue(gameLog.toString().contains("\nYour arrow goes whistling through the dungeon "
            + "and there's a clunk "
            + "as it falls to the ground after hitting a cave wall"));
  }

  @Test
  public void testShootHitInjureAndKill() {
    StringReader input = new StringReader("M E S S 1 S S 1 q");
    StringBuilder gameLog = new StringBuilder();
    Location nextLocation = getNextLocation(Move.EAST, dungeonMonster);
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    Location playerCurrentLocation = dungeonMonster.getPlayerCurrentLocation();
    assertFalse(nextLocation.getRow() == playerCurrentLocation.getRow()
            && nextLocation.getColumn() == playerCurrentLocation.getColumn());
    assertFalse(nextLocation.hasMonster());
    nextLocation = dungeonMonster.getMaze().get(0).get(0);
    assertTrue(nextLocation.hasMonster());
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    assertTrue(gameLog.toString().contains("What do you want to do? MOVE: M SHOOT: S PICKUP: P "
            + "QUIT: Q\n"
            + "\n"
            + "Where do you want to shoot?\n"
            + "\n"
            + "How far do you want to shoot? (1-5)\n"
            + "\n"
            + "You hear a painful roar in the distance. It seems your arrow hit an Otyugh\n"
            + "You have 2 arrows left\n"
            + "What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q\n"
            + "\n"
            + "Where do you want to shoot?\n"
            + "\n"
            + "How far do you want to shoot? (1-5)\n"
            + "\n"
            + "You hear a painful roar and wild thrashing in the darkness and then silence. It "
            + "seems you've killed an Otyugh\n"
            + "You have 1 arrow left\n"
            + "What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q"));
  }

  @Test
  public void testGameEndWin() {
    StringReader input = new StringReader("P A M E S S 1 S S 1 M S M S M W S S 1 S S 1 M S M W");
    StringBuilder gameLog = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    System.out.println(gameLog);
    assertTrue(gameLog.toString().contains("You have escaped the mines of Moria"));
  }

  @Test
  public void testChanceSurviveWithInjuredMonster() {
    StringReader input = new StringReader("P A M E S S 1 M S M S M W S S 1 S S 1 M S M W");
    StringBuilder gameLog = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    System.out.println(gameLog);
    assertTrue(gameLog.toString().contains("There is an injured Otyugh resting. You have "
            + "miraculously survived!!"));
  }

  @Test
  public void testKilledByMonster() {
    StringReader input = new StringReader("P A M E M S M S M W S S 1 S S 1 M S M W");
    StringBuilder gameLog = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    System.out.println(gameLog);
    assertTrue(gameLog.toString().contains("You were killed. You died a gruesome death at "
            + "the hands of the Otyugh"));
  }

  @Test
  public void arrowsOverError() {
    StringReader input = new StringReader("M E S S 1 S S 1 S S 1 S S 1 q");
    StringBuilder gameLog = new StringBuilder();
    Location nextLocation = getNextLocation(Move.EAST, dungeonMonster);
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    assertTrue(gameLog.toString().contains("Player has no arrows\n"));
  }

  @Test
  public void arrowsPickUpSuccessful() {
    StringReader input = new StringReader("P A q");
    StringBuilder gameLog = new StringBuilder();
    Location playerCurrentLocation = dungeonMonster.getPlayerCurrentLocation();
    boolean expectedValue = playerCurrentLocation.hasArrows();
    int locationArrows = playerCurrentLocation.getArrows();
    int expectedArrows = locationArrows + dungeonMonster.getPlayerDescription().getArrows();
    assertTrue(expectedValue);
    assertTrue(expectedArrows > 0);
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    Player playerNewDescription = dungeonMonster.getPlayerDescription();
    //Location no longer has arrows after picking
    assertFalse(playerCurrentLocation.hasArrows());
    //player has treasure and same as arrows that was in location
    assertTrue(playerNewDescription.hasArrows());
    assertEquals(expectedArrows, playerNewDescription.getArrows());
    System.out.println(gameLog);
    assertTrue(gameLog.toString().contains("What to pick? Enter A for arrows or T for treasure"));
    assertTrue(gameLog.toString().contains(new StringBuilder("You have picked up ")
            .append(locationArrows).append(" arrows")));
    assertTrue(gameLog.toString().contains(new StringBuilder("You have ")
            .append(expectedArrows).append(" arrows left")));
  }

  @Test
  public void arrowsPickUpWhenNothingAtLocation() {
    StringReader input = new StringReader("P A P A q");
    StringBuilder gameLog = new StringBuilder();
    Location playerCurrentLocation = dungeonMonster.getPlayerCurrentLocation();
    boolean expectedValue = playerCurrentLocation.hasArrows();
    int locationArrows = playerCurrentLocation.getArrows();
    int expectedArrows = locationArrows + dungeonMonster.getPlayerDescription().getArrows();
    assertTrue(expectedValue);
    assertTrue(expectedArrows > 0);
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    Player playerNewDescription = dungeonMonster.getPlayerDescription();
    //Location no longer has arrows after picking
    assertFalse(playerCurrentLocation.hasArrows());
    //player has treasure and same as arrows that was in location
    assertTrue(playerNewDescription.hasArrows());
    assertEquals(expectedArrows, playerNewDescription.getArrows());
    System.out.println(gameLog);
    assertTrue(gameLog.toString().contains("No arrows to pick\n"));
  }

  @Test
  public void testTreasurePicking() {
    Player playerDescription = dungeonMonster.getPlayerDescription();
    assertFalse(playerDescription.hasTreasure());
    assertEquals(0, playerDescription.getTreasure().size());
    Location playerNewLocationWithTreasure = dungeonMonster.getPlayerCurrentLocation();
    boolean expectedValue = playerNewLocationWithTreasure.hasTreasure();
    Map<Treasure, Integer> caveTreasure = playerNewLocationWithTreasure.getTreasure();
    assertTrue(expectedValue);
    assertTrue(caveTreasure.size() > 0);
    StringReader input = new StringReader("P T P T q");
    StringBuilder gameLog = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    System.out.println(gameLog);
    Player playerNewDescription = dungeonMonster.getPlayerDescription();
    //Location no longer has treasure after picking
    assertFalse(playerNewLocationWithTreasure.hasTreasure());
    //player has treasure and same as treasure that was in cave
    assertTrue(playerNewDescription.hasTreasure());
    assertEquals(caveTreasure.size(), playerNewDescription.getTreasure().size());
    assertEquals(caveTreasure, playerNewDescription.getTreasure());
    //When there's nothing to pick
    assertTrue(gameLog.toString().contains("No treasure to pick"));
  }

  @Test
  public void testInvalidCommand() {
    StringReader input = new StringReader("D q");
    StringBuilder gameLog = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    assertTrue(gameLog.toString().contains("Please choose one of the valid commands MOVE: M "
            + "SHOOT: S PICKUP: P QUIT: Q\n"));
  }

  @Test
  public void testInvalidMoveDirectionForLocation() {
    StringReader input = new StringReader("M S q");
    StringBuilder gameLog = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    System.out.println(gameLog);
    assertTrue(gameLog.toString().contains("Provided move is not a valid move SOUTH"));
  }

  @Test
  public void testInvalidMoveDirection() {
    StringReader input = new StringReader("M F W q");
    StringBuilder gameLog = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    assertTrue(gameLog.toString().contains("Please enter a valid direction. You can input N for "
            + "North, E for East, S for South, W for West"));
  }

  @Test
  public void testInvalidShootDirectionForLocation() {
    StringReader input = new StringReader("S S N 1 q");
    StringBuilder gameLog = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    System.out.println(gameLog);
    assertTrue(gameLog.toString().contains("Provided direction is not a valid direction for "
            + "current location"));
  }

  @Test
  public void testInvalidShootDirection() {
    StringReader input = new StringReader("S F W 1 q");
    StringBuilder gameLog = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    assertTrue(gameLog.toString().contains("Please enter a valid direction. You can input N for "
            + "North, E for East, S for South, W for West"));
  }

  @Test
  public void testInvalidShootDistance() {
    StringReader input = new StringReader("S W 7 1 q");
    StringBuilder gameLog = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    assertTrue(gameLog.toString().contains("Distance cannot be less than 1 or greater than 5"));

    input = new StringReader("S W -1 2 q");
    gameLog = new StringBuilder();
    controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    assertTrue(gameLog.toString().contains("Distance cannot be less than 1 or greater than 5"));
  }

  @Test
  public void testInvalidPickupOption() {
    StringReader input = new StringReader("P X P A q");
    StringBuilder gameLog = new StringBuilder();
    Location playerCurrentLocation = dungeonMonster.getPlayerCurrentLocation();
    boolean expectedValue = playerCurrentLocation.hasArrows();
    int locationArrows = playerCurrentLocation.getArrows();
    int expectedArrows = locationArrows + dungeonMonster.getPlayerDescription().getArrows();
    assertTrue(expectedValue);
    assertTrue(expectedArrows > 0);
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    try {
      controller.play(dungeonMonster);
    } catch (IOException ioException) {
      assertEquals("Shouldn't throw error", ioException.getMessage());
    }
    Player playerNewDescription = dungeonMonster.getPlayerDescription();
    //Location no longer has arrows after picking
    assertFalse(playerCurrentLocation.hasArrows());
    //player has treasure and same as arrows that was in location
    assertTrue(playerNewDescription.hasArrows());
    assertEquals(expectedArrows, playerNewDescription.getArrows());
    assertTrue(gameLog.toString().contains("Please pick a valid pickup option"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInputSourceException() {
    Appendable gameLog = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(null, gameLog);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidOutputAppendableException() {
    StringReader input = new StringReader("M E q");
    DungeonController controller = new DungeonConsoleController(input,null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidModelException() throws IOException {
    StringReader input = new StringReader("M E q");
    Appendable gameLog = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    controller.play(null);
  }

  @Test(expected = IOException.class)
  public void testOutputException() throws IOException {
    StringReader input = new StringReader("S W 7 1 q");
    Appendable gameLog = new FakeAppendable();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    controller.play(dungeonMonster);
  }

  @Test(expected = NoSuchElementException.class)
  public void testInputException() throws IOException {
    StringReader input = new StringReader("S W 7 1");
    Appendable gameLog = new StringBuilder();
    DungeonController controller = new DungeonConsoleController(input, gameLog);
    controller.play(dungeonMonster);
  }

}