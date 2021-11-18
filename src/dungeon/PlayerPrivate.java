package dungeon;

//package-private interface
//Provided mutation methods for PlayerModel
interface PlayerPrivate extends Player {

  void addToTreasure(Treasure treasure, int randomTreasureQuantity) throws IllegalArgumentException;

  void fireArrow();

  void pickArrows(int arrows);
}
