import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;

import dungeon.Dungeon;
import dungeon.DungeonConsoleController;
import dungeon.DungeonController;
import dungeon.DungeonModel;
import dungeon.Location;
import dungeon.Move;
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
    if (args.length < 6) {
      System.out.println("Please enter all parameters");
    }
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
    boolean wrapped = Boolean.parseBoolean(args[2]);
    Randomizer randomizer = new GameRandomizer();
    Dungeon dungeon = new DungeonModel(
            rows, columns, wrapped, interconnectivity, treasurePercentage, numberOfMonsters,
            randomizer);
    DungeonController controller = new DungeonConsoleController(
            new InputStreamReader(System.in), System.out);
    controller.play(dungeon);
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
}
