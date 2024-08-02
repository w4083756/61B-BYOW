
package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;
import edu.princeton.cs.algs4.StdDraw;
import org.apache.hc.core5.annotation.Internal;

import java.awt.*;
import java.util.Arrays;


public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    static String name;
    private String MAX_SEED = "2147483647";

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        // create black canvas
        StdDraw.setCanvasSize(this.WIDTH * 16, this.HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.WIDTH);
        StdDraw.setYscale(0, this.HEIGHT);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        // create manu page 1
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontBig);
        StdDraw.text(WIDTH / 2, HEIGHT / 1.5, "CS61B :  THE GAME");
        Font a = new Font("Monaco", Font.BOLD, 20);
        StdDraw.setFont(a);
        StdDraw.text(WIDTH / 2, HEIGHT / 2.2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2.5, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2.9, "Quit (Q)");
        StdDraw.show();

        // manu after enter "n"
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char l1 = StdDraw.nextKeyTyped();
                if (l1 == 'n') {
                    name = "";
                    String s1 = "Please enter your name, end with 's'";
                    drawFrame(s1, WIDTH / 2, HEIGHT / 1.3, "Monaco", Font.BOLD, 30);
                    String c = solicitNCharsInput(WIDTH / 2, HEIGHT / 3, "Monaco", Font.BOLD, 30);
                    name = name + c.substring(0, c.length() - 1);
                    name = c.substring(0, c.length() - 1);
                    String s2 = "Please enter a random number, end with 's'";
                    drawFrame(s2, WIDTH / 2, HEIGHT / 1.3, "Monaco", Font.BOLD, 30);
                    break;
                } else if (l1 == 'q') {
                    System.exit(0);
                } else if (l1 == 'l') {
                    In in = new In("/Users/andywang/Desktop/CS61B/sp23-proj3-g1119/proj3/newGame.txt");
                    String savedLine = in.readLine();
                    interactWithInputString(savedLine);
                }
            }
        }

        // user enter random number and end with "s"
        String nToS;
        long seed2;
        while (true) {
            nToS = 'n' + solicitNCharsInput(WIDTH / 2, HEIGHT / 3, "Monaco", Font.BOLD, 30);
            String nTosNumberPart = nToS.substring(1, nToS.length() - 1);
            if (isNumber(nTosNumberPart)) {
                seed2 = Long.valueOf(nTosNumberPart);
                break;
            }
            String str3 = "Your input is invalid, please try again";
            drawFrame(str3, WIDTH / 2, HEIGHT / 1.3, "Monaco", Font.BOLD, 30);
            StdDraw.show();
        }

        //use seed to create new world
        NewWord newWorld = new NewWord(WIDTH, HEIGHT, seed2);
        ter.initialize(WIDTH, HEIGHT + 4);
        TETile[][] finalWorldFrame1 = newWorld.getTiles();

        //begin game
        gameBegin(finalWorldFrame1, newWorld, nToS);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, running both of these:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param s the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    private String retrieveSeed(String s) {
        int i = 0;
        String seed = "";
        while (Character.isDigit(s.charAt(i))) {
            seed += s.charAt(i);
            i++;
        }
        return seed;
    }

    private TETile[][] generateEmptyTiles() {
        TETile[][] t = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x++) {
            Arrays.fill(t[x], Tileset.NOTHING);
        }
        return t;
    }

    private int cutIt(String s) {
        String n = "";
        if (s.length() > MAX_SEED.length()) {
            int i = 0;
            while (n.length() < MAX_SEED.length()) {
                n += s.charAt(i);
                i++;
            }
        }
        return Integer.parseInt(n);
    }

    public TETile[][] interactWithInputString(String input) {
        if (input.length() < 1) {
            return generateEmptyTiles();
        } else if (input.charAt(0) == 'l') {
            String rest = "";
            for (int i = 1; i < input.length(); i++) {
                rest += input.charAt(i);
            }
            In in = new In("/Users/andywang/Desktop/CS61B/sp23-proj3-g1119/proj3/newGame.txt");
            String savedLine = in.readLine();
            if (savedLine == null) {
                return generateEmptyTiles();
            }
            interactWithInputString(savedLine.substring(0, savedLine.length() - 2) + rest);
        }
        String seed = "";
        String stringAfterS = "";
        String cNoq = input.substring(0, input.length() - 2);
        for (int i = 0; i < input.length() - 1; i++) {
            if (input.charAt(i) == 'n') {
                seed = retrieveSeed(input.substring(1));
                stringAfterS = input.substring(seed.length() + 2, input.length());
                break;
            }
          /**if (input.charAt(i) == 's') {
          //      seed = input.substring(1, i);
          //      stringAfterS = input.substring(i + 1, input.length());
          //      break;
          }*/
        }
        if (seed.equals("")) {
            return generateEmptyTiles();
        }
        long seedInt = Long.parseLong(seed);
        NewWord newWord = new NewWord(WIDTH, HEIGHT, seedInt);
        for (int i = 0; i < stringAfterS.length(); i++) {
            if (stringAfterS.charAt(i) == 'w') {
                newWord.moveTop();
            } else if (stringAfterS.charAt(i) == 'a') {
                newWord.moveLeft();
            } else if (stringAfterS.charAt(i) == 'd') {
                newWord.moveRight();
            } else if (stringAfterS.charAt(i) == 's') {
                newWord.moveBottom();
            } else if (stringAfterS.charAt(i) == 't') {
                for (Room room : newWord.Rooms) {
                    if (!room.Light) {
                        room.turnOnLight();
                        break;
                    }
                }
            } else if (stringAfterS.charAt(i) == 'o') {
                for (Room room : newWord.Rooms) {
                    if (room.Light) {
                        room.turnOffLight();
                        break;
                    }
                }
            }
//            else if (stringAfterS.charAt(i) == ':') {
//                if (stringAfterS.charAt(i + 1) == 'q') {
//                    new CreateFile();
//                    Out out = new Out("/Users/andywang/Desktop/CS61B/sp23-proj3-g1119/proj3/newGame.txt");
//                    out.print(input);
//                    break;
//                }
//            }
        }
//        ter.initialize(WIDTH, HEIGHT + 4);
//         ter.renderFrame(newWord.getTiles());
//         TETile[][] finalWorldFrame = newWord.getTiles();
//         gameBegin(finalWorldFrame, newWord, cNoq);
//         gameBegin(finalWorldFrame, newWord,cNoq);
//         return finalWorldFrame;
        return newWord.getTiles();
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        TETile[][] t1 = engine.interactWithInputString("n2838278388919144292ss");
        TETile[][] t2 = engine.interactWithInputString("n2838278388919144292ss:q");
        System.out.println(Arrays.deepEquals(t1, t2));

    }

    public void drawFrame(String s, double x, double y, String name1, int style, int size) {
        /* Take the input string S and display it at the center of the screen,
         * with the pen settings given below. */
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font(name1, style, size);
        StdDraw.setFont(fontBig);
        StdDraw.text(x, y, s);
        StdDraw.show();
    }
    public String solicitNCharsInput(double x, double y, String name1, int style, int size) {
        String b = "";
        while (true) {
            StdDraw.clear(Color.BLACK);
            if (StdDraw.hasNextKeyTyped()) {
                b = b + StdDraw.nextKeyTyped();
                drawFrame(b, x, y, name1, style, size);
                for (int i = 0; i < b.length() - 1; i++) {
                    if (b.charAt(i) == ':' && (b.charAt(i + 1) == 'q' || b.charAt(i + 1) == 'Q')) {
                        System.exit(0);
                    }
                }
                if (b.charAt(b.length() - 1) == 's') {
                    break;
                }
            }
        }
        return b;
    }
    public boolean isNumber(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public void gameBegin(TETile[][] tileCurr, NewWord newWord1, String str) {
        while (true) {
            StdDraw.setPenColor(Color.WHITE);
            Font fontSmall = new Font("Monaco", Font.BOLD, 15);
            StdDraw.setFont(fontSmall);
            StdDraw.line(0, HEIGHT + 1, WIDTH, HEIGHT + 1);
            StdDraw.textLeft(0, HEIGHT + 3, newWord1.mousePos());
            StdDraw.show();
            StdDraw.pause(10);
            ter.renderFrame(tileCurr);
            if (StdDraw.hasNextKeyTyped()) {
                char l = StdDraw.nextKeyTyped();
                str = str + l;
                if (l == 'w') {
                    newWord1.moveTop();
                } else if (l == 'a') {
                    newWord1.moveLeft();
                } else if (l == 'd') {
                    newWord1.moveRight();
                } else if (l == 's') {
                    newWord1.moveBottom();
                } else if (l == 't') {
                    for (Room room : newWord1.Rooms) {
                        if (!room.Light) {
                            room.turnOnLight();
                            break;
                        }
                    }
                } else if (l == 'o') {
                    for (Room room : newWord1.Rooms) {
                        if (room.Light) {
                            room.turnOffLight();
                            break;
                        }
                    }
                }
                for (int i = 0; i < str.length() - 1; i++) {
                    if (str.charAt(i) == ':' && (str.charAt(i + 1) == 'q'
                            || str.charAt(i + 1) == 'Q')) {
                        new CreateFile();
                        Out out = new Out("/Users/andywang/Desktop/CS61B/sp23-proj3-g1119/proj3/newGame.txt");
                        out.print(str);
                        System.exit(0);
                    }
                }
            }
        }
    }
}
