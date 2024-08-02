package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.StdDraw;

import java.util.*;


public class NewWord {
    private List<int[]> midpoints = new ArrayList<>();
    public List<Room> Rooms;
    public static TETile[][] tiles;
    private int WIDTH;
    private int HEIGHT;
    private Random random;
    private int numRoom;
    private int playerX;
    private int playerY;

    public NewWord(int width, int height, long seed) {
        random = new Random(seed);
        Rooms = new ArrayList<>();
        this.WIDTH = width;
        this.HEIGHT = height;
        this.tiles = new TETile[WIDTH][HEIGHT];
        this.numRoom = RandomUtils.uniform(random, 10, 30);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
        //random generate rooms

        for (int i = 0; i < numRoom; i++) {
            int randRoomWidth = RandomUtils.uniform(random, 2, 8);
            int randRoomHeight = RandomUtils.uniform(random, 2, 8);
            int randStartX = RandomUtils.uniform(random, 0, WIDTH - 1);
            int randStartY = RandomUtils.uniform(random, 0, HEIGHT - 1);
            int randEndX = randStartX + randRoomWidth + 1;
            int randEndY = randStartY + randRoomHeight + 1;
            boolean overLap = generateRandomRoom(randStartX, randEndX, randStartY, randEndY);
            // eliminate overlap rooms
            if (!overLap) {
                generateMidPoints(randRoomWidth, randRoomHeight, randStartX, randStartY);
            }
        }
        // use hallway connect rooms
        for (int i = 0; i < midpoints.size() - 1; i++) {
            int[] firstMidpoint = midpoints.get(i);
            int[] secondMidpoint = midpoints.get(i + 1);
            firstLeftBottom(firstMidpoint, secondMidpoint);
            secondLeftBottom(firstMidpoint, secondMidpoint);
            firstLeftTop(firstMidpoint, secondMidpoint);
            secondLeftTop(firstMidpoint, secondMidpoint);
        }
        //add walls to hallways
        addHallwayWall();

        //generate gate
        generateGate();

        //generate player
        generatePlayer();
    }

    public TETile[][] getTiles() {
        return tiles;
    }


    public void generateMidPoints(int randRoomWidth, int randRoomHeight,
                                  int randStartX, int randStartY) {
        Room room = new Room(randRoomWidth, randRoomHeight, randStartX, randStartY);
        int midpointX = RandomUtils.uniform(random, randStartX + 1, randStartX + randRoomWidth);
        int midpointY = RandomUtils.uniform(random, randStartY + 1, randStartY + randRoomHeight);
        tiles[midpointX][midpointY] = Tileset.LOCKED_DOOR;
        int[] midpoint = new int[2];
        midpoint[0] = midpointX;
        midpoint[1] = midpointY;
        midpoints.add(midpoint);
        Rooms.add(room);
    }

    public boolean generateRandomRoom(int randStartX, int randEndX, int randStartY, int randEndY) {
        boolean overLap = false;
        for (int m = randStartX; m <= randEndX; m++) {
            for (int n = randStartY; n <= randEndY; n++) {
                if (m < WIDTH && n < HEIGHT && tiles[m][n] != Tileset.NOTHING) {
                    overLap = true;
                }
                else if (m >= WIDTH || n >= HEIGHT) {
                    overLap = true;
                }
            }
        }
        return overLap;
    }
    public void firstLeftBottom(int[] firstMidpoint, int[] secondMidpoint) {
        if (firstMidpoint[0] <= secondMidpoint[0] && firstMidpoint[1] <= secondMidpoint[1]) {
            int left = firstMidpoint[0];
            int right = secondMidpoint[0];
            int bottom = firstMidpoint[1];
            int top = secondMidpoint[1];
            for (int m = left; m <= right; m++) {
                tiles[m][bottom] = Tileset.FLOOR;
            }
            for (int n = bottom; n <= top; n++) {
                tiles[right][n] = Tileset.FLOOR;
            }
        }
    }
    public void secondLeftBottom(int[] firstMidpoint, int[] secondMidpoint) {
        if (firstMidpoint[0] > secondMidpoint[0] && firstMidpoint[1] > secondMidpoint[1]) {
            int left = secondMidpoint[0];
            int right = firstMidpoint[0];
            int bottom = secondMidpoint[1];
            int top = firstMidpoint[1];
            for (int m = left; m <= right; m++) {
                tiles[m][bottom] = Tileset.FLOOR;
            }
            for (int n = bottom; n <= top; n++) {
                tiles[right][n] = Tileset.FLOOR;
            }
        }
    }
    public void firstLeftTop(int[] firstMidpoint, int[] secondMidpoint) {
        if (firstMidpoint[0] <= secondMidpoint[0] && firstMidpoint[1] >= secondMidpoint[1]) {
            int left = firstMidpoint[0];
            int right = secondMidpoint[0];
            int bottom = secondMidpoint[1];
            int top = firstMidpoint[1];
            for (int m = left; m <= right; m++) {
                tiles[m][top] = Tileset.FLOOR;
            }
            for (int n = bottom; n <= top; n++) {
                tiles[right][n] = Tileset.FLOOR;
            }
        }
    }
    public void secondLeftTop(int[] firstMidpoint, int[] secondMidpoint) {
        if (firstMidpoint[0] > secondMidpoint[0] && firstMidpoint[1] < secondMidpoint[1]) {
            int left = secondMidpoint[0];
            int right = firstMidpoint[0];
            int bottom = firstMidpoint[1];
            int top = secondMidpoint[1];
            for (int m = left; m <= right; m++) {
                tiles[m][top] = Tileset.FLOOR;
            }
            for (int n = bottom; n <= top; n++) {
                tiles[right][n] = Tileset.FLOOR;
            }
        }
    }

    public void addHallwayWall() {
        for (int i = 1; i < WIDTH; i++) {
            for (int j = 1; j < HEIGHT; j++) {
                if (tiles[i][j].description().equals(Tileset.FLOOR.description())
                        && tiles[i - 1][j].description().equals(Tileset.NOTHING.description())) {
                    tiles[i - 1][j] = Tileset.WALL;
                }
                if (tiles[i][j].description().equals(Tileset.FLOOR.description())
                        && tiles[i + 1][j].description().equals(Tileset.NOTHING.description())) {
                    tiles[i + 1][j] = Tileset.WALL;
                }
                if (tiles[i][j].description().equals(Tileset.FLOOR.description())
                        && tiles[i][j - 1].description().equals(Tileset.NOTHING.description())) {
                    tiles[i][j - 1] = Tileset.WALL;
                }
                if (tiles[i][j].description().equals(Tileset.FLOOR.description())
                        && tiles[i][j + 1].description().equals(Tileset.NOTHING.description())) {
                    tiles[i][j + 1] = Tileset.WALL;
                }
                if (tiles[i][j].description().equals(Tileset.FLOOR.description())
                        && tiles[i - 1][j + 1].description().equals(Tileset.NOTHING.description())) {
                    tiles[i - 1][j + 1] = Tileset.WALL;
                }
                if (tiles[i][j].description().equals(Tileset.FLOOR.description())
                        && tiles[i - 1][j - 1].description().equals(Tileset.NOTHING.description())) {
                    tiles[i - 1][j - 1] = Tileset.WALL;
                }
                if (tiles[i][j].description().equals(Tileset.FLOOR.description())
                        && tiles[i + 1][j + 1].description().equals(Tileset.NOTHING.description())) {
                    tiles[i + 1][j + 1] = Tileset.WALL;
                }
                if (tiles[i][j].description().equals(Tileset.FLOOR.description())
                        && tiles[i + 1][j - 1].description().equals(Tileset.NOTHING.description())) {
                    tiles[i + 1][j - 1] = Tileset.WALL;
                }
            }
        }
    }

    private boolean validDoor(int gateX, int gateY) {
        return (tiles[gateX - 1][gateY] == Tileset.NOTHING && tiles[gateX + 1][gateY] == Tileset.FLOOR)
                || (tiles[gateX + 1][gateY] == Tileset.NOTHING && tiles[gateX - 1][gateY] == Tileset.FLOOR)
                || (tiles[gateX][gateY - 1] == Tileset.NOTHING && tiles[gateX][gateY + 1] == Tileset.FLOOR)
                || (tiles[gateX][gateY + 1] == Tileset.NOTHING && tiles[gateX][gateY - 1] == Tileset.FLOOR);
    }
    public void generateGate() {
        while (true) {
            int gateX = RandomUtils.uniform(random, 1, WIDTH - 1);
            int gateY = RandomUtils.uniform(random, 1, HEIGHT - 1);
            if (validDoor(gateX, gateY)) {
                tiles[gateX][gateY] = Tileset.LOCKED_DOOR;
                break;
            }
        }
    }
    public void generatePlayer() {
        while(true) {
            this.playerX = RandomUtils.uniform(random, 1, WIDTH - 1);
            this.playerY = RandomUtils.uniform(random, 1, HEIGHT - 1);
            if (tiles[playerX][playerY] == Tileset.FLOOR) {
                tiles[playerX][playerY] = Tileset.PLAYER;
                break;
            }
        }
    }
    public void moveTop() {
        if (tiles[playerX][playerY + 1].description().equals(Tileset.FLOOR.description())) {
            tiles[playerX][playerY] = Tileset.FLOOR;
            tiles[playerX][playerY + 1] = Tileset.PLAYER;
            playerY = playerY + 1;
        }
    }
    public void moveBottom() {
        if (tiles[playerX][playerY - 1].description().equals(Tileset.FLOOR.description())) {
            tiles[playerX][playerY] = Tileset.FLOOR;
            tiles[playerX][playerY - 1] = Tileset.PLAYER;
            playerY = playerY - 1;
        }
    }
    public void moveLeft() {
        if (tiles[playerX - 1][playerY].description().equals(Tileset.FLOOR.description())) {
            tiles[playerX][playerY] = Tileset.FLOOR;
            tiles[playerX - 1][playerY] = Tileset.PLAYER;
            playerX = playerX - 1;
        }
    }
    public void moveRight() {
        if (tiles[playerX + 1][playerY].description().equals(Tileset.FLOOR.description())) {
            tiles[playerX][playerY] = Tileset.FLOOR;
            tiles[playerX + 1][playerY] = Tileset.PLAYER;
            playerX  = playerX + 1;
        }
    }
    public String mousePos() {
        int x = (int) StdDraw.mouseX();
        int y = (int) StdDraw.mouseY();
        if (x >= WIDTH || y >= HEIGHT) {
            return "out of bound";
        }
//        else if (x == playerX && y == playerY) {
//            return Engine.name;
//        }
        return tiles[x][y].description();
    }


    public static void main(String[] args) {
        // Change these parameters as necessary
        int width = 50;
        int height = 40;
        int a = 1234;
        NewWord newWord = new NewWord(width, height, a);

        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        ter.renderFrame(newWord.getTiles());
    }
}
