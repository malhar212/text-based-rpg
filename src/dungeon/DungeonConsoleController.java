package dungeon;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Represents a controller that allows user ability to manipulate the player in the dungeon game.
 */
public class DungeonConsoleController implements DungeonController {

  private final Readable readable;
  private final Appendable appendable;

  /**
   * Creates a new instance of DungeonConsoleController.
   *
   * @param readable     the input readable.
   * @param appendable   the output appendable.
   * @throws IllegalArgumentException if any of the given parameters are null.
   */
  public DungeonConsoleController(Readable readable, Appendable appendable)
          throws IllegalArgumentException {
    if (readable == null || appendable == null) {
      throw new IllegalArgumentException("Please provide valid parameters to the controller");
    }
    this.readable = readable;
    this.appendable = appendable;
  }

  private static boolean compareLocations(Location locationA, Location locationB) {
    return locationA.getRow() == locationB.getRow()
            && locationA.getColumn() == locationB.getColumn();
  }

  /**
   * Starts the run of a dungeon game.
   *
   * @param model the model of the dungeon game.
   * @throws IOException if an I/O error occurs.
   * @throws IllegalArgumentException if the given model is null.
   */
  @Override
  public void play(Dungeon model) throws IOException, IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("Please provide valid model");
    }
    Scanner scanner = new Scanner(readable);
    appendable.append("Welcome to the dungeons");
    appendable.append("\nYou can input N for North, E for East, S for South, W for West");
    while (!model.isGameOver()) {
      Location playerCurrentLocation = model.getPlayerCurrentLocation();
      Set<Commands> commandsList = new LinkedHashSet<>();
      commandsList.add(Commands.MOVE);
      if (model.getPlayerDescription().hasArrows()) {
        commandsList.add(Commands.SHOOT);
      }
      if (playerCurrentLocation.isCave()) {
        appendable.append("\nYou are in a cave,");
      } else {
        appendable.append("\nYou are in a tunnel,");
      }
      SmellLevel smell = model.getSmell(playerCurrentLocation);
      switch (smell) {
        case LESS: {
          appendable.append("\nThere is a rancid smell somewhere near");
          break;
        }
        case MORE: {
          appendable.append("\nThere is a very strong rancid smell somewhere near");
          break;
        }
        default: {
          //do nothing
          appendable.append("");
        }
      }
      if (playerCurrentLocation.hasTreasure()) {
        appendable.append("\nThere is treasure here");
        Map<Treasure, Integer> treasure = playerCurrentLocation.getTreasure();
        appendable.append("\nYou find");
        printTreasures(treasure);
        commandsList.add(Commands.PICKUP);
      }
      if (playerCurrentLocation.hasArrows()) {
        int arrows = playerCurrentLocation.getArrows();
        appendable.append("\nThere ");
        if (arrows > 1) {
          appendable.append("are ").append(String.valueOf(arrows)).append(" arrows here\n");
        } else {
          appendable.append("is an arrow here\n");
        }
        commandsList.add(Commands.PICKUP);
      }
      Set<Move> nextMoves = playerCurrentLocation.getNextMoves();
      appendable.append("\nYou can move in\n");
      for (Move move : nextMoves) {
        appendable.append(move.getFullForm()).append(": ").append(move.getShortForm()).append(
                "\n");
      }
      Commands commands = null;
      commandsList.add(Commands.QUIT);
      while (commands != Commands.MOVE) {
        appendable.append("\nWhat do you want to do?");
        printCommandList(commandsList);
        String commandInput = scanner.next();
        try {
          commands = Commands.getByShortHand(commandInput);
          if (!commandsList.contains(commands)) {
            commands = null;
          }
          if (commands != null) {
            switch (commands) {
              case MOVE: {
                Move direction = null;
                while (direction == null) {
                  appendable.append("\nWhere do we go?\n");
                  String nextMove = scanner.next();
                  direction = getDirection(nextMove);
                }
                model.movePlayer(direction);
                Location newPlayerLocation = model.getPlayerCurrentLocation();
                if (newPlayerLocation.hasMonster() && !model.isPlayerDead()) {
                  appendable.append("\nThere is an injured Otyugh resting. You have miraculously "
                          + "survived!!");
                }
                break;
              }
              case PICKUP: {
                Commands itemCommand = null;
                while (itemCommand == null) {
                  appendable.append("\nWhat to pick? Enter A for arrows or T for treasure\n");
                  String nextCommand = scanner.next();
                  itemCommand = Commands.getByShortHand(nextCommand);
                  if (!itemCommand.equals(Commands.PICKARROWS)
                          && !itemCommand.equals(Commands.PICKTREASURE)) {
                    itemCommand = null;
                  }
                }
                switch (itemCommand) {
                  case PICKARROWS: {
                    if (playerCurrentLocation.hasArrows()) {
                      try {
                        int arrows = playerCurrentLocation.getArrows();
                        model.playerPickArrows();
                        appendable.append("\nYou have picked up ").append(String.valueOf(arrows));
                        if (arrows > 1) {
                          appendable.append(" arrows");
                        } else {
                          appendable.append(" arrow");
                        }
                      } catch (IllegalStateException ise) {
                        appendable.append("\nNo arrows to pick");
                      }
                    } else {
                      appendable.append("\nNo arrows to pick");
                    }
                    break;
                  }
                  case PICKTREASURE: {
                    if (playerCurrentLocation.hasTreasure()) {
                      try {
                        Map<Treasure, Integer> treasure = playerCurrentLocation.getTreasure();
                        model.playerPickTreasure();
                        appendable.append("\nYou picked up");
                        printTreasures(treasure);
                      } catch (IllegalStateException ise) {
                        appendable.append("\nNo treasure to pick");
                      }
                    } else {
                      appendable.append("\nNo treasure to pick");
                    }
                    break;
                  }
                  default: {
                    appendable.append("Invalid choice to pick");
                  }
                }
                printPlayerDescription(model.getPlayerDescription());
                break;
              }
              case SHOOT: {
                if (model.getPlayerDescription().hasArrows()) {
                  Move direction = null;
                  while (direction == null) {
                    appendable.append("\nWhere do you want to shoot?\n");
                    String nextMove = scanner.next();
                    direction = getDirection(nextMove);
                  }
                  Integer arrowDistance = null;
                  ArrowHitOutcome arrowHit = ArrowHitOutcome.MISS;
                  while (arrowDistance == null) {
                    try {
                      appendable.append("\nHow far do you want to shoot? (1-5)\n");
                      arrowDistance = Integer.parseInt(scanner.next());
                      arrowHit = model.shootArrow(direction, arrowDistance);
                    } catch (NumberFormatException ime) {
                      appendable.append("\nPlease enter a valid distance as an integer\n");
                    } catch (IllegalArgumentException iae) {
                      appendable.append("\nPlease enter a valid distance between 1-5 as an "
                              + "input\n");
                      arrowDistance = null;
                    }
                  }
                  switch (arrowHit) {
                    case MISS: {
                      appendable
                              .append("\nYour arrow goes whistling through the dungeon "
                                      + "and there's a clunk "
                                      + "as it falls to the ground after hitting a cave wall");
                      break;
                    }
                    case INJURED: {
                      appendable
                              .append("\nYou hear a painful roar in the distance. "
                                      + "It seems your arrow hit an Otyugh");
                      break;
                    }
                    case KILLED: {
                      appendable
                              .append("\nYou hear a painful roar and wild thrashing in the "
                                      + "darkness and then silence. "
                                      + "It seems you've killed an Otyugh");
                      break;
                    }
                    default: {
                      appendable
                              .append("\nYour arrow goes whistling through the dungeon "
                                      + "and there's a clunk "
                                      + "as it falls to the ground after hitting a cave wall");
                    }
                  }
                  int remainingArrows = model.getPlayerDescription().getArrows();
                  if (remainingArrows == 0) {
                    appendable.append("\nYou have no arrows remaining");
                  } else if (remainingArrows == 1) {
                    appendable.append("\nYou have ").append(String.valueOf(remainingArrows))
                            .append(" arrow left");
                  } else {
                    appendable.append("\nYou have ").append(String.valueOf(remainingArrows))
                            .append(" arrows left");
                  }
                  break;
                }
                appendable.append("\nYou have no arrows to shoot");
                break;
              }
              case QUIT: {
                System.exit(0);
                break;
              }
              default: {
                appendable.append("\nYour commands is invalid");
              }
            }
          }
        } catch (IllegalArgumentException iae) {
          //do nothing
          appendable.append("\nPlease choose one of the valid commands");
          printCommandList(commandsList);
        }
      }
      //visualizeKruskals(model);
    }
    if (model.isPlayerDead()) {
      appendable.append("\nYou were killed. You died a gruesome death at the hands of the Otyugh");
    }
    else if (model.playerVisitedEnd()) {
      appendable.append("\nYou have escaped the mines of Moria");
      Player playerDescription = model.getPlayerDescription();
      if (playerDescription.hasTreasure()) {
        appendable.append("\nYou collected these treasures on your journey");
        Map<Treasure, Integer> treasures = playerDescription.getTreasure();
        for (Treasure treasure : treasures.keySet()) {
          appendable.append(" ").append(treasure.name()).append(": ")
                  .append(String.valueOf(treasures.get(treasure)));
        }
      }
    }
  }

  private void printTreasures(Map<Treasure, Integer> treasure) throws IOException {
    for (Treasure treasureItem : treasure.keySet()) {
      Integer count = treasure.get(treasureItem);
      appendable.append(" ").append(String.valueOf(count)).append(" ")
              .append(getTreasureString(treasureItem, count));
    }
  }

  private void printCommandList(Set<Commands> commandsList) throws IOException {
    for (Commands loopCommand : commandsList) {
      appendable.append(" ").append(loopCommand.name()).append(": ")
              .append(loopCommand.getShortHand());
    }
    appendable.append("\n");
  }

  private Move getDirection(String nextMove) throws IOException {
    nextMove = nextMove.toUpperCase();
    Move direction = null;
    switch (nextMove) {
      case "N": {
        direction = Move.NORTH;
        break;
      }
      case "S": {
        direction = Move.SOUTH;
        break;
      }
      case "E": {
        direction = Move.EAST;
        break;
      }
      case "W": {
        direction = Move.WEST;
        break;
      }
      default: {
        appendable.append(
                "Please enter a valid direction. You can input N for North, E for East, S for"
                        + " South, W for West\n");
      }
    }
    return direction;
  }

  private String getTreasureString(Treasure treasureItem, Integer count) {
    switch (treasureItem) {
      case RUBIES: {
        if (count > 1) {
          return "rubies";
        } else {
          return "ruby";
        }
      }
      case DIAMONDS: {
        if (count > 1) {
          return "diamonds";
        } else {
          return "diamond";
        }
      }
      case SAPPHIRES: {
        if (count > 1) {
          return "sapphires";
        } else {
          return "sapphire";
        }
      }
      default: {
        return "";
      }
    }

  }

  private void visualizeKruskals(Dungeon dungeon) throws IOException {
    List<List<Location>> maze = dungeon.getMaze();
    Location startLocation = dungeon.getStartLocation();
    Location endLocation = dungeon.getEndLocation();
    Location playerCurrentLocation = dungeon.getPlayerCurrentLocation();
    Appendable sb = appendable;
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
              sb.append(String.valueOf(0));
              break;
            case LESS:
              sb.append(String.valueOf(1));
              break;
            case MORE:
              sb.append(String.valueOf(2));
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
  }

  private void printPlayerDescription(Player player) throws IOException {
    if (player.hasTreasure()) {
      Map<Treasure, Integer> treasure = player.getTreasure();
      appendable.append("\nYou now have the following treasure");
      for (Treasure treasureItem : treasure.keySet()) {
        Integer count = treasure.get(treasureItem);
        appendable.append(" ").append(String.valueOf(count)).append(" ")
                .append(getTreasureString(treasureItem, count));
      }
    } else {
      appendable.append("\nPlayer has no treasure");
    }
    if (player.hasArrows()) {
      int arrows = player.getArrows();
      if (arrows > 1) {
        appendable.append("\nYou have ").append(String.valueOf(arrows)).append(" arrows left\n");
      } else {
        appendable.append("\nYou have 1 arrow left\n");
      }
    } else {
      appendable.append("\nPlayer has no arrows");
    }
  }

}
