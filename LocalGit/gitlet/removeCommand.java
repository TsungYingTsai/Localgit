package gitlet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by intel66 on 12/26/2017.
 */
public class removeCommand extends Repository implements CommandInterface {
    @Override
    public void runTheAction(Repository Repo, ArrayList<String> Operands) {
        String rmFile = Operands.get(0);
        remove(Repo, rmFile);
    }

    public void remove(Repository Repo, String rmFile){
        Path filePath = GetworkingDir().resolve(rmFile);
        HashMap<String,byte[]> LoadStageMap = LoadStageMap();


        HashMap<String,byte[]> LoadDeleteMap = LoaddeleteMap();
        HashMap<String,String> LoadblobMap = LoadblobMap();

        try {

            byte[] Addcontent = Files.readAllBytes(filePath);
            boolean isStage = LoadStageMap.containsKey(rmFile);
            boolean isTrack = LoadblobMap.containsKey(rmFile);
            if (isStage == false && isTrack == false) {throw new IllegalArgumentException("No reason to remove the file.");}
/**
 * deal with staging area
 */
            if (isStage) {
                LoadStageMap.remove(rmFile);
                SaveLoadStageMap(LoadStageMap);
            }

/**
 * deal with commit item
 */
            if (isTrack) {
                if (Files.isDirectory(filePath) == true) {
                    throw new IllegalArgumentException(("Cannot remove a directory."));
                }
                if (Files.exists(filePath) == false) {
                    throw new IllegalArgumentException(("wanted deleted file not existed in workingDir"));
                }
                LoadDeleteMap.put(rmFile,Addcontent);
                SaveLoadDeleteMap(LoadDeleteMap);

                Files.delete(filePath);
            }

        } catch (IOException excp) {
            throw new IllegalArgumentException("No reason to remove the file!");
        }
    }

    @Override
    public boolean WrongOperands(ArrayList<String> Operands) {return Operands.size() != 1;}

    @Override
    public boolean NeedRepo() {return true;}

}
