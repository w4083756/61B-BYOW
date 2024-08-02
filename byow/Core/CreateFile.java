package byow.Core;
import java.io.File;
import java.io.IOException;
public class CreateFile {
    public CreateFile() {
        try {
            File myobj = new File("/Users/andywang/Desktop/CS61B/sp23-proj3-g1119/proj3/newGame.txt");
            if (myobj.createNewFile()) {
                System.out.println("File create: " + myobj.getName());
            } else {
                System.out.println("File already exists. ");
            }
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }
}
