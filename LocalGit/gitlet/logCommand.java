package gitlet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by intel66 on 12/19/2017.
 */
public class logCommand extends Repository implements CommandInterface {

    @Override
    public void runTheAction(Repository Repo, ArrayList<String> Operands) {
        String LoadheadSHA = STtrigger.load(GetheadFile().getParent(),HEAD);
        HashMap<String,Commit> LoadshatocmMap = LoadshatocmMap();
        HashMap<String,String> LoadcommittreeMap = LoadcommittreeMap();

        while (LoadheadSHA != null) {
            System.out.println(LoadshatocmMap.get(LoadheadSHA).toPrint());
            LoadheadSHA = LoadcommittreeMap.get(LoadheadSHA);
        }

    }

    @Override
    public boolean WrongOperands(ArrayList<String> Operands) {return Operands.size() != 0;}

    @Override
    public boolean NeedRepo() {return true;}
}
