package gitlet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by intel66 on 12/20/2017.
 */
public class findCommand extends Repository implements CommandInterface {
    private Integer i = 0;
    @Override
    public void runTheAction(Repository Repo, ArrayList<String> Operands) {
        //String LoadheadSHA = STtrigger.load(GetheadFile().getParent(),HEAD);
        HashMap<String,Commit> LoadshatocmMap = LoadshatocmMap();
        HashMap<String,String> LoadcommittreeMap = LoadcommittreeMap();
        //HashMap<String,Commit> LoadbranchMap = HMtrigger.load(GetbranchFile().getParent(),BRANCH);

        String findMSG = Operands.get(0);

        for (String key : LoadcommittreeMap.keySet()) {
            if (LoadshatocmMap.get(key).GetMessage().equals(findMSG)) {
                this.i += 1;
                System.out.println(TurnSHA.Tosha1(key));
            }
        }
        if (i.equals(0)) {throw new IllegalArgumentException("Found no commit with that message.");}
    }

    @Override
    public boolean WrongOperands(ArrayList<String> Operands) {return Operands.size() != 1;}

    @Override
    public boolean NeedRepo() {return true;}
}
