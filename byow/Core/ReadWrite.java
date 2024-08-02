package byow.Core;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Out;

public class ReadWrite {
    public static void main(String[] args) {

        // Writing to a file

        Out out = new Out("/Users/andywang/Desktop/CS61B/sp23-s707/proj3/newGame.txt");
        out.print();


        // Reading from a file
        In in = new In("/Desktop/CS61B/sp23-s707/proj3/gamefile/new.txt");

    }
}
