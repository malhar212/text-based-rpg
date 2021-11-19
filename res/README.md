#Dungeons

##Overview
Dungeons is dungeon game that creates a maze of caves and tunnels and starts off a player at the start cave.\
Player can navigate through the maze collecting any treasures in the caves along the way or picking up arrows.\
Player can shoot the arrows to kill monsters they encounter along the way\
There will always be a monster at the end cave.

##List of Features
1. Player can collect treasure.
2. Player can collect arrows.
3. Players can move in NORTH, SOUTH, EAST, WEST direction if the cave has those directions open.
4. Player can shoot an arrow in the directions that are available from the current location.
5. Player can kill a monster(Otyugh).

##How to run
1. Make sure java is installed on the system.
2. Navigate to the directory where the jar file is stored using terminal.
3. Run command `java -jar Project3.jar 5 4 false 0 30 3`
4. Where command arguments are `java -jar Project3.jar rows columns wrapped interconnectivity treasureAndArrowPercentage numberOfMonsters`
5. `rows` integer number of rows of dungeon maze.
6. `columns` integer number of columns of the dungeon maze.
7. `wrapped` boolean for maze wrapped around edges or not.
8. `interconnectivity` integer interconnectivity of the maze.
9. `treasureAndArrowPercentage` integer percentage of caves to be filled with treasure and percentage of all locations to be filled with arrows.
10. `numberOfMonsters` number of monsters in the dungeon.

##How to use Program
The game can be played by following instructions as provided on the screen.\

Common controls are to enter the corresponding alphabet irrespective of case\
MOVE: M SHOOT: S PICKUP: P QUIT: Q\
You can input N for North, E for East, S for South, W for West\
for the command you want to perform.

The Dungeon interface is the main model of game.\
To create a new Dungeon:
```
Randomizer randomizer = new GameRandomizer();
Dungeon dungeon = new DungeonModel(rows,columns,wrapped,
interconnectivity,treasureAndArrowPercentage,numberOfMonsters, randomizer);
```

To create a DungeonController to interact with the game:
```
DungeonController controller = new DungeonConsoleController(
            new InputStreamReader(System.in), System.out);
```
In the above example we're using System.in as the input source and System.out as the output appendable.

To start the game:
```
controller.play(dungeon);
```
To get startLocation of dungeon:
```
Location startLocation = dungeon.getStartLocation();
```

To get endLocation of dungeon:
```
Location endLocation = dungeon.getEndLocation();
```

To get player current Location of dungeon:
```
Location playerCurrentLocation = dungeon.getPlayerCurrentLocation();
```

Next moves available from current location:
`Set<Move> nextMoves = dungeon.getAvailableMoves();`

To move player:\
`movePlayer(move);`

To check if current location has treasure:
```
Location playerCurrentLocation = dungeon.getPlayerCurrentLocation();
playerCurrentLocation.hasTreasure();
```

To pick treasure at the location\
`dungeon.playerPickTreasure()`

We can use `Player player = dungeon.getPlayerDescription();` to get info about player.
Details of a player can be obtained by:
```
boolean hasTreasure = player.hasTreasure();
Map<Treasure, Integer> treasure = player.getTreasure();
```

##Description of Example
Examples of output

Intro after starting the game
```
Welcome to the dungeons
You can input N for North, E for East, S for South, W for West
You are in a cave,
There are 3 arrows here

You can move in
NORTH: N

What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q
```

We enter the alphabet to perform a command

The below example shows 1 ply of moving
```
What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q
M

Where do we go?
n

You are in a tunnel,
There are 3 arrows here

You can move in
NORTH: N
SOUTH: S

What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q
```

Picking up treasure
```
You are in a cave,
There is a very strong rancid smell somewhere near
There is treasure here
You find 4 sapphires
You can move in
NORTH: N
EAST: E
WEST: W

What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q
p

What to pick? Enter A for arrows or T for treasure
t

You picked up 4 sapphires
You now have the following treasure 4 sapphires
You have 9 arrows left

What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q
```

Picking up arrows
```
You are in a tunnel,
There are 3 arrows here

You can move in
NORTH: N
SOUTH: S

What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q
p

What to pick? Enter A for arrows or T for treasure
a

You have picked up 3 arrows
Player has no treasure
You have 9 arrows left

What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q
```

After you've performed a pick operation there is a player description printed
```
You now have the following treasure 9 sapphires 5 diamonds
You have 11 arrows left
```

Example of shooting arrows
```
What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q
s

Where do you want to shoot?
w

How far do you want to shoot? (1-5)
1

You hear a painful roar and wild thrashing in the darkness and then silence. It seems you've killed an Otyugh
You have 9 arrows left
What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q
```

After a monster is injured
```
You hear a painful roar in the distance. It seems your arrow hit an Otyugh
You have 10 arrows left
What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q
```

After a monster is killed
```
You hear a painful roar and wild thrashing in the darkness and then silence. It seems you've killed an Otyugh
You have 9 arrows left
What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q
```

If your arrow misses
```
Your arrow goes whistling through the dungeon and there's a clunk as it falls to the ground after hitting a cave wall
You have 4 arrows left
What do you want to do? MOVE: M SHOOT: S QUIT: Q
```

Example of killed by a monster
```
What do you want to do? MOVE: M SHOOT: S QUIT: Q
m

Where do we go?
s

You were killed. You died a gruesome death at the hands of the Otyugh
```

Example of winning the game
```
What do you want to do? MOVE: M SHOOT: S PICKUP: P QUIT: Q
m

Where do we go?
n

You have escaped the mines of Moria
You collected these treasures on your journey SAPPHIRES: 9 DIAMONDS: 5
```

##Design Changes
The following changes were made to the previous design: 
1. Added class Randomizer interface.
2. Added PlayerPrivate interface to keep object mutating code internal to the package.
3. Added class Edge to help in generation of maze in the Dungeon class.
4. Some changes to method names and return types.

##Assumptions
The following assumptions were made: 
1. Running the game and moving player is the responsibility of the controller in this case the Driver class.
2. After visiting end node the controller can still freely move player.
3. Player is instantiated inside Dungeon model.

##Limitations
1. Players cannot set their player names due to requirement of the project.
2. Next location cannot be fetched without moving to that location.

##Citations
1. [Random number generation between range](https://www.delftstack.com/howto/java/java-random-number-in-range/)
2. [Running jar file](https://stackoverflow.com/questions/1238145/how-to-run-a-jar-file)
3. [Markdown guide](https://www.markdownguide.org/cheat-sheet/)
4. [Implementing BFS to certain depth](https://stackoverflow.com/questions/10258305/how-to-implement-a-breadth-first-search-to-a-certain-depth)