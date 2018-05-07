package gitlet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by intel66 on 12/20/2017.
 */
public class statusCommand extends Repository implements CommandInterface {

    @Override
    public void runTheAction(Repository Repo, ArrayList<String> Operands) {

        String LoadCMheadSHA = LoadCMheadSHA();
        //HashMap<String,Commit> LoadshatocmMap = HMtrigger.load(GetshatocmFile().getParent(),SHATOCM);
        ///HashMap<Commit,Commit> LoadcommittreeMap = HMtrigger.load(GetcommittreeFile().getParent(),COMMITTREE);
        HashMap<String,String> LoadbranchMap = LoadbranchMap();
        String crrBranch = LoadcrrBranch();

        HashMap<String,byte[]> LoadStageMap = LoadStageMap();
        //HashMap<String,String> LoadTrackMap = HMtrigger.load(GettrackFile().getParent(),TRACK);
        HashMap<String,byte[]> LoaddeleteMap = LoaddeleteMap();
        //String HeadFile = STtrigger.load(GetheadFile().getParent(),HEAD);

        System.out.println("=== Branches ===");
        for (String branch : LoadbranchMap.keySet()) {
            //TODO: if it is current branch, add '*'
            if (crrBranch.equals(branch)) {System.out.print("*");}
            System.out.println(branch);
        } System.out.println("");

        System.out.println("=== Staged Files ===");
        for (String Stage : LoadStageMap.keySet()) {
            System.out.println(Stage);
        } System.out.println("");

        System.out.println("=== Removed Files ===");
        for (String delete : LoaddeleteMap.keySet()) {
            System.out.println(delete);
        } System.out.println("");

        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println("");

        System.out.println("=== Untracked Files ===");
        for (String untrack : Repo.WRDIRtxtUntrackinCrrBranch().keySet()) {
            System.out.println(untrack);
        }
        System.out.println("");
        System.out.println("=== Head  ===");
        System.out.println(LoadCMheadSHA);
        System.out.println("");

        System.out.println("=== CrrBranch History  ===");

        System.out.println("Current Branch is ... " + crrBranch);
        HashMap<String,String> CommitHistory = BranchCommitHistory(crrBranch);
        String headSHA = LoadbranchMap.get(crrBranch);
        System.out.println(headSHA);
        while (CommitHistory.get(headSHA) != null) {
            System.out.println(CommitHistory.get(headSHA));
            headSHA = CommitHistory.get(headSHA);
        }
        System.out.println("");

    }

    @Override
    public boolean WrongOperands(ArrayList<String> Operands) {return Operands.size() != 0;}

    @Override
    public boolean NeedRepo() {return true;}
}
