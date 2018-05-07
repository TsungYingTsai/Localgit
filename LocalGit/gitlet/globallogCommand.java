package gitlet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by intel66 on 12/20/2017.
 */
public class globallogCommand extends Repository implements CommandInterface {
    @Override
    public void runTheAction(Repository Repo, ArrayList<String> Operands) {
        String LoadheadSHA = STtrigger.load(GetheadFile().getParent(),HEAD);
        HashMap<String,Commit> LoadshatocmMap = LoadshatocmMap();
        HashMap<String,String> LoadcommittreeMap = LoadcommittreeMap();

        for (String key : LoadcommittreeMap.keySet()) {
            System.out.println(LoadshatocmMap.get(key).toPrint());
        }
    }

    @Override
    public boolean WrongOperands(ArrayList<String> Operands) {return Operands.size() != 0;}

    @Override
    public boolean NeedRepo() {return true;}
}
