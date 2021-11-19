import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

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

/**
 * Driver class that acts as controller for the Dungeon.
 */
public class Driver {

  /**
   * Driver method to run the application.
   *
   * @param args Arguments to the main method.
   */
  public static void main(String[] args) throws IOException {
    int rows = 0;
    int columns = 0;
    int interconnectivity = 0;
    int treasurePercentage = 0;
    int numberOfMonsters = 0;
    try {
      rows = Integer.parseInt(args[0]);
      columns = Integer.parseInt(args[1]);
      interconnectivity = Integer.parseInt(args[3]);
      treasurePercentage = Integer.parseInt(args[4]);
      numberOfMonsters = Integer.parseInt(args[5]);
    } catch (NumberFormatException e) {
      System.out.println(
              "Rows, Columns, Interconnectivity, Treasure Percentage, Number of monsters have to "
                      + "be Integers");
      System.exit(0);
    }
    if (!(args[2].equalsIgnoreCase("true")
            || args[2].equalsIgnoreCase("false"))) {
      System.out.println(
              "Wrapped has to be true or false");
      System.exit(0);
    }
/*    boolean traverseAllNodes = false;
    if (args.length > 6) {
      if (!(args[6].equalsIgnoreCase("true")
              || args[6].equalsIgnoreCase("false"))) {
        System.out.println(
                "Traverse all node has to be true or false");
        System.exit(0);
      }
      traverseAllNodes = Boolean.parseBoolean(args[6]);
    }*/
    boolean wrapped = Boolean.parseBoolean(args[2]);
    Randomizer randomizer = new GameRandomizer(28, 33, 23, 29, 6, 23, 27, 24, 27, 7, 14, 4, 7, 2, 14, 23, 1, 11, 5, 16, 5, 3, 14, 5, 8, 13, 10, 11, 11, 8, 6, 2, 5, 1, 3, 4, 3, 2, 0, 0, 4, 8, 4, 6, 12, 1, 0, 5, 2, 3, 1, 2, 0, 3, 0, 4, 9, 2, 0, 5, 0, 5, 7, 1, 1, 3, 1, 1, 1, 4, 4, 1, 1, 5, 5, 1, 1, 4, 3, 3, 9, 1, 13, 2, 5, 2, 4, 2, 13, 3, 7, 1, 11, 3, 4, 3, 5, 3, 3, 1, 11, 10, 0);
    Dungeon dungeon = new DungeonModel(
            rows, columns, wrapped, interconnectivity, treasurePercentage, numberOfMonsters,
            randomizer);
    //Boolean[][] visitedGrid = initiateVisitGrid(rows, columns);
    //int caves = getAllCaves(dungeon.getMaze()).size();
    //System.out.println("Number of caves: " + caves);
    //System.out.println("Number of tunnels: " + getNumberOfTunnels(dungeon.getMaze()));
    //double percentage = treasurePercentage / 100.00;
    /*int numberOfNodesToFillTreasure = Double.valueOf(
            Math.round(percentage * caves)).intValue();
    if (numberOfNodesToFillTreasure == 0) {
      numberOfNodesToFillTreasure = 1;
    }*/
    //System.out.println("Treasure filled caves: " + numberOfNodesToFillTreasure);
    /*System.out.println(printLegend());*/
    System.out.println(visualizeKruskals(dungeon));
    DungeonController controller = new DungeonConsoleController(
            new InputStreamReader(System.in), System.out);
    try {
      controller.play(dungeon);
    }
    catch (IOException ioe) {
      System.out.println(ioe.getMessage());
    }
  }

  private static String printLegend() {
    StringBuilder sb = new StringBuilder("\nDungeon Legend")
            .append("\nStart Location: S, End Location: X, Player Location: P")
            .append("\nStart Location and Player Location same: $")
            .append("\nEnd Location and Player Location same: *")
            .append("\nLocation has treasure: ▲ , Location Doesn't have treasure: 0");
    return sb.toString();
  }

  private static void traverseAllNodes(Boolean[][] visitedGrid, Dungeon dungeon) {
    while (!fullyTraversed(visitedGrid)) {
      System.out.println("\n\n");
      System.out.println(visualizeKruskals(dungeon));
      Location playerCurrentLocation = dungeon.getPlayerCurrentLocation();
      System.out.println("\n\nPlayer Current Location "
              + locationNodeSummary(playerCurrentLocation));
      int row = playerCurrentLocation.getRow();
      int column = playerCurrentLocation.getColumn();
      visitedGrid[row][column] = true;
      if (playerCurrentLocation.hasTreasure()) {
        dungeon.playerPickTreasure();
        System.out.println("Picking cave treasure");
      }
      List<Move> availableMoves = new ArrayList<>(dungeon.getAvailableDirections());
      Collections.shuffle(availableMoves);
      goNextMove(dungeon, playerCurrentLocation, availableMoves);
      System.out.println("New Location " + locationNodeSummary(dungeon.getPlayerCurrentLocation()));
      System.out.println("Player Description " + playerDescription(dungeon.getPlayerDescription()));
    }
  }

  private static void findEndNode(Dungeon dungeon) {
    while (!compareLocations(dungeon.getPlayerCurrentLocation(), dungeon.getEndLocation())) {
      Location playerCurrentLocation = dungeon.getPlayerCurrentLocation();
      System.out.println("\n\n");
      System.out.println(printLegend());
      System.out.println(visualizeKruskals(dungeon));
      System.out.println("Player Current Location " + locationNodeSummary(playerCurrentLocation));
      if (playerCurrentLocation.hasTreasure()) {
        dungeon.playerPickTreasure();
        System.out.println("Picking cave treasure");
      }
      List<Move> availableMoves = new ArrayList<>(dungeon.getAvailableDirections());
      Collections.shuffle(availableMoves);
      goNextMove(dungeon, playerCurrentLocation, availableMoves);
      System.out.println("New Location " + locationNodeSummary(dungeon.getPlayerCurrentLocation()));
      System.out.println("Player Description " + playerDescription(dungeon.getPlayerDescription()));
      if (compareLocations(dungeon.getPlayerCurrentLocation(), dungeon.getEndLocation())) {
        if (playerCurrentLocation.hasTreasure()) {
          dungeon.playerPickTreasure();
          System.out.println("Picking cave treasure");
        }
      }
    }
  }

  private static String playerDescription(Player playerDescription) {
    StringBuilder sb = new StringBuilder();
    if (playerDescription.hasTreasure()) {
      Map<Treasure, Integer> playerTreasure = playerDescription.getTreasure();
      for (Treasure treasure : playerTreasure.keySet()) {
        Integer quantity = playerTreasure.get(treasure);
        sb.append(", ").append(treasure).append(": ").append(quantity);
      }
    } else {
      sb.append("Player has no treasure");
    }
    return sb.toString();
  }

  private static void goNextMove(Dungeon dungeon, Location playerCurrentLocation,
                                 List<Move> availableMoves) {
    for (Move move : availableMoves) {
      List<Integer> nextLocationRowsColumns = getNextLocation(dungeon.getPlayerCurrentLocation(),
              move, dungeon);
      Location nextLocation = getLocation(nextLocationRowsColumns.get(0),
              nextLocationRowsColumns.get(1), dungeon);
      if (!compareLocations(nextLocation, playerCurrentLocation)) {
        dungeon.movePlayer(move);
        System.out.println("Moving " + move);
        break;
      }
    }
  }

  private static String locationNodeSummary(Location location) {
    StringBuilder sb = new StringBuilder();
    Set<Move> locationNextMoves = location.getNextMoves();
    sb.append("LocationNode : ").append("Grid Location row,column: ").append(location.getRow())
            .append(",").append(location.getColumn());
    if (locationNextMoves.size() != 2) {
      sb.append(". Location Type: Cave");
    } else {
      sb.append(". Location Type: Tunnel");
    }
    sb.append(", available moves: ").append(locationNextMoves)
            .append(", treasure: ").append(location.getTreasure());
    return sb.toString();
  }

  private static StringBuilder visualizeVisitedGrid(Boolean[][] visitedGrid) {
    StringBuilder sb = new StringBuilder("\nVisited Nodes are marked by true");
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
        if (compareLocations(startLocation, locationNode)
                && compareLocations(playerCurrentLocation, locationNode)) {
          sb.append("$");
        } else if (compareLocations(startLocation, locationNode)) {
          sb.append("S");
        } else if (compareLocations(endLocation, locationNode)
                && compareLocations(playerCurrentLocation, locationNode)) {
          if (locationNode.hasMonster()) {
            sb.append("M");
          } else {
            sb.append("*");
          }
        } else if (compareLocations(playerCurrentLocation, locationNode)) {
          sb.append("P");
        } else if (compareLocations(endLocation, locationNode)) {
          sb.append("X");
        }
        /*if (locationNode.hasTreasure()) {
          sb.append("▲");
        }*/
        if (locationNode.hasMonster()) {
          sb.append("M");
        } else {
          switch (dungeon.getSmell(locationNode)) {
            case NONE:
              sb.append(0);
              break;
            case LESS:
              sb.append(1);
              break;
            case MORE:
              sb.append(2);
              break;
            default:
              sb.append("N");
          }
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

  private static Location getLocation(int x, int y, Dungeon dungeon) {
    return dungeon.getMaze().get(x).get(y);
  }

  private static List<Integer> getNextLocation(Location location, Move move, Dungeon dungeon)
          throws IllegalArgumentException, IllegalStateException {
    int currentX = location.getRow();
    int currentY = location.getColumn();
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

  private static int getNumberOfTunnels(List<List<Location>> maze) {
    return maze.size() * maze.get(0).size() - getAllCaves(maze).size();
  }
/*
  private static int getSmell(Location location, Dungeon dungeon) {
    System.out.println(location.getRow() + "," + location.getColumn());
    int smell = 0;
    Set<Move> nextMoves = location.getNextMoves();
    System.out.println(nextMoves);
    int count2PositionMonsters = 0;
    for (Move move : nextMoves) {
      List<Integer> nextLocationInts = getNextLocation(location, move, dungeon);
      Location location1 = getLocation(nextLocationInts.get(0), nextLocationInts.get(1), dungeon);
      System.out.println("Level 1 neighbors " + location1.getRow() + location1.getColumn());
      System.out.println(location1.hasMonster());
      if (location1.hasMonster()) {
        System.out.println(count2PositionMonsters);
        smell = 2;
      }
      System.out.println(smell);
      Set<Move> nextMovesLevel2 = location1.getNextMoves();
      for (Move nextMoveLeve2 : nextMovesLevel2) {
        List<Integer> nextLocation2PositionInts = getNextLocation(location1, nextMoveLeve2,
                dungeon);
        Location location2 = getLocation(nextLocation2PositionInts.get(0),
                nextLocation2PositionInts.get(1), dungeon);
        System.out.println("Level 2 neighbors " + location2.getRow() + location1.getColumn());
        System.out.println(location2.hasMonster());
        if (location2.hasMonster()) {
          count2PositionMonsters++;
          if (smell < 2 && count2PositionMonsters > 1) {
            smell = 2;
          }
          if (smell != 2) {
            smell = 1;
          }
          System.out.println(count2PositionMonsters);
          System.out.println(smell);
        }
      }
    }
    System.out.println("HERRR " + smell);
    return smell;
  }

  private static boolean shootArrow(Move direction, int arrowDistance, Dungeon dungeon)
          throws IllegalArgumentException {
    if (arrowDistance <= 0 || arrowDistance > 5) {
      throw new IllegalArgumentException("Distance cannot be less than 1 or greater than 5");
    }
    boolean hit = false;
    int distance = arrowDistance;
    Location arrowCurrentLocation = dungeon.getPlayerCurrentLocation();
    System.out.println("Start Arrow loaction " + arrowCurrentLocation.getRow() + ", "
            + arrowCurrentLocation.getColumn());
    System.out.println(arrowCurrentLocation.getNextMoves());
    //TODO validate move throw exception using existing function
    Move travelDirection = direction;
    while (distance > 0) {
      System.out.println("Distance " + distance);
      System.out.println("Direction " + travelDirection);
      System.out.println("Current Arrow loaction " + arrowCurrentLocation.getRow() + ", "
              + arrowCurrentLocation.getColumn());
      Set<Move> nextMoves = arrowCurrentLocation.getNextMoves();
      System.out.println(nextMoves);
      System.out.println("Direction contains " + nextMoves.contains(travelDirection));
      if (nextMoves.contains(travelDirection)) {
        List<Integer> arrowNextLocation =
                getNextLocation(arrowCurrentLocation, travelDirection, dungeon);
        System.out.println(arrowNextLocation);
        arrowCurrentLocation =
                getLocation(arrowNextLocation.get(0), arrowNextLocation.get(1), dungeon);
        System.out.println("NExt moves " + nextMoves.size());
      } else {
        if (nextMoves.size() == 2) {
          System.out.println("Remove successful " + nextMoves.remove(travelDirection.getOpposite()));
          travelDirection = nextMoves.iterator().next();
          System.out.println("directionupdate " + travelDirection);
          List<Integer> arrowNextLocation =
                  getNextLocation(arrowCurrentLocation, travelDirection, dungeon);
          arrowCurrentLocation =
                  getLocation(arrowNextLocation.get(0), arrowNextLocation.get(1), dungeon);
        } else {
          distance = 0;
        }
      }
      if (arrowCurrentLocation.getNextMoves().size() != 2) {
        distance--;
      }
      System.out.println("Distance left " + distance);
    }
    if (arrowCurrentLocation.hasMonster() && distance == 0) {
      hit = true;
      //TODO hit monster
    }
    System.out.println("Arrow loaction " + arrowCurrentLocation.getRow() + ", "
            + arrowCurrentLocation.getColumn());
    return hit;
  }*/
}
