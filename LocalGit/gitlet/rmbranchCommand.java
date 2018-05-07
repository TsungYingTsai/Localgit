package gitlet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by intel66 on 1/12/2018.
 */
public class rmbranchCommand extends Repository implements CommandInterface {

    @Override
    public void runTheAction(Repository Repo, ArrayList<String> Operands) {
        String rmBranch = Operands.get(0);

        HashMap<String, String> LoadbranchMap = LoadbranchMap();
                //HMtrigger.load(GetbranchFile().getParent(), BRANCH);
        if (LoadbranchMap.containsKey(rmBranch) == false) {
            throw new IllegalArgumentException("A branch with that name does not exist.");
        }

        String crrBranch = STtrigger.load(GetcrrBranchName().getParent(),CRRBRANCH);
        if (crrBranch.equals(rmBranch) == true) {
            throw new IllegalArgumentException("Cannot remove the current branch.");
        }

        LoadbranchMap.remove(rmBranch);
        SaveLoadbranchMap(LoadbranchMap);
        //HMtrigger.save(LoadbranchMap,GetbranchFile().getParent(), BRANCH);

    }

    @Override
    public boolean WrongOperands(ArrayList<String> Operands) {return Operands.size() != 1;}

    @Override
    public boolean NeedRepo() {return true;}
}
