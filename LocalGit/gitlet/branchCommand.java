package gitlet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by intel66 on 12/22/2017.
 */
public class branchCommand extends Repository implements CommandInterface {


    @Override
    public void runTheAction(Repository Repo, ArrayList<String> Operands) {
        String branchName = Operands.get(0);
        HashMap<String,String> LoadbranchMap = LoadbranchMap();
        String LoadCMheadSHA = LoadCMheadSHA();
        if (LoadbranchMap.keySet().contains(branchName)) {throw new IllegalArgumentException("A branch with that name already exists.");}
        LoadbranchMap.put(branchName,LoadCMheadSHA);
        SaveLoadbranchMap(LoadbranchMap);

        System.out.println(LoadbranchMap);
    }

    @Override
    public boolean WrongOperands(ArrayList<String> Operands) {return Operands.size() != 1;}

    @Override
    public boolean NeedRepo() {return true;}


}
