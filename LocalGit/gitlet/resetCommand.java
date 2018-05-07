package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by intel66 on 1/12/2018.
 */
public class resetCommand extends Repository implements CommandInterface {
    @Override
    public void runTheAction(Repository Repo, ArrayList<String> Operands) {
        String tarCommitID = Operands.get(0);

        HashMap<String,Commit> LoadshatocmMap = LoadshatocmMap();
                //HMtrigger.load(GetshatocmFile().getParent(),SHATOCM);
        if (LoadshatocmMap.containsKey(tarCommitID) == false) {
            throw new IllegalArgumentException("No commit with that id exists.");
        }

        HashMap<String,byte[]> WRDIRtxtUntrackinCrrBranch = WRDIRtxtUntrackinCrrBranch(); // untracked in current branch HEAD

        /**
         * Load CommitID BlobInformation, and to check failure check
         */
        Commit tarCommit = LoadshatocmMap.get(tarCommitID);
        HashMap<String,String> tarFiletoSHAname = tarCommit.GetCMBlobInfo();

        for (String untrackedFile : WRDIRtxtUntrackinCrrBranch.keySet()) {
            if (tarFiletoSHAname.containsKey(untrackedFile) == true) {
                throw new IllegalArgumentException("There is an untracked file in the way; delete it or add it first.");
            }
        }

        // chekout all files
        HashMap<String,byte[]> LoadshatoBlobMap = LoadshatoblobMap();
                //HMtrigger.load(GetshatoBlobFile().getParent(),SHATOBLOB);
        Commit IDCommit = LoadshatocmMap.get(tarCommitID);
        HashMap<String,String> IDFilenametoSHA = IDCommit.GetCMBlobInfo();

        HashMap<String,byte[]> LoadStageMap = LoadStageMap();
                //HMtrigger.load(GetstageFile().getParent(),STAGE);

        for (String checkoutFileName : IDFilenametoSHA.keySet()) {
            File ff = new File(checkoutFileName);
            Utils.writeContents(ff,LoadshatoBlobMap.get(IDFilenametoSHA.get(checkoutFileName)));
            LoadStageMap.remove(checkoutFileName);
        }

        String crrBranch = LoadcrrBranch();
                //STtrigger.load(GetcrrBranchName().getParent(),CRRBRANCH);
        HashMap<String,String> LoadbranchMap = LoadbranchMap();
                //HMtrigger.load(GetbranchFile().getParent(),BRANCH);
        //String Headfile = STtrigger.load(GetheadFile().getParent(),HEAD);

        SaveLoadCMheadSHA(tarCommitID);
        //STtrigger.save(tarCommitID,GetheadFile().getParent(),HEAD);
        LoadbranchMap.put(crrBranch,tarCommitID);
        SaveLoadbranchMap(LoadbranchMap);
        //HMtrigger.save(LoadbranchMap,GetbranchFile().getParent(),BRANCH);
        SaveLoadStageMap(LoadStageMap);
        //HMtrigger.save(LoadStageMap,GetstageFile().getParent(),STAGE);

    }

    @Override
    public boolean WrongOperands(ArrayList<String> Operands) {return Operands.size() != 1;}

    @Override
    public boolean NeedRepo() {return true;}
}
