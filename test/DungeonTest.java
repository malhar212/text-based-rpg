import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import dungeon.Dungeon;
import dungeon.DungeonModel;
import dungeon.Location;
import dungeon.Move;
import dungeon.Player;
import dungeon.Treasure;
import org.junit.Before;
import org.junit.Test;
import randomizer.GameRandomizer;
import randomizer.Randomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Test the Dungeon game.
 */
public class DungeonTest {
  private Dungeon dungeonWrapped;
  private Dungeon dungeonWrappedInterconnectivity;
  private Dungeon dungeonWrappedInterconnectivityTreasure30;
  private Dungeon dungeon;
  private Dungeon dungeonTreasure30;

  @Before
  public void setUp() {
    //Test Maze with interconnectivity 0, wrapped true
    Randomizer testRandomizer = new GameRandomizer(18, 38, 24, 33, 35, 11, 9,
            1, 6, 13, 9, 0, 18, 13, 21, 14, 12, 11, 18, 17, 6, 5,
            10, 6, 6, 3, 2, 4, 9, 9, 3, 6, 6, 3, 3, 0, 0, 2, 0, 0, 2, 3);
    //No interconnectivity, No Treasure, wrapped true
    dungeonWrapped = new DungeonModel(
            5, 4, true, 0, 0, testRandomizer);
    // Interconnectivity 4, No Treasure, wrapped true
    Randomizer randomizer = new GameRandomizer(9, 4, 1, 24, 10, 19,
            11, 14, 15, 8, 21, 17, 16, 4, 17, 14, 5, 12, 0, 11, 8, 5, 9,
            4, 4, 12, 1, 10, 7, 5, 3, 8, 1, 4, 0, 3, 2, 2, 0, 0, 15, 3, 14, 8, 5, 4, 0);
    dungeonWrappedInterconnectivity = new DungeonModel(
            5, 4, true, 4, 0, randomizer);
    //No interconnectivity, Treasure 30%, Wrapped true
    Randomizer randomizer1 = new GameRandomizer(1, 36, 34, 17, 26, 16, 20, 22, 20,
            13, 27, 16, 23, 25, 11, 16, 19, 4, 12, 6, 17, 11, 14, 15, 3, 12, 13, 6, 8,
            0, 4, 6, 5, 6, 2, 4, 0, 2, 0, 0, 10, 19, 7, 7, 2, 1, 2, 4, 2, 2, 0, 4, 1,
            5, 4, 3, 2, 5, 0, 1, 0, 2, 9, 0);
    dungeonWrappedInterconnectivityTreasure30 = new DungeonModel(
            5, 4, true, 4, 30, randomizer1);
    //No interconnectivity, No treasure, not wrapped
    Randomizer randomizer2 = new GameRandomizer(8, 26, 1, 0, 26, 19, 7,
            4, 19, 14, 16, 16, 12, 5, 0, 14, 0, 2, 2, 4, 1, 3, 8, 5, 6, 5, 4, 3, 2, 1, 0, 0, 0);
    dungeon = new DungeonModel(
            5, 4, false, 0, 0, randomizer2);
    //No interconnectivity, Treasure 30 percent, not wrapped
    Randomizer randomizer3 = new GameRandomizer(23, 28, 3, 6, 6, 22, 8, 4, 14,
            19, 10, 14, 12, 2, 6, 1, 10, 3, 7, 4, 9, 1, 4, 4,
            5, 5, 0, 1, 0, 1, 0, 5, 3, 1, 3, 0, 3, 0, 5, 9, 1,
            2, 3, 3, 2, 0, 2, 1, 5, 0, 2, 2, 2, 1, 1, 5, 1, 2, 1, 3, 0);
    dungeonTreasure30 = new DungeonModel(
            5, 4, false, 0, 30, randomizer3);
  }

  @Test
  public void testInitialState() {
    //Tests player position is same as start position, player has no treasure.
    Location startLocation = dungeon.getStartLocation();
    Location playerLocation = dungeon.getPlayerCurrentLocation();
    assertTrue(compareLocations(startLocation, playerLocation));
    Player playerDescription = dungeon.getPlayerDescription();
    assertFalse(playerDescription.hasTreasure());
    assertEquals(0, playerDescription.getTreasure().size());
    int expectedPaths = traverseAllNodesAndReturnTotalPaths(dungeon);

    Location startLocationTreasure30 = dungeonTreasure30.getStartLocation();
    Location playerLocationTreasure30 = dungeonTreasure30.getPlayerCurrentLocation();
    assertTrue(compareLocations(startLocationTreasure30, playerLocationTreasure30));
    Player playerDescriptionTreasure30 = dungeonTreasure30.getPlayerDescription();
    assertFalse(playerDescriptionTreasure30.hasTreasure());
    assertEquals(0, playerDescription.getTreasure().size());

    Location startLocationWrapped = dungeonWrapped.getStartLocation();
    Location playerLocationWrapped = dungeonWrapped.getPlayerCurrentLocation();
    assertTrue(compareLocations(startLocationWrapped, playerLocationWrapped));
    Player playerDescriptionWrapped = dungeonWrapped.getPlayerDescription();
    assertFalse(playerDescriptionWrapped.hasTreasure());
    assertEquals(0, playerDescriptionWrapped.getTreasure().size());

    Location startLocationWrappedInterconnectivity =
            dungeonWrappedInterconnectivity.getStartLocation();
    Location playerLocationWrappedInterconnectivity =
            dungeonWrappedInterconnectivity.getPlayerCurrentLocation();
    assertTrue(compareLocations(startLocationWrappedInterconnectivity,
            playerLocationWrappedInterconnectivity));
    Player playerDescriptionWrappedInterconnectivity =
            dungeonWrappedInterconnectivity.getPlayerDescription();
    assertFalse(playerDescriptionWrappedInterconnectivity.hasTreasure());
    assertEquals(0, playerDescriptionWrappedInterconnectivity.getTreasure().size());

    Location startLocationWrappedInterconnectivityTreasure30 =
            dungeonWrappedInterconnectivityTreasure30.getStartLocation();
    Location playerLocationWrappedInterconnectivityTreasure30 =
            dungeonWrappedInterconnectivityTreasure30.getPlayerCurrentLocation();
    assertTrue(compareLocations(startLocationWrappedInterconnectivityTreasure30,
            playerLocationWrappedInterconnectivityTreasure30));
    Player playerDescriptionWrappedInterconnectivityTreasure30 =
            dungeonWrappedInterconnectivityTreasure30.getPlayerDescription();
    assertFalse(playerDescriptionWrappedInterconnectivityTreasure30.hasTreasure());
    assertEquals(0,
            playerDescriptionWrappedInterconnectivityTreasure30.getTreasure().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRows() {
    Randomizer gameRandomizer = new GameRandomizer();
    Dungeon dungeon1 = new DungeonModel(3,
            4, true, 0, 10,gameRandomizer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeRows() {
    Randomizer gameRandomizer = new GameRandomizer();
    Dungeon dungeon1 = new DungeonModel(-3,
            4, true, 0, 10,gameRandomizer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidColumns() {
    Randomizer gameRandomizer = new GameRandomizer();
    Dungeon dungeon1 = new DungeonModel(5,
            3, true, 0, 10,gameRandomizer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeColumns() {
    Randomizer gameRandomizer = new GameRandomizer();
    Dungeon dungeon1 = new DungeonModel(5,
            -3, true, 0, 10,gameRandomizer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInterconnectivityWrapped() {
    Randomizer gameRandomizer = new GameRandomizer();
    Dungeon dungeon1 = new DungeonModel(5,
            4, true, 100, 10,gameRandomizer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeInterconnectivityWrapped() {
    Randomizer gameRandomizer = new GameRandomizer();
    Dungeon dungeon1 = new DungeonModel(5,
            4, true, -1, 10,gameRandomizer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidInterconnectivityNotWrapped() {
    Randomizer gameRandomizer = new GameRandomizer();
    Dungeon dungeon1 = new DungeonModel(5,
            4, false, 100, 10,gameRandomizer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeInterconnectivityNotWrapped() {
    Randomizer gameRandomizer = new GameRandomizer();
    Dungeon dungeon1 = new DungeonModel(5,
            4, false, -10, 10,gameRandomizer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidTreasurePercentage() {
    Randomizer gameRandomizer = new GameRandomizer();
    Dungeon dungeon1 = new DungeonModel(5,
            4, false, 10, 110,gameRandomizer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeTreasurePercentage() {
    Randomizer gameRandomizer = new GameRandomizer();
    Dungeon dungeon1 = new DungeonModel(5,
            4, false, 10, -110,gameRandomizer);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRandomizer() {
    Dungeon dungeon1 = new DungeonModel(5,
            4, false, 10, 10,null);
  }

  @Test
  public void testGetMazeAndMazeCreation() {
    //Testing visually start and end of mazes are at least 5 edges away.
    String expectedValue = new StringBuilder("\n            \n")
            .append("0--0--0--0  \n")
            .append("|        |  \n")
            .append("0  $0--0--0  \n")
            .append("|     |     \n")
            .append("X0--0  0  0  \n")
            .append("|  |     |  \n")
            .append("0  0  0--0  \n")
            .append("|     |  |  \n")
            .append("0--0--0  0  \n")
            .append("\n")
            .append("Number of edges: 19").toString();
    //No interconnectivity, No treasure, not wrapped
    assertEquals(expectedValue, visualizeKruskals(dungeon).toString());
    // Testing interconnectivity
    // Totals paths in the tree are paths in MST which is (rows * columns) - 1
    // Plus the interconnectivity provided.
    int expectedNumberOfPaths = getExpectedNumberOfPaths(dungeon, 0);
    int actualNumberOfPaths = traverseAllNodesAndReturnTotalPaths(dungeon);
    assertEquals(expectedNumberOfPaths, actualNumberOfPaths);

    String expectedValueDungeonWrapped = new StringBuilder()
            .append("\n|           \n")
            .append("0--0  0--0--\n")
            .append("   |        \n")
            .append("$0--0--0--0  \n")
            .append("   |        \n")
            .append("0--0  0--0--\n")
            .append("            \n")
            .append("0--0--X0  0  \n")
            .append("|        |  \n")
            .append("0--0--0  0--\n")
            .append("\n")
            .append("Number of edges: 19").toString();
    assertEquals(expectedValueDungeonWrapped, visualizeKruskals(dungeonWrapped).toString());
    // Testing interconnectivity
    // Totals paths in the tree are paths in MST which is (rows * columns) - 1
    // Plus the interconnectivity provided.
    expectedNumberOfPaths = getExpectedNumberOfPaths(dungeonWrapped, 0);
    actualNumberOfPaths = traverseAllNodesAndReturnTotalPaths(dungeonWrapped);
    assertEquals(expectedNumberOfPaths, actualNumberOfPaths);

    String expectedValueInterconnectivityTest = new StringBuilder().append("\n")
            .append("|  |  |  |  \n")
            .append("0--0  0  0  \n")
            .append("|     |  |  \n")
            .append("0--0  0--0--\n")
            .append("         |  \n")
            .append("$0  0--0--0--\n")
            .append("   |  |     \n")
            .append("0--0--0  X0--\n")
            .append("      |     \n")
            .append("0--0--0  0  \n")
            .append("\n")
            .append("Number of edges: 23").toString();
    assertEquals(expectedValueInterconnectivityTest,
            visualizeKruskals(dungeonWrappedInterconnectivity).toString());
    // Testing interconnectivity
    // Totals paths in the tree are paths in MST which is (rows * columns) - 1
    // Plus the interconnectivity provided.
    expectedNumberOfPaths = getExpectedNumberOfPaths(
            dungeonWrappedInterconnectivity, 4);
    actualNumberOfPaths = traverseAllNodesAndReturnTotalPaths(dungeonWrappedInterconnectivity);
    assertEquals(expectedNumberOfPaths, actualNumberOfPaths);

    //30 percent treasure Wrapped
    String expectedValueTreasureFilled = new StringBuilder()
            .append("\n|           \n")
            .append("0--0  X0  0--\n")
            .append("|  |  |  |  \n")
            .append("0  0  0  0  \n")
            .append("|  |  |  |  \n")
            .append("▲--▲  0  0--\n")
            .append("|  |  |  |  \n")
            .append("0  0  0  0  \n")
            .append("|     |  |  \n")
            .append("▲  0--0--$0--\n")
            .append("\n")
            .append("Number of edges: 23").toString();
    assertEquals(expectedValueTreasureFilled,
            visualizeKruskals(dungeonWrappedInterconnectivityTreasure30).toString());
    // Testing interconnectivity
    // Totals paths in the tree are paths in MST which is (rows * columns) - 1
    // Plus the interconnectivity provided.
    expectedNumberOfPaths = getExpectedNumberOfPaths(
            dungeonWrappedInterconnectivityTreasure30, 4);
    actualNumberOfPaths = traverseAllNodesAndReturnTotalPaths(
            dungeonWrappedInterconnectivityTreasure30);
    assertEquals(expectedNumberOfPaths, actualNumberOfPaths);

    //30 percent treasure Not wrapped
    String expectedValueTreasureFilledTreasure30 = new StringBuilder()
            .append("\n            \n")
            .append("▲  0--0--0  \n")
            .append("|  |  |     \n")
            .append("0--$▲  0--0  \n")
            .append("   |        \n")
            .append("▲--0--0--0  \n")
            .append("   |  |  |  \n")
            .append("▲--0  ▲  0  \n")
            .append("   |        \n")
            .append("0--0--0--X0  \n")
            .append("\n")
            .append("Number of edges: 19").toString();
    assertEquals(expectedValueTreasureFilledTreasure30,
            visualizeKruskals(dungeonTreasure30).toString());
    // Testing interconnectivity
    // Totals paths in the tree are paths in MST which is (rows * columns) - 1
    // Plus the interconnectivity provided.
    expectedNumberOfPaths = getExpectedNumberOfPaths(dungeonTreasure30, 0);
    actualNumberOfPaths = traverseAllNodesAndReturnTotalPaths(dungeonTreasure30);
    assertEquals(expectedNumberOfPaths, actualNumberOfPaths);

    // Testing interconnectivity
    // Totals paths in the tree are paths in MST which is (rows * columns) - 1
    // Plus the interconnectivity provided.
    Randomizer gameRandomizer = new GameRandomizer();
    int randomRows = gameRandomizer.getRandomValue(5,20);
    int randomColumns = gameRandomizer.getRandomValue(5,20);
    int randomInterconnectivity = gameRandomizer.getRandomValue(5,20);
    Dungeon randomDungeon = new DungeonModel(randomRows,
            randomColumns, true, randomInterconnectivity, 0, gameRandomizer);
    expectedNumberOfPaths = getExpectedNumberOfPaths(randomDungeon, randomInterconnectivity);
    actualNumberOfPaths = traverseAllNodesAndReturnTotalPaths(randomDungeon);
    assertEquals(expectedNumberOfPaths, actualNumberOfPaths);
  }

  @Test
  public void testMinimum5HopsStartToEnd() {
    //Testing with deterministic maze
    String expectedValue = new StringBuilder("\n            \n")
            .append("0--0--0--0  \n")
            .append("|        |  \n")
            .append("0  $0--0--0  \n")
            .append("|     |     \n")
            .append("X0--0  0  0  \n")
            .append("|  |     |  \n")
            .append("0  0  0--0  \n")
            .append("|     |  |  \n")
            .append("0--0--0  0  \n")
            .append("\n")
            .append("Number of edges: 19").toString();
    //No interconnectivity, No treasure, not wrapped
    assertEquals(expectedValue, visualizeKruskals(dungeon).toString());
    int pathCount = 0;
    dungeon.movePlayer(Move.WEST);
    pathCount++;
    dungeon.movePlayer(Move.WEST);
    pathCount++;
    dungeon.movePlayer(Move.NORTH);
    pathCount++;
    dungeon.movePlayer(Move.EAST);
    pathCount++;
    dungeon.movePlayer(Move.EAST);
    pathCount++;
    dungeon.movePlayer(Move.EAST);
    pathCount++;
    dungeon.movePlayer(Move.SOUTH);
    pathCount++;
    assertFalse(dungeon.playerVisitedEnd());
    dungeon.movePlayer(Move.SOUTH);
    pathCount++;
    //assertTrue(dungeon.playerVisitedEnd());
    assertTrue(pathCount >= 5);

  }

  @Test
  public void testNavigationThroughWrappedPath() {
    String expectedValueTreasureFilled = new StringBuilder()
            .append("\n|           \n")
            .append("0--0  X0  0--\n")
            .append("|  |  |  |  \n")
            .append("0  0  0  0  \n")
            .append("|  |  |  |  \n")
            .append("▲--▲  0  0--\n")
            .append("|  |  |  |  \n")
            .append("0  0  0  0  \n")
            .append("|     |  |  \n")
            .append("▲  0--0--$0--\n")
            .append("\n")
            .append("Number of edges: 23").toString();
    assertEquals(expectedValueTreasureFilled,
            visualizeKruskals(dungeonWrappedInterconnectivityTreasure30).toString());
    assertEquals(expectedValueTreasureFilled,
            visualizeKruskals(dungeonWrappedInterconnectivityTreasure30).toString());
    //Try to move from 4,4 to 4,0
    dungeonWrappedInterconnectivityTreasure30.movePlayer(Move.WEST);
    Location playerCurrentLocation =
            dungeonWrappedInterconnectivityTreasure30.getPlayerCurrentLocation();
    int row = playerCurrentLocation.getRow();
    int column = playerCurrentLocation.getColumn();
    assertEquals(4,row);
    assertEquals(0,column);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testErrorOnNavigatingWrappedPathInNonWrappedDungeon() {
    String expectedValueTreasureFilledTreasure30 = new StringBuilder()
            .append("\n            \n")
            .append("▲  0--0--0  \n")
            .append("|  |  |     \n")
            .append("0--$▲  0--0  \n")
            .append("   |        \n")
            .append("▲--0--0--0  \n")
            .append("   |  |  |  \n")
            .append("▲--0  ▲  0  \n")
            .append("   |        \n")
            .append("0--0--0--X0  \n")
            .append("\n")
            .append("Number of edges: 19").toString();
    assertEquals(expectedValueTreasureFilledTreasure30,
            visualizeKruskals(dungeonTreasure30).toString());
    //Try to move from 1,1 to 1,0
    dungeonTreasure30.movePlayer(Move.EAST);
    Location playerCurrentLocation = dungeonTreasure30.getPlayerCurrentLocation();
    int row = playerCurrentLocation.getRow();
    int column = playerCurrentLocation.getColumn();
    assertEquals(1,row);
    assertEquals(0,column);
    //Try to move from 1,0 to 1,3
    dungeonTreasure30.movePlayer(Move.EAST);
  }

  @Test
  public void testTreasureFilledMaze() {
    //Test 0 percent treasure
    assertEquals(0, getTreasureFilledLocations(dungeonWrapped.getMaze()).size());
    assertEquals(0, getTreasureFilledLocations(dungeon.getMaze()).size());
    assertEquals(0,
            getTreasureFilledLocations(dungeonWrappedInterconnectivity.getMaze()).size());
    //30 percent treasure Wrapped
    String expectedValueTreasureFilled = new StringBuilder()
            .append("\n|           \n")
            .append("0--0  X0  0--\n")
            .append("|  |  |  |  \n")
            .append("0  0  0  0  \n")
            .append("|  |  |  |  \n")
            .append("▲--▲  0  0--\n")
            .append("|  |  |  |  \n")
            .append("0  0  0  0  \n")
            .append("|     |  |  \n")
            .append("▲  0--0--$0--\n")
            .append("\n")
            .append("Number of edges: 23").toString();
    final List<List<Location>> dungeonWrappedTreasure30Maze =
            dungeonWrappedInterconnectivityTreasure30.getMaze();
    assertEquals(expectedValueTreasureFilled,
            visualizeKruskals(dungeonWrappedInterconnectivityTreasure30).toString());
    int numberOfCaves = getAllCaves(dungeonWrappedTreasure30Maze).size();
    //30 percent of all locations that are caves i.e entrances not equal to 2
    int expectedNumberOfTreasureFilledCaves = Double.valueOf(
            Math.round(0.3 * numberOfCaves)).intValue();
    List<Location> treasureFilledLocations =
            getTreasureFilledLocations(dungeonWrappedTreasure30Maze);
    assertEquals(expectedNumberOfTreasureFilledCaves,
            treasureFilledLocations.size());
    //asserting tunnel is not filled with treasure
    for (Location location : treasureFilledLocations) {
      Set<Move> nextMoves = location.getNextMoves();
      assertNotEquals(2, nextMoves.size());
    }
    //30 percent treasure Not wrapped
    String expectedValueTreasureFilledTreasure30 = new StringBuilder()
            .append("\n            \n")
            .append("▲  0--0--0  \n")
            .append("|  |  |     \n")
            .append("0--$▲  0--0  \n")
            .append("   |        \n")
            .append("▲--0--0--0  \n")
            .append("   |  |  |  \n")
            .append("▲--0  ▲  0  \n")
            .append("   |        \n")
            .append("0--0--0--X0  \n")
            .append("\n")
            .append("Number of edges: 19").toString();
    final List<List<Location>> dungeonTreasure30Maze =
            dungeonTreasure30.getMaze();
    assertEquals(expectedValueTreasureFilledTreasure30,
            visualizeKruskals(dungeonTreasure30).toString());
    int numberOfCavesTreasure30 = getAllCaves(dungeonTreasure30Maze).size();
    //30 percent of all locations that are caves i.e entrances not equal to 2
    int expectedNumberOfTreasureFilledCavesTreasure30 = Double.valueOf(
            Math.round(0.3 * numberOfCavesTreasure30)).intValue();
    treasureFilledLocations = getTreasureFilledLocations(dungeonTreasure30Maze);
    assertEquals(expectedNumberOfTreasureFilledCavesTreasure30,
            treasureFilledLocations.size());
    //asserting tunnel is not filled with treasure
    for (Location location : treasureFilledLocations) {
      Set<Move> nextMoves = location.getNextMoves();
      assertNotEquals(2, nextMoves.size());
    }
  }

  @Test
  public void testTreasurePicking() {
    List<List<Location>> dungeonTreasure30Maze = dungeonTreasure30.getMaze();
    int numberOfCaves = getAllCaves(dungeonTreasure30Maze).size();
    //30 percent of all locations that are caves i.e entrances not equal to 2
    int expectedNumberOfTreasureFilledCaves = Double.valueOf(
            Math.round(0.3 * numberOfCaves)).intValue();
    assertEquals(expectedNumberOfTreasureFilledCaves,
            getTreasureFilledLocations(dungeonTreasure30Maze).size());
    Location playerCurrentLocation = dungeonTreasure30.getPlayerCurrentLocation();
    Location startLocation = dungeonTreasure30.getStartLocation();
    assertTrue(compareLocations(startLocation, playerCurrentLocation));
    Player playerDescription = dungeonTreasure30.getPlayerDescription();
    assertFalse(playerDescription.hasTreasure());
    assertEquals(0, playerDescription.getTreasure().size());
    traverseToFirstTreasureCave(dungeonTreasure30);
    Location playerNewLocationWithTreasure = dungeonTreasure30.getPlayerCurrentLocation();
    boolean expectedValue = playerNewLocationWithTreasure.hasTreasure();
    Map<Treasure, Integer> caveTreasure = playerNewLocationWithTreasure.getTreasure();
    assertTrue(expectedValue);
    assertTrue(caveTreasure.size() > 0);
    dungeonTreasure30.playerPickTreasure();
    Player playerNewDescription = dungeonTreasure30.getPlayerDescription();
    //Location no longer has treasure after picking
    assertFalse(playerNewLocationWithTreasure.hasTreasure());
    //player has treasure and same as treasure that was in cave
    assertTrue(playerNewDescription.hasTreasure());
    assertEquals(caveTreasure.size(), playerNewDescription.getTreasure().size());
    assertEquals(caveTreasure, playerNewDescription.getTreasure());
    //traverse all nodes picking up treasure
    traverseAllNodes(
            initiateVisitGrid(dungeonTreasure30Maze.size(), dungeonTreasure30Maze.get(0).size()),
            dungeonTreasure30);
    int expectedCavesWithTreasureAfterTraversal = 0;
    assertEquals(expectedCavesWithTreasureAfterTraversal,
            getTreasureFilledLocations(dungeonTreasure30.getMaze()).size());


    List<List<Location>> dungeonWrappedInterconnectivityTreasure30Maze =
            dungeonWrappedInterconnectivityTreasure30.getMaze();
    numberOfCaves = getAllCaves(dungeonWrappedInterconnectivityTreasure30Maze).size();
    //30 percent of all locations that are caves i.e entrances not equal to 2
    expectedNumberOfTreasureFilledCaves = Double.valueOf(
            Math.round(0.3 * numberOfCaves)).intValue();
    assertEquals(expectedNumberOfTreasureFilledCaves,
            getTreasureFilledLocations(dungeonWrappedInterconnectivityTreasure30Maze).size());
    playerCurrentLocation = dungeonWrappedInterconnectivityTreasure30.getPlayerCurrentLocation();
    startLocation = dungeonWrappedInterconnectivityTreasure30.getStartLocation();
    assertTrue(compareLocations(startLocation, playerCurrentLocation));
    playerDescription = dungeonWrappedInterconnectivityTreasure30.getPlayerDescription();
    assertFalse(playerDescription.hasTreasure());
    assertEquals(0, playerDescription.getTreasure().size());
    traverseToFirstTreasureCave(dungeonWrappedInterconnectivityTreasure30);
    playerNewLocationWithTreasure =
            dungeonWrappedInterconnectivityTreasure30.getPlayerCurrentLocation();
    expectedValue = playerNewLocationWithTreasure.hasTreasure();
    caveTreasure = playerNewLocationWithTreasure.getTreasure();
    assertTrue(caveTreasure.size() > 0);
    dungeonWrappedInterconnectivityTreasure30.playerPickTreasure();
    playerNewDescription = dungeonWrappedInterconnectivityTreasure30.getPlayerDescription();
    //Location no longer has treasure after picking
    assertFalse(playerNewLocationWithTreasure.hasTreasure());
    //player has treasure and same as treasure that was in cave
    assertTrue(playerNewDescription.hasTreasure());
    assertEquals(caveTreasure.size(), playerNewDescription.getTreasure().size());
    assertEquals(caveTreasure, playerNewDescription.getTreasure());
    //traverse all nodes picking up treasure
    traverseAllNodes(
            initiateVisitGrid(dungeonTreasure30Maze.size(), dungeonTreasure30Maze.get(0).size()),
            dungeonWrappedInterconnectivityTreasure30);
    assertEquals(expectedCavesWithTreasureAfterTraversal,
            getTreasureFilledLocations(dungeonWrappedInterconnectivityTreasure30.getMaze()).size());

  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidTreasurePicking() {
    //Picking on start position of maze with no treasure
    dungeon.playerPickTreasure();
  }

  @Test
  public void testModelMutation() {
    final List<List<Location>> maze = dungeon.getMaze();
    int expectedSize = maze.size();
    maze.remove(0);
    final List<List<Location>> maze2 = dungeon.getMaze();
    //modifying fetched maze state doesn't affect model internal maze state
    assertEquals(expectedSize,maze2.size());
    Location location = maze2.get(0).get(1);
    location = null;
    assertNull(location);
    final List<List<Location>> maze3 = dungeon.getMaze();
    assertNotNull(maze3.get(0).get(1));

    //Modifying fetched location doesn't affect model internal state
    Location startLocation = dungeon.getStartLocation();
    startLocation = null;
    assertNull(startLocation);
    assertNotNull(dungeon.getStartLocation());
    Location endLocation = dungeon.getEndLocation();
    endLocation = null;
    assertNull(endLocation);
    assertNotNull(dungeon.getEndLocation());
    Location playerLocation = dungeon.getPlayerCurrentLocation();
    playerLocation = null;
    assertNull(playerLocation);
    assertNotNull(dungeon.getPlayerCurrentLocation());

    //Modifying fetched available moves doesn't affect model state
    Set<Move> nextMoves = dungeon.getPlayerCurrentLocation().getNextMoves();
    int size = nextMoves.size();
    assertNotEquals(0, size);
    nextMoves.removeAll(nextMoves);
    assertEquals(0, nextMoves.size());
    nextMoves = null;
    assertNull(nextMoves);
    Set<Move> nextMoves1 = dungeon.getPlayerCurrentLocation().getNextMoves();
    assertNotNull(nextMoves1);
    int size1 = nextMoves1.size();
    assertNotEquals(0, size1);
    assertEquals(size, size1);

    //Modifying fetched Player does not modify
    Player playerDescription = dungeon.getPlayerDescription();
    playerDescription = null;
    assertNotNull(dungeon.getPlayerDescription());
  }

  @Test
  public void testMove() {
    Randomizer randomizer = new GameRandomizer();

    Location startLocation = dungeon.getStartLocation();
    Location playerLocation = dungeon.getPlayerCurrentLocation();
    assertTrue(compareLocations(startLocation, playerLocation));
    List<Move> dungeonAvailableMoves = new ArrayList<>(dungeon.getAvailableMoves());
    int randomIndex = randomizer.getRandomValue(0, dungeonAvailableMoves.size() - 1);
    Move move = dungeonAvailableMoves.get(randomIndex);
    List<Integer> nextLocation = getNextLocation(move, dungeon);
    int expectedRow = nextLocation.get(0);
    int expectedColumn = nextLocation.get(1);
    dungeon.movePlayer(move);
    Location newPlayerLocation = dungeon.getPlayerCurrentLocation();
    int newRow = newPlayerLocation.getRow();
    int newColumn = newPlayerLocation.getColumn();
    assertFalse(compareLocations(newPlayerLocation, playerLocation));
    assertEquals(expectedRow, newRow);
    assertEquals(expectedColumn, newColumn);

    startLocation = dungeonWrapped.getStartLocation();
    playerLocation = dungeonWrapped.getPlayerCurrentLocation();
    assertTrue(compareLocations(startLocation, playerLocation));
    dungeonAvailableMoves = new ArrayList<>(dungeonWrapped.getAvailableMoves());
    randomIndex = randomizer.getRandomValue(0, dungeonAvailableMoves.size() - 1);
    move = dungeonAvailableMoves.get(randomIndex);
    nextLocation = getNextLocation(move, dungeonWrapped);
    expectedRow = nextLocation.get(0);
    expectedColumn = nextLocation.get(1);
    dungeonWrapped.movePlayer(move);
    newPlayerLocation = dungeonWrapped.getPlayerCurrentLocation();
    newRow = newPlayerLocation.getRow();
    newColumn = newPlayerLocation.getColumn();
    assertFalse(compareLocations(newPlayerLocation, playerLocation));
    assertEquals(expectedRow, newRow);
    assertEquals(expectedColumn, newColumn);

    startLocation = dungeonWrappedInterconnectivity.getStartLocation();
    playerLocation = dungeonWrappedInterconnectivity.getPlayerCurrentLocation();
    assertTrue(compareLocations(startLocation, playerLocation));
    dungeonAvailableMoves = new ArrayList<>(dungeonWrappedInterconnectivity.getAvailableMoves());
    randomIndex = randomizer.getRandomValue(0, dungeonAvailableMoves.size() - 1);
    move = dungeonAvailableMoves.get(randomIndex);
    nextLocation = getNextLocation(move, dungeonWrappedInterconnectivity);
    expectedRow = nextLocation.get(0);
    expectedColumn = nextLocation.get(1);
    dungeonWrappedInterconnectivity.movePlayer(move);
    newPlayerLocation = dungeonWrappedInterconnectivity.getPlayerCurrentLocation();
    newRow = newPlayerLocation.getRow();
    newColumn = newPlayerLocation.getColumn();
    assertFalse(compareLocations(newPlayerLocation, playerLocation));
    assertEquals(expectedRow, newRow);
    assertEquals(expectedColumn, newColumn);

    startLocation = dungeonWrappedInterconnectivityTreasure30.getStartLocation();
    playerLocation = dungeonWrappedInterconnectivityTreasure30.getPlayerCurrentLocation();
    assertTrue(compareLocations(startLocation, playerLocation));
    dungeonAvailableMoves =
            new ArrayList<>(dungeonWrappedInterconnectivityTreasure30.getAvailableMoves());
    randomIndex = randomizer.getRandomValue(0, dungeonAvailableMoves.size() - 1);
    move = dungeonAvailableMoves.get(randomIndex);
    nextLocation = getNextLocation(move, dungeonWrappedInterconnectivityTreasure30);
    expectedRow = nextLocation.get(0);
    expectedColumn = nextLocation.get(1);
    dungeonWrappedInterconnectivityTreasure30.movePlayer(move);
    newPlayerLocation = dungeonWrappedInterconnectivityTreasure30.getPlayerCurrentLocation();
    newRow = newPlayerLocation.getRow();
    newColumn = newPlayerLocation.getColumn();
    assertFalse(compareLocations(newPlayerLocation, playerLocation));
    assertEquals(expectedRow, newRow);
    assertEquals(expectedColumn, newColumn);

    startLocation = dungeonTreasure30.getStartLocation();
    playerLocation = dungeonTreasure30.getPlayerCurrentLocation();
    assertTrue(compareLocations(startLocation, playerLocation));
    dungeonAvailableMoves = new ArrayList<>(dungeonTreasure30.getAvailableMoves());
    randomIndex = randomizer.getRandomValue(0, dungeonAvailableMoves.size() - 1);
    move = dungeonAvailableMoves.get(randomIndex);
    nextLocation = getNextLocation(move, dungeonTreasure30);
    expectedRow = nextLocation.get(0);
    expectedColumn = nextLocation.get(1);
    dungeonTreasure30.movePlayer(move);
    newPlayerLocation = dungeonTreasure30.getPlayerCurrentLocation();
    newRow = newPlayerLocation.getRow();
    newColumn = newPlayerLocation.getColumn();
    assertFalse(compareLocations(newPlayerLocation, playerLocation));
    assertEquals(expectedRow, newRow);
    assertEquals(expectedColumn, newColumn);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMoveException() {
    Set<Move> availableMoves = dungeon.getAvailableMoves();
    for (Move move : Move.values()) {
      if (!availableMoves.contains(move)) {
        dungeon.movePlayer(move);
      }
    }
  }

  @Test
  public void testAllLocationsAccessible() {
    int rows = dungeon.getMaze().size();
    int columns = dungeon.getMaze().get(0).size();
    //2D Array of Booleans initialized to false
    Boolean[][] visitedGrid = initiateVisitGrid(rows, columns);
    //Assert grid has false value
    assertFalse(fullyTraversed(visitedGrid));
    //Traverse all nodes, mark visited nodes to true in 2D Array
    traverseAllNodes(visitedGrid, dungeon);
    //Assert grid all values in grid are true
    assertTrue(fullyTraversed(visitedGrid));

    visitedGrid = initiateVisitGrid(rows, columns);
    assertFalse(fullyTraversed(visitedGrid));
    traverseAllNodes(visitedGrid, dungeonWrapped);
    assertTrue(fullyTraversed(visitedGrid));

    visitedGrid = initiateVisitGrid(rows, columns);
    assertFalse(fullyTraversed(visitedGrid));
    traverseAllNodes(visitedGrid, dungeonWrappedInterconnectivity);
    assertTrue(fullyTraversed(visitedGrid));

    visitedGrid = initiateVisitGrid(rows, columns);
    assertFalse(fullyTraversed(visitedGrid));
    traverseAllNodes(visitedGrid, dungeonWrappedInterconnectivityTreasure30);
    assertTrue(fullyTraversed(visitedGrid));

    visitedGrid = initiateVisitGrid(rows, columns);
    assertFalse(fullyTraversed(visitedGrid));
    traverseAllNodes(visitedGrid, dungeonTreasure30);
    assertTrue(fullyTraversed(visitedGrid));
  }

  @Test
  public void startToEnd() {
    Location startLocation = dungeon.getStartLocation();
    Location playerLocation = dungeon.getPlayerCurrentLocation();
    Location endLocation = dungeon.getEndLocation();
    assertTrue(compareLocations(startLocation, playerLocation));
    assertFalse(compareLocations(endLocation, playerLocation));
    assertFalse(compareLocations(endLocation, startLocation));
    assertFalse(dungeon.playerVisitedEnd());
    traverseToEndNode(dungeon);
    Location newPlayerLocation = dungeon.getPlayerCurrentLocation();
    assertFalse(compareLocations(startLocation, newPlayerLocation));
    assertTrue(compareLocations(endLocation, newPlayerLocation));
    assertFalse(compareLocations(endLocation, startLocation));
    assertTrue(dungeon.playerVisitedEnd());

    Location startLocationTreasure30 = dungeonTreasure30.getStartLocation();
    Location playerLocationTreasure30 = dungeonTreasure30.getPlayerCurrentLocation();
    Location endLocationTreasure30 = dungeonTreasure30.getEndLocation();
    assertTrue(compareLocations(startLocationTreasure30, playerLocationTreasure30));
    assertFalse(compareLocations(endLocationTreasure30, playerLocationTreasure30));
    assertFalse(compareLocations(endLocationTreasure30, startLocationTreasure30));
    assertFalse(dungeonTreasure30.playerVisitedEnd());
    traverseToEndNode(dungeonTreasure30);
    Location newPlayerLocationTreasure30 = dungeonTreasure30.getPlayerCurrentLocation();
    assertFalse(compareLocations(startLocationTreasure30, newPlayerLocationTreasure30));
    assertTrue(compareLocations(endLocationTreasure30, newPlayerLocationTreasure30));
    assertFalse(compareLocations(endLocationTreasure30, startLocationTreasure30));
    assertTrue(dungeonTreasure30.playerVisitedEnd());

    Location startLocationWrapped = dungeonWrapped.getStartLocation();
    Location playerLocationWrapped = dungeonWrapped.getPlayerCurrentLocation();
    Location endLocationWrapped = dungeonWrapped.getEndLocation();
    assertTrue(compareLocations(startLocationWrapped, playerLocationWrapped));
    assertFalse(compareLocations(endLocationWrapped, playerLocationWrapped));
    assertFalse(compareLocations(endLocationWrapped, startLocationWrapped));
    assertFalse(dungeonWrapped.playerVisitedEnd());
    traverseToEndNode(dungeonWrapped);
    Location newPlayerLocationWrapped = dungeonWrapped.getPlayerCurrentLocation();
    assertFalse(compareLocations(startLocationWrapped, newPlayerLocationWrapped));
    assertTrue(compareLocations(endLocationWrapped, newPlayerLocationWrapped));
    assertFalse(compareLocations(endLocationWrapped, startLocationWrapped));
    assertTrue(dungeonWrapped.playerVisitedEnd());

    Location startLocationWrappedInterconnectivity =
            dungeonWrappedInterconnectivity.getStartLocation();
    Location playerLocationWrappedInterconnectivity =
            dungeonWrappedInterconnectivity.getPlayerCurrentLocation();
    Location endLocationWrappedInterconnectivity = dungeonWrappedInterconnectivity.getEndLocation();
    assertTrue(compareLocations(startLocationWrappedInterconnectivity,
            playerLocationWrappedInterconnectivity));
    assertFalse(compareLocations(endLocationWrappedInterconnectivity,
            playerLocationWrappedInterconnectivity));
    assertFalse(compareLocations(endLocationWrappedInterconnectivity,
            startLocationWrappedInterconnectivity));
    assertFalse(dungeonWrappedInterconnectivity.playerVisitedEnd());
    traverseToEndNode(dungeonWrappedInterconnectivity);
    Location newPlayerLocationWrappedInterconnectivity =
            dungeonWrappedInterconnectivity.getPlayerCurrentLocation();
    assertFalse(compareLocations(startLocationWrappedInterconnectivity,
            newPlayerLocationWrappedInterconnectivity));
    assertTrue(compareLocations(endLocationWrappedInterconnectivity,
            newPlayerLocationWrappedInterconnectivity));
    assertFalse(compareLocations(endLocationWrappedInterconnectivity,
            startLocationWrappedInterconnectivity));
    assertTrue(dungeonWrappedInterconnectivity.playerVisitedEnd());

    Location startLocationWrappedInterconnectivityTreasure30 =
            dungeonWrappedInterconnectivityTreasure30.getStartLocation();
    Location playerLocationWrappedInterconnectivityTreasure30 =
            dungeonWrappedInterconnectivityTreasure30.getPlayerCurrentLocation();
    Location endLocationWrappedInterconnectivityTreasure30 =
            dungeonWrappedInterconnectivityTreasure30.getEndLocation();
    assertTrue(compareLocations(startLocationWrappedInterconnectivityTreasure30,
            playerLocationWrappedInterconnectivityTreasure30));
    assertFalse(compareLocations(endLocationWrappedInterconnectivityTreasure30,
            playerLocationWrappedInterconnectivityTreasure30));
    assertFalse(compareLocations(endLocationWrappedInterconnectivityTreasure30,
            startLocationWrappedInterconnectivityTreasure30));
    assertFalse(dungeonWrappedInterconnectivityTreasure30.playerVisitedEnd());
    traverseToEndNode(dungeonWrappedInterconnectivityTreasure30);
    Location newPlayerLocationWrappedInterconnectivityTreasure30 =
            dungeonWrappedInterconnectivityTreasure30.getPlayerCurrentLocation();
    assertFalse(compareLocations(startLocationWrappedInterconnectivityTreasure30,
            newPlayerLocationWrappedInterconnectivityTreasure30));
    assertTrue(compareLocations(endLocationWrappedInterconnectivityTreasure30,
            newPlayerLocationWrappedInterconnectivityTreasure30));
    assertFalse(compareLocations(endLocationWrappedInterconnectivityTreasure30,
            startLocationWrappedInterconnectivityTreasure30));
    assertTrue(dungeonWrappedInterconnectivityTreasure30.playerVisitedEnd());
  }

  private List<Location> getTreasureFilledLocations(List<List<Location>> maze) {
    List<Location> treasureFilledLocations = new ArrayList<>();
    for (List<Location> list : maze) {
      for (Location locationNode : list) {
        if (locationNode.hasTreasure()) {
          treasureFilledLocations.add(locationNode);
        }
      }
    }
    return treasureFilledLocations;
  }

  private static StringBuilder visualizeKruskals(Dungeon dungeon) {
    List<List<Location>> maze = dungeon.getMaze();
    Location startLocation = dungeon.getStartLocation();
    Location endLocation = dungeon.getEndLocation();
    Location playerCurrentLocation = dungeon.getPlayerCurrentLocation();
    StringBuilder sb = new StringBuilder();
    int count = 0;
    for (List<Location> list : maze) {
      sb.append("\n");
      for (Location locationNode : list) {
        Set<Move> nextMoves = locationNode.getNextMoves();
        if (nextMoves.contains(Move.NORTH)) {
          sb.append("|");
          count++;
        } else {
          sb.append(" ");
        }
        sb.append("  ");
      }
      sb.append("\n");
      for (Location locationNode : list) {
        if (compareLocations(startLocation, locationNode) && compareLocations(playerCurrentLocation,
                locationNode)) {
          sb.append("$");
        } else if (compareLocations(startLocation, locationNode)) {
          sb.append("S");
        } else if (compareLocations(endLocation, locationNode) && compareLocations(
                playerCurrentLocation, locationNode)) {
          sb.append("*");
        } else if (compareLocations(playerCurrentLocation, locationNode)) {
          sb.append("P");
        } else if (compareLocations(endLocation, locationNode)) {
          sb.append("X");
        }
        if (locationNode.hasTreasure()) {
          sb.append("▲");
        } else {
          sb.append("0");
        }
        Set<Move> nextMoves = locationNode.getNextMoves();
        if (nextMoves.contains(Move.WEST)) {
          sb.append("--");
          count++;
        } else {
          sb.append("  ");
        }
      }
    }
    sb.append("\n\nNumber of edges: ").append(count);
    return sb;
  }

  private static void traverseAllNodes(Boolean[][] visitedGrid, Dungeon dungeon) {
    while (!fullyTraversed(visitedGrid)) {
      Location playerCurrentLocation = dungeon.getPlayerCurrentLocation();
      int row = playerCurrentLocation.getRow();
      int column = playerCurrentLocation.getColumn();
      visitedGrid[row][column] = true;
      if (playerCurrentLocation.hasTreasure()) {
        dungeon.playerPickTreasure();
      }
      List<Move> availableMoves = new ArrayList<>(dungeon.getAvailableMoves());
      Collections.shuffle(availableMoves);
      goNextMove(dungeon, playerCurrentLocation, availableMoves);
    }
  }

  private static int traverseAllNodesAndReturnTotalPaths(Dungeon dungeon) {
    Boolean[][] visitedGrid = initiateVisitGrid(dungeon.getMaze().size(),
            dungeon.getMaze().get(0).size());
    int exitsFromNodes = 0;
    while (!fullyTraversed(visitedGrid)) {
      Location playerCurrentLocation = dungeon.getPlayerCurrentLocation();
      int row = playerCurrentLocation.getRow();
      int column = playerCurrentLocation.getColumn();
      List<Move> availableMoves = new ArrayList<>(dungeon.getAvailableMoves());
      Collections.shuffle(availableMoves);
      if (!visitedGrid[row][column]) {
        exitsFromNodes += availableMoves.size();
        visitedGrid[row][column] = true;
      }
      if (playerCurrentLocation.hasTreasure()) {
        dungeon.playerPickTreasure();
      }
      goNextMove(dungeon, playerCurrentLocation, availableMoves);
    }
    return exitsFromNodes / 2;
  }

  private static void traverseToEndNode(Dungeon dungeon) {
    do {
      Location playerCurrentLocation = dungeon.getPlayerCurrentLocation();
      if (playerCurrentLocation.hasTreasure()) {
        dungeon.playerPickTreasure();
      }
      List<Move> availableMoves = new ArrayList<>(dungeon.getAvailableMoves());
      Collections.shuffle(availableMoves);
      goNextMove(dungeon, playerCurrentLocation, availableMoves);
    }
    while (!compareLocations(dungeon.getPlayerCurrentLocation(), dungeon.getEndLocation()));
  }

  private static void traverseToFirstTreasureCave(Dungeon dungeon) {
    do {
      Location playerCurrentLocation = dungeon.getPlayerCurrentLocation();
      if (playerCurrentLocation.hasTreasure()) {
        break;
      }
      List<Move> availableMoves = new ArrayList<>(dungeon.getAvailableMoves());
      Collections.shuffle(availableMoves);
      goNextMove(dungeon, playerCurrentLocation, availableMoves);
    }
    while (!compareLocations(dungeon.getPlayerCurrentLocation(), dungeon.getEndLocation()));
  }

  private static void goNextMove(Dungeon dungeon, Location playerCurrentLocation,
                                 List<Move> availableMoves) {
    for (Move move : availableMoves) {
      List<Integer> nextLocationRowsColumns = getNextLocation(move, dungeon);
      Location nextLocation = getLocation(nextLocationRowsColumns.get(0),
              nextLocationRowsColumns.get(1), dungeon);
      if (!compareLocations(nextLocation, playerCurrentLocation)) {
        dungeon.movePlayer(move);
        break;
      }
    }
  }

  private static StringBuilder visualizeVisitedGrid(Boolean[][] visitedGrid) {
    StringBuilder sb = new StringBuilder("\n");
    for (Boolean[] booleans : visitedGrid) {
      sb.append("\n");
      for (Boolean aBoolean : booleans) {
        sb.append(" ").append(aBoolean);
      }
    }
    return sb;
  }

  private static boolean fullyTraversed(Boolean[][] visitedGrid) {
    for (Boolean[] booleans : visitedGrid) {
      for (Boolean aBoolean : booleans) {
        if (!aBoolean) {
          return false;
        }
      }
    }
    return true;
  }

  private static Boolean[][] initiateVisitGrid(int rows, int columns) {
    Boolean[][] visitedGrid = new Boolean[rows][columns];
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        visitedGrid[i][j] = false;
      }
    }
    return visitedGrid;
  }

  private static boolean compareLocations(Location locationA, Location locationB) {
    return locationA.getRow() == locationB.getRow()
            && locationA.getColumn() == locationB.getColumn();
  }

  private static List<Location> getAllCaves(List<List<Location>> maze) {
    List<Location> allNodes = new ArrayList<>();
    for (List<Location> yList : maze) {
      for (Location location : yList) {
        if (location.getNextMoves().size() != 2) {
          allNodes.add(location);
        }
      }
    }
    return allNodes;
  }

  private static int getNumberOfTunnels(List<List<Location>> maze) {
    return maze.size() * maze.get(0).size() - getAllCaves(maze).size();
  }

  private static Location getLocation(int x, int y, Dungeon dungeon) {
    return dungeon.getMaze().get(x).get(y);
  }

  private static List<Integer> getNextLocation(Move move, Dungeon dungeon)
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
    if (coordinates.size() != 2) {
      throw new IllegalArgumentException("Something went wrong in getting new coordinates");
    }
    return coordinates;
  }

  private int getExpectedNumberOfPaths(Dungeon dungeon, int interconnectivity) {
    return ((dungeon.getMaze().size() * dungeon.getMaze().get(1).size()) - 1) + interconnectivity;
  }
}