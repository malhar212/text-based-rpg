package dungeon;


import randomizer.Randomizer;

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

  /**
   * Creates an instance of DungeonModel.
   *
   * @param rows               the number of rows in the dungeon maze.
   * @param columns            the number of columns in the dungeon maze.
   * @param wrapped            if the maze is wrapped around edges or not.
   * @param interconnectivity  the interconnectivity.
   * @param treasurePercentage the percentage of caves that have treasure.
   *                           If percentage of nodes is decimal it
   *                           takes the lower bound number of nodes.
   * @param randomizer         the game randomizer to use.
   * @throws IllegalArgumentException if maze dimensions are less than 3x2.
   * @throws IllegalArgumentException if randomizer is null.
   * @throws IllegalArgumentException if treasure percentage is negative or greater than 100.
   * @throws IllegalArgumentException if interconnectivity is not between 0
   *                                  and maximum possible interconnectivity.
   */
  public DungeonModel(int rows, int columns, boolean wrapped,
                      int interconnectivity, int treasurePercentage,
                      Randomizer randomizer) throws IllegalArgumentException {
    validator(rows, columns, wrapped, interconnectivity, treasurePercentage, randomizer);
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
    buildDungeon(rows, columns, wrapped, interconnectivity, treasurePercentage, randomizer);
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
  public Set<Move> getAvailableMoves() {
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
   * Moves the player in the maze in the provided Move direction.
   *
   * @param move {@link Move} to be executed.
   * @throws IllegalArgumentException if provided move is not a valid move.
   */
  @Override
  public void movePlayer(Move move) throws IllegalArgumentException {
    if (!validateNextMove(move)) {
      throw new IllegalArgumentException("Provided move is not a valid move " + move);
    }
    List<Integer> nextLocation = getNextLocation(move);
    currentX = nextLocation.get(0);
    currentY = nextLocation.get(1);
    this.playerVisitedEnd = currentX == endX && currentY == endY;
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
   * @return player in the maze as a {@link Player}.
   */
  @Override
  public Player getPlayerDescription() {
    return player;
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
          List<Integer> nextLocationCoords = getNextLocation(move);
          Location childLocation = getLocation(nextLocationCoords.get(0),
                  nextLocationCoords.get(1));
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
      return false;
    }
    final Set<Move> nextMoves = getLocation(currentX, currentY).getNextMoves();
    return nextMoves.contains(move);
  }

  private List<Integer> getNextLocation(Move move) throws IllegalArgumentException {
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

  private List<LocationPrivate> getAllCaves() {
    List<LocationPrivate> allNodes = new ArrayList<>();
    for (List<LocationPrivate> yList : maze) {
      for (LocationPrivate location : yList) {
        if (location.getNextMoves().size() != 2) {
          allNodes.add(location);
        }
      }
    }
    return allNodes;
  }

  private void validator(int rows, int columns, boolean wrapped,
                         int interconnectivity, int treasurePercentage,
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

  private void buildDungeon(int rows, int columns, boolean wrapped,
                            int interconnectivity, int treasurePercentage,
                            Randomizer randomizer) {
    Stack<Edge> edges = generateEdges(rows, columns, wrapped);
    List<Edge> skippedEdges = new ArrayList<>();
    List<Set<LocationPrivate>> listOfSets = new ArrayList<>();
    connectMazeEdges(randomizer, edges, skippedEdges, listOfSets);
    connectInterconnectivity(interconnectivity, skippedEdges);
    fillUpTreasure(treasurePercentage, randomizer);
    generateStartEndNodes(randomizer);
  }
}
