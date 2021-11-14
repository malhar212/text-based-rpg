package dungeon;

//package-private interface.
//Provided mutation methods for LocationNode internally to the package.
interface LocationPrivate extends Location {

  void setNextMove(Move move)  throws IllegalArgumentException;

  void setTreasure(Treasure treasure, int randomTreasureQuantity);

  void pickTreasure(Treasure treasure, int randomTreasureQuantity);
}
