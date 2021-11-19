package dungeon;

import java.io.IOException;

/**
 * This serves as the controller for the player in the dungeon game.
 */
public interface DungeonController {

  /**
   * Starts the run of a dungeon game.
   *
   * @param model the model of the dungeon game.
   * @throws IOException if an I/O error occurs.
   * @throws IllegalArgumentException if the given model is null.
   */
  //TODO model field or not/update diagram/Update readme
  void play(Dungeon model) throws IOException, IllegalArgumentException;
}
