package dungeon;

//package-private
final class Edge {
  private final int x1;
  private final int y1;
  private final int x2;
  private final int y2;
  private final Move move;

  public int getX1() {
    return x1;
  }

  public int getY1() {
    return y1;
  }

  public int getX2() {
    return x2;
  }

  public int getY2() {
    return y2;
  }



  public Move getMove() {
    return move;
  }

  public Edge(int x, int y, int x2, int y2, Move move) {
    this.x1 = x;
    this.y1 = y;
    this.x2 = x2;
    this.y2 = y2;
    this.move = move;
  }
}
