package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;


public class Room {
    public boolean Light;
    private int startX;
    private int startY;
    private int x;
    private int y;

    public Room(int x, int y, int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
        this.x = x;
        this.y = y;
        this.Light = false;
        int endX = startX + x + 1;
        int endY = startY + y + 1;
        if (endX < Engine.WIDTH && endY < Engine.HEIGHT) {
            for (int i = startX; i <= endX; i++) {
                NewWord.tiles[i][startY] = Tileset.WALL;
                NewWord.tiles[i][endY] = Tileset.WALL;
            }
            for (int i = startY; i <= endY; i++) {
                NewWord.tiles[startX][i] = Tileset.WALL;
                NewWord.tiles[endX][i] = Tileset.WALL;
            }
            for (int i = startX + 1; i < endX; i++) {
                for (int j = startY + 1; j < endY; j++) {
                    NewWord.tiles[i][j] = Tileset.FLOOR;
                }
            }
        }
    }
    public void turnOnLight() {
        int endX = startX + x + 1;
        int endY = startY + y + 1;
        if (Light == false) {
            Light = true;
            for (int i = startX + 1; i < endX; i++) {
                for (int j = startY + 1; j < endY; j++) {
                    NewWord.tiles[i][j] = Tileset.FLOORLIGHT;
                }
            }
        }
    }
    public void turnOffLight() {
        int endX = startX + x + 1;
        int endY = startY + y + 1;
        if (Light == true) {
            Light = false;
            for (int i = startX + 1; i < endX; i++) {
                for (int j = startY + 1; j < endY; j++) {
                    NewWord.tiles[i][j] = Tileset.FLOOR;
                }
            }
        }
    }
}
