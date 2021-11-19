package dungeon;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import randomizer.Randomizer;

/**
 * DungeonModel represents an instance of the Dungeon game.
 * It contains the player, maze, treasure and maintains game state.
 */
public class DungeonModel implements Dungeon {
  private final List<List<LocationPrivate>> maze;
  private final Randomizer randomizer;
  private final PlayerPrivate player;
  private int startX;
  private int startY;
  private int endX;
  private int endY;
  private int currentX;
  private int currentY;
  private boolean playerVisitedEnd;
  private boolean isGameOver;
  private boolean isPlayerDead;

  /**
   * Creates an instance of DungeonModel.
   *
   * @param rows                       the number of rows in the dungeon maze.
   * @param columns                    the number of columns in the dungeon maze.
   * @param wrapped                    if the maze is wrapped around edges or not.
   * @param interconnectivity          the interconnectivity.
   * @param treasureAndArrowPercentage the percentage of caves that have treasure or arrows.
   *                                   If percentage of nodes is decimal it
   *                                   takes the lower bound number of nodes.
   * @param numberOfMonsters           the number of monsters in the dungeon of caves.
   * @param randomizer                 the game randomizer to use.
   * @throws IllegalArgumentException if maze dimensions are less than 3x2.
   * @throws IllegalArgumentException if randomizer is null.
   * @throws IllegalArgumentException if treasure percentage is negative or greater than 100.
   * @throws IllegalArgumentException if interconnectivity is not between 0
   *                                  and maximum possible interconnectivity.
   */
  //TODO Commandline params in readme and other places, Uncomment test
  public DungeonModel(int rows, int columns, boolean wrapped,
                      int interconnectivity, int treasureAndArrowPercentage, int numberOfMonsters,
                      Randomizer randomizer) throws IllegalArgumentException {
    validator(rows, columns, wrapped, interconnectivity, treasureAndArrowPercentage,
            numberOfMonsters, randomizer);
    this.randomizer = randomizer;
    this.maze = new ArrayList<>();
    this.player = new PlayerModel();
    this.startX = -1;
    this.startY = -1;
    this.endX = -1;
    this.endY = -1;
    this.currentX = startX;
    this.currentY = startY;
    this.playerVisitedEnd = false;
    this.isGameOver = false;
    this.isPlayerDead = false;
    buildDungeon(rows, columns, wrapped, interconnectivity, treasureAndArrowPercentage,
            numberOfMonsters, randomizer);
  }

  /**
   * Returns the maze of the dungeon.
   *
   * @return the maze as List of Lists of {@link Location}.
   */
  @Override
  public List<List<Location>> getMaze() {
    List<List<Location>> tmpMaze = new ArrayList<>();
    for (List<LocationPrivate> yList : this.maze) {
      List<Location> tmpList = new ArrayList<>(yList);
      tmpMaze.add(tmpList);
    }
    return tmpMaze;
  }

  /**
   * Returns the starting location/cave in the maze.
   *
   * @return {@link Location} representing the starting location.
   */
  @Override
  public Location getStartLocation() {
    return getLocation(startX, startY);
  }

  /**
   * Returns the end location/cave in the maze.
   *
   * @return {@link Location} representing the end location.
   */
  @Override
  public Location getEndLocation() {
    return getLocation(endX, endY);
  }

  /**
   * Returns the current location of player in the maze.
   *
   * @return {@link Location} representing the player's location.
   */
  @Override
  public Location getPlayerCurrentLocation() {
    return getLocation(currentX, currentY);
  }

  /**
   * Returns the moves that are possible from the current location.
   *
   * @return a Set of {@link Move}s
   */
  @Override
  public Set<Move> getAvailableDirections() {
    return getLocation(currentX, currentY).getNextMoves();
  }

  /**
   * Checks whether the player has visited the end cave.
   *
   * @return true if {@link Player} has visited the end cave.
   */
  @Override
  public boolean playerVisitedEnd() {
    return this.playerVisitedEnd;
  }

  /**
   * Checks whether the game is over or not.
   *
   * @return true if game has ended.
   */
  @Override
  public boolean isGameOver() {
    return isGameOver;
  }

  /**
   * Moves the player in the maze in the provided Move direction.
   *
   * @param move {@link Move} to be executed.
   * @throws IllegalArgumentException if provided move is not a valid move.
   */
  @Override
  public void movePlayer(Move move) throws IllegalArgumentException {
    if (!isGameOver && !isPlayerDead && validateNextMove(move)) {
      throw new IllegalArgumentException("Provided move is not a valid move " + move);
    }
    LocationPrivate nextLocation = getNextLocation(getPlayerCurrentLocation(), move);
    currentX = nextLocation.getRow();
    currentY = nextLocation.getColumn();
    if (nextLocation.hasMonster()) {
      Monster monster = nextLocation.getMonster();
      if (!monster.isDead() && monster.isInjured()) {
        int chance = randomizer.getRandomValue(0,1);
        System.out.println("Random number " + chance);
        if (chance == 1) {
          this.isPlayerDead = true;
        }
      }
      else {
        this.isPlayerDead = true;
      }
    }
    if (!isPlayerDead) {
      this.playerVisitedEnd = currentX == endX && currentY == endY;
    }
    if (playerVisitedEnd || isPlayerDead) {
      isGameOver = true;
    }
  }

  /**
   * Makes the player pick the treasure at the current location.
   *
   * @throws IllegalStateException if current location has no treasure.
   */
  @Override
  public void playerPickTreasure() throws IllegalStateException {
    Location playerCurrentLocation = getPlayerCurrentLocation();
    if (!playerCurrentLocation.hasTreasure()) {
      throw new IllegalStateException("Location has no treasure");
    }
    LocationPrivate currentLocation = getLocation(playerCurrentLocation.getRow(),
            playerCurrentLocation.getColumn());
    Map<Treasure, Integer> currentLocationTreasure = playerCurrentLocation.getTreasure();
    for (Treasure treasure : currentLocationTreasure.keySet()) {
      Integer quantity = currentLocationTreasure.get(treasure);
      player.addToTreasure(treasure, quantity);
      currentLocation.pickTreasure(treasure, quantity);

    }
  }

  /**
   * Provides the description of a player.
   *
   * @return player in the maze as a {@link Player}.
   */
  @Override
  public Player getPlayerDescription() {
    return player;
  }


  /**
   * Gets the SmellLevel at the provided location.
   *
   * @param location the location whose smell level is to be obtained.
   * @return SmellLevel of the particular location.
   * @throws IllegalStateException if there is problem with SmellLevel at the location.
   */
  @Override
  public SmellLevel getSmell(Location location) throws IllegalStateException {
    int smell = getSmellHelper(location);
    return SmellLevel.getSmellLevel(smell);
  }

  /**
   * Fires the crooked arrow in the specified direction and distance.
   *
   * @param direction the direction in which to fire the arrow.
   * @param arrowDistance the number of caves the arrow should traverse.
   * @return ArrowHitOutcome value that represents whether monster is successfully hit.
   * @throws IllegalArgumentException if distance is less than 1 or greater than 5.
   * @throws IllegalArgumentException if an invalid direction is provided.
   * @throws IllegalStateException if player has no arrows to fire.
   */
  @Override
  public ArrowHitOutcome shootArrow(Move direction, int arrowDistance)
          throws IllegalArgumentException, IllegalStateException {
    if (arrowDistance <= 0 || arrowDistance > 5) {
      throw new IllegalArgumentException("Distance cannot be less than 1 or greater than 5");
    }
    if (validateNextMove(direction)) {
      throw new IllegalArgumentException("Invalid direction");
    }
    if (player.hasArrows()) {
      return fireArrowHelper(direction, arrowDistance);
    }
    throw new IllegalStateException("Player has no arrows");
  }

  /**
   * Makes the player pick the arrows from the their current location.
   *
   * @throws IllegalStateException if the current location has no arrows.
   */
  @Override
  public void playerPickArrows() throws IllegalStateException {
    LocationPrivate playerCurrentLocation = getLocation(currentX, currentY);
    int arrows = playerCurrentLocation.pickArrows();
    player.pickArrows(arrows);
  }

  /**
   * Checks whether the player is alive or dead.
   *
   * @return true if {@link Player} has been killed.
   */
  @Override
  public boolean isPlayerDead() {
    return isPlayerDead;
  }

  private void generateStartEndNodes(Randomizer randomizer) {
    boolean startEndSet = false;
    List<LocationPrivate> allCaves = getAllCaves();
    while (!startEndSet && !allCaves.isEmpty()) {
      int randomIndex = randomizer.getRandomValue(0, allCaves.size() - 1);
      Location startLocation = allCaves.remove(randomIndex);
      startX = startLocation.getRow();
      startY = startLocation.getColumn();
      updateCurrentPosition(startX, startY);
      List<Location> locationStack = new LinkedList<>();//stack = util.Stack()
      List<Location> visited = new ArrayList<>();//explored = []
      Map<Location, List<Integer>> prospectiveEndNodes = new LinkedHashMap<>();
      locationStack.add(startLocation);//stack.push((problem.getStartState(), [], 0))
      int pathLength = 0;
      int elementsToDepthIncrease = 1;
      int nextElementsToDepthIncrease = 0;
      while (!locationStack.isEmpty()) {
        List<LocationPrivate> allCavesList = getAllCaves();
        LocationPrivate location = (LocationPrivate) locationStack.remove(0);
        updateCurrentPosition(location.getRow(), location.getColumn());
        Set<Move> locationNextMoves = location.getNextMoves();
        for (Move move : locationNextMoves) {
          Location childLocation = getNextLocation(getPlayerCurrentLocation(), move);
          if (!visited.contains(childLocation) && !locationStack.contains(childLocation)) {
            locationStack.add(childLocation);
            nextElementsToDepthIncrease++;
          }
        }
        if (!visited.contains(location)) {
          visited.add(location);
          --elementsToDepthIncrease;
          if (elementsToDepthIncrease == 0) {
            pathLength++;
            elementsToDepthIncrease = nextElementsToDepthIncrease;
            nextElementsToDepthIncrease = 0;
          }
          if (allCavesList.contains(location)) {
            List<Integer> pathLengths = prospectiveEndNodes.get(location);
            if (pathLengths == null) {
              pathLengths = new ArrayList<>();
            }
            pathLengths.add(pathLength);
            Collections.sort(pathLengths);
            prospectiveEndNodes.put(location, pathLengths);
          }
        }
      }
      List<Location> toBeRemoved = new ArrayList<>();
      for (Location location : prospectiveEndNodes.keySet()) {
        List<Integer> integerList = prospectiveEndNodes.get(location);
        Integer integer = integerList.get(0);
        if (integer < 6) {
          toBeRemoved.add(location);
        }
      }
      for (Location location : toBeRemoved) {
        prospectiveEndNodes.remove(location);
      }
      updateCurrentPosition(startX, startY);

      if (prospectiveEndNodes.size() > 0 && startX != -1 && startY != -1) {
        int randomIndexEndNode = randomizer.getRandomValue(0, prospectiveEndNodes.size() - 1);
        List<Location> prospectiveEndLocations = new ArrayList<>(prospectiveEndNodes.keySet());
        Location endLocation = prospectiveEndLocations.get(randomIndexEndNode);
        endX = endLocation.getRow();
        endY = endLocation.getColumn();
        updateCurrentPosition(startX, startY);
        startEndSet = true;
      }
    }
  }

  private LocationPrivate getLocation(int x, int y) {
    return maze.get(x).get(y);
  }

  private void updateCurrentPosition(int x, int y) {
    currentX = x;
    currentY = y;
  }

  private boolean validateNextMove(Move move) {
    if (move == null) {
      return true;
    }
    final Set<Move> nextMoves = getLocation(currentX, currentY).getNextMoves();
    return !nextMoves.contains(move);
  }

  private LocationPrivate getNextLocation(Location location, Move move)
          throws IllegalArgumentException {
    List<Integer> coordinates = new ArrayList<>();
    int row = location.getRow();
    int column = location.getColumn();
    switch (move) {
      case NORTH: {
        int x = row - 1;
        if (x < 0) {
          x = maze.size() - 1;
        }
        coordinates.add(x);
        coordinates.add(column);
        break;
      }
      case SOUTH: {
        int x = row + 1;
        if (x == maze.size()) {
          x = 0;
        }
        coordinates.add(x);
        coordinates.add(column);
        break;
      }
      case WEST: {
        int y = column + 1;
        if (y == maze.get(row).size()) {
          y = 0;
        }
        coordinates.add(row);
        coordinates.add(y);
        break;
      }
      case EAST: {
        int y = column - 1;
        if (y < 0) {
          y = maze.get(row).size() - 1;
        }
        coordinates.add(row);
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
    return maze.get(coordinates.get(0)).get(coordinates.get(1));
  }

  private void fillUpTreasure(int treasurePercentage, Randomizer randomizer) {
    if (treasurePercentage > 0) {
      List<LocationPrivate> allNodes = getAllCaves();
      double percentage = treasurePercentage / 100.00;
      int numberOfNodesToFillTreasure = Double.valueOf(
              Math.round(percentage * allNodes.size())).intValue();
      if (numberOfNodesToFillTreasure == 0) {
        numberOfNodesToFillTreasure = 1;
      }
      while (numberOfNodesToFillTreasure > 0) {
        List<Treasure> treasures = new ArrayList<>(Arrays.asList(Treasure.values()));
        int randomIndex = randomizer.getRandomValue(0, allNodes.size() - 1);
        LocationPrivate treasureNode = allNodes.remove(randomIndex);
        int randomNumberOfTreasures = randomizer.getRandomValue(1, 3);
        for (int i = 0; i < randomNumberOfTreasures; i++) {
          int randomTreasureIndex = randomizer.getRandomValue(0, treasures.size() - 1);
          Treasure treasure = treasures.remove(randomTreasureIndex);
          int randomTreasureQuantity = randomizer.getRandomValue(1, 5);
          treasureNode.setTreasure(treasure, randomTreasureQuantity);
        }
        numberOfNodesToFillTreasure--;
      }
    }
  }

  private void fillUpArrows(int treasurePercentage, Randomizer randomizer) {
    if (treasurePercentage > 0) {
      List<LocationPrivate> totalLocations = getAllLocations();
      double percentage = treasurePercentage / 100.00;
      int numberOfLocationsToFillArrows = Double.valueOf(
              Math.round(percentage * totalLocations.size())).intValue();
      if (numberOfLocationsToFillArrows == 0) {
        numberOfLocationsToFillArrows = 1;
      }
      while (numberOfLocationsToFillArrows > 0) {
        int randomIndex = randomizer.getRandomValue(0, totalLocations.size() - 1);
        LocationPrivate arrowNode = totalLocations.remove(randomIndex);
        int randomNumberOfArrows = randomizer.getRandomValue(1, 3);
        arrowNode.setArrows(randomNumberOfArrows);
        numberOfLocationsToFillArrows--;
      }
    }
  }

  private void fillUpMonsters(int numberOfMonsters, Randomizer randomizer) {
    List<LocationPrivate> allNodes = getAllCaves();
    LocationPrivate endNode = getLocation(endX, endY);
    endNode.setMonster();
    numberOfMonsters--;
    while (numberOfMonsters > 0) {
      int randomIndex = randomizer.getRandomValue(0, allNodes.size() - 1);
      LocationPrivate monsterCave = allNodes.remove(randomIndex);
      if (monsterCave.getRow() != startX && monsterCave.getColumn() != startY) {
        monsterCave.setMonster();
        numberOfMonsters--;
      }
    }
  }

  private ArrowHitOutcome fireArrowHelper(Move direction, int arrowDistance)
          throws IllegalArgumentException, IllegalStateException {
    player.fireArrow();
    ArrowHitOutcome hit = ArrowHitOutcome.MISS;
    int distance = arrowDistance;
    LocationPrivate arrowCurrentLocation = getLocation(currentX,currentY);

    Move travelDirection = direction;
    while (distance > 0) {
      Set<Move> nextMoves = arrowCurrentLocation.getNextMoves();
      if (nextMoves.contains(travelDirection)) {
        arrowCurrentLocation = getNextLocation(arrowCurrentLocation, travelDirection);
      } else {
        if (!arrowCurrentLocation.isCave()) {
          travelDirection = nextMoves.iterator().next();
          arrowCurrentLocation = getNextLocation(arrowCurrentLocation, travelDirection);
        } else {
          distance = 0;
        }
      }
      if (arrowCurrentLocation.isCave()) {
        distance--;
      }
    }
    if (arrowCurrentLocation.hasMonster() && distance == 0) {
      arrowCurrentLocation.hitMonster();
      if (arrowCurrentLocation.getMonster().isDead()) {
        hit = ArrowHitOutcome.KILLED;
      }
      else if (arrowCurrentLocation.getMonster().isInjured()) {
        hit = ArrowHitOutcome.INJURED;
      }
    }
    System.out.println("Arrow destination " + arrowCurrentLocation.getRow() + ", " + arrowCurrentLocation.getColumn());
    return hit;
  }

  private List<LocationPrivate> getAllCaves() {
    List<LocationPrivate> allNodes = new ArrayList<>();
    for (List<LocationPrivate> yList : maze) {
      for (LocationPrivate location : yList) {
        if (location.isCave()) {
          allNodes.add(location);
        }
      }
    }
    return allNodes;
  }

  private List<LocationPrivate> getAllLocations() {
    List<LocationPrivate> allNodes = new ArrayList<>();
    for (List<LocationPrivate> yList : maze) {
      allNodes.addAll(yList);
    }
    return allNodes;
  }

  private void validator(int rows, int columns, boolean wrapped,
                         int interconnectivity, int treasurePercentage, int numberOfMonsters,
                         Randomizer randomizer) throws IllegalArgumentException {
    if (randomizer == null) {
      throw new IllegalArgumentException("Randomizer cannot be null");
    }
    if (!(rows >= 5 && columns >= 4)) {
      throw new IllegalArgumentException("Maze dimensions cannot be less than 5x4");
    }
    int interconnectivityLimit;
    int totalEdges = (rows * (columns - 1)) + ((rows - 1) * columns);
    if (wrapped) {
      totalEdges += rows + columns;
    }
    int mstEdges = (rows * columns) - 1;
    interconnectivityLimit = totalEdges - mstEdges;
    if (interconnectivity < 0 || interconnectivity > interconnectivityLimit) {
      throw new IllegalArgumentException("Interconnectivity must be between 0 and "
              + interconnectivityLimit);
    }
    if (treasurePercentage < 0 || treasurePercentage > 100) {
      throw new IllegalArgumentException(
              "Treasure percentage cannot be negative or greater than 100");
    }
    if (numberOfMonsters < 0) {
      throw new IllegalArgumentException("Number of monsters cannot be negative");
    }
  }

  private void connectInterconnectivity(int interconnectivity, List<Edge> skippedEdges) {
    if (interconnectivity > 0) {
      for (int i = 0; i < interconnectivity; i++) {
        int index = randomizer.getRandomValue(0, skippedEdges.size() - 1);
        Edge edge = skippedEdges.remove(index);
        int x = edge.getX1();
        int y = edge.getY1();
        int x2 = edge.getX2();
        int y2 = edge.getY2();
        LocationPrivate locationA = getLocation(x, y);
        LocationPrivate locationB = getLocation(x2, y2);
        Move move = edge.getMove();
        locationA.setNextMove(move);
        locationB.setNextMove(move.getOpposite());
      }
    }
  }

  private void connectMazeEdges(Randomizer randomizer, Stack<Edge> edges,
                                List<Edge> skippedEdges, List<Set<LocationPrivate>> listOfSets) {
    while (edges.size() > 0) {
      int index = randomizer.getRandomValue(0, edges.size() - 1);
      Edge edge = edges.remove(index);
      int x = edge.getX1();
      int y = edge.getY1();
      int x2 = edge.getX2();
      int y2 = edge.getY2();
      LocationPrivate locationA = getLocation(x, y);
      LocationPrivate locationB = getLocation(x2, y2);
      Move move = edge.getMove();
      Set<LocationPrivate> setA = getSetContainingNode(listOfSets, locationA);
      if (setA == null) {
        setA = new HashSet<>();
        setA.add(locationA);
        listOfSets.add(setA);
      }
      Set<LocationPrivate> setB = getSetContainingNode(listOfSets, locationB);
      if (setB == null) {
        setB = new HashSet<>();
        setB.add(locationB);
        listOfSets.add(setB);
      }
      if (!Collections.disjoint(setA, setB)) {
        skippedEdges.add(edge);
      } else {
        setA.addAll(setB);
        listOfSets.remove(setB);
        locationA.setNextMove(move);
        locationB.setNextMove(move.getOpposite());
      }
    }
  }

  private Stack<Edge> generateEdges(int rows, int columns, boolean wrapped) {
    Stack<Edge> edges = new Stack<>();
    for (int x = 0; x < rows; x++) {
      maze.add(x, new ArrayList<>());
      for (int y = 0; y < columns; y++) {
        List<LocationPrivate> yList = maze.get(x);
        yList.add(y, new LocationNode(x, y));
        if (x != 0 || wrapped) {
          int x2 = (x == 0) ? rows - 1 : x - 1;
          int y2 = y;
          edges.add(new Edge(x, y, x2, y2, Move.NORTH));
        }
        if (y != columns - 1 || wrapped) {
          int x2 = x;
          int y2 = (y == columns - 1) ? 0 : y + 1;
          edges.add(new Edge(x, y, x2, y2, Move.WEST));
        }
      }
    }
    return edges;
  }

  private Set<LocationPrivate> getSetContainingNode(
          List<Set<LocationPrivate>> listOfSets, LocationPrivate locationA) {
    for (Set<LocationPrivate> set : listOfSets) {
      if (set.contains(locationA)) {
        return set;
      }
    }
    return null;
  }

  private int getSmellHelper(Location location) {
    int smell = 0;
    Set<Move> nextMoves = location.getNextMoves();
    int count2PositionMonsters = 0;
    for (Move move : nextMoves) {
      Location location1 = getNextLocation(location, move);
      if (location1.hasMonster()) {
        smell = 2;
      }
      Set<Move> nextMovesLevel2 = location1.getNextMoves();
      for (Move nextMoveLeve2 : nextMovesLevel2) {
        Location location2 = getNextLocation(location1, nextMoveLeve2);
        if (location2.hasMonster()) {
          count2PositionMonsters++;
          if (smell < 2 && count2PositionMonsters > 1) {
            smell = 2;
          }
          if (smell != 2) {
            smell = 1;
          }
        }
      }
    }
    return smell;
  }

  private void buildDungeon(int rows, int columns, boolean wrapped,
                            int interconnectivity, int treasurePercentage, int numberOfMonsters,
                            Randomizer randomizer) {
    Stack<Edge> edges = generateEdges(rows, columns, wrapped);
    List<Edge> skippedEdges = new ArrayList<>();
    List<Set<LocationPrivate>> listOfSets = new ArrayList<>();
    connectMazeEdges(randomizer, edges, skippedEdges, listOfSets);
    connectInterconnectivity(interconnectivity, skippedEdges);
    int numberOfCaves = getAllCaves().size();
    if (numberOfMonsters > numberOfCaves) {
      throw new IllegalArgumentException("Number of monsters is greater than number of caves: "
              + numberOfCaves);
    }
    fillUpTreasure(treasurePercentage, randomizer);
    fillUpArrows(treasurePercentage, randomizer);
    generateStartEndNodes(randomizer);
    fillUpMonsters(numberOfMonsters,randomizer);
  }
}
