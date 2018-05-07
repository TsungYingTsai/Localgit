package gitlet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by intel66 on 12/26/2017.
 */
public class addCommand extends Repository implements CommandInterface {

    @Override
    public void runTheAction(Repository Repo, ArrayList<String> Operands) {
        String theFile = Operands.get(0);
        Path filePath = GetworkingDir().resolve(theFile);
        if (Files.exists(filePath) == false) {
            throw new IllegalArgumentException("File does not exist.");
        }
        if (Files.isDirectory(filePath) == true) {
            throw new IllegalArgumentException(("Cannot add a directory."));
        }
        HashMap<String,byte[]> LoaddeleteMap = LoaddeleteMap();
        if (LoaddeleteMap.containsKey(theFile)) {
            LoaddeleteMap.remove(theFile);
            HMtrigger.save(LoaddeleteMap,GetdeleteFile().getParent(),DELETE);
            return;
        }

        HashMap<String,byte[]> LoadStageMap = LoadStageMap();
        HashMap<String, String> LoadblobMap = LoadblobMap();
        HashMap<String,byte[]> LoadshatoblobMap = LoadshatoblobMap();


        try {

            byte[] Addcontent = Files.readAllBytes(filePath);

            if (LoadblobMap.containsKey(theFile)) {
                if ( new String(LoadshatoblobMap.get(LoadblobMap.get(theFile))).equals(new String(Addcontent))){
                    throw new IllegalArgumentException("The identical file has been commited.");
                }
            }

            if (LoadStageMap.containsKey(theFile) == true && new String (LoadStageMap.get(theFile)).equals(new String(Addcontent))) {
                throw new IllegalArgumentException("The file has been staged.");
            }
            else {
                LoadStageMap.put(theFile, Addcontent);
                SaveLoadStageMap(LoadStageMap);
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Target file does not existed!");
        }
    }

    @Override
    public boolean WrongOperands(ArrayList<String> Operands) {return Operands.size() != 1;}

    @Override
    public boolean NeedRepo() {return true;}

}
