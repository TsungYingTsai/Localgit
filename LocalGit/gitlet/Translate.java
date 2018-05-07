package gitlet;

import java.io.*;
import java.nio.file.Path;

/**
 * Created by intel66 on 12/25/2017.
 */
public class Translate<S> implements Serializable {

    public void save(S Input, Path directory, String fileName) {
        try {

            FileOutputStream fileOut =
                    new FileOutputStream(directory+"/"+fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(Input);
            out.close();
            fileOut.close();

        } catch (IOException i) {
            throw new IllegalArgumentException("System cant SAVE the file");
        }
    }
    public S load( Path directory, String fileName) {
        S e_2 = null;

        try {
            FileInputStream fileIn = new FileInputStream(directory+"/"+fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e_2 =  (S) in.readObject();
            in.close();
            fileIn.close();
            return e_2;
        } catch (IOException | ClassNotFoundException i) {

            throw new IllegalArgumentException("System cant FIND the file");
        }
    }
}
