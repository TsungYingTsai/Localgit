package gitlet;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by intel66 on 12/26/2017.
 */
public class commitCommand extends Repository implements CommandInterface {
    //Commit CommitTrigger = new Commit();

    @Override
    public void runTheAction(Repository Repo, ArrayList<String> Operands) {
        String commitMSG = Operands.get(0);
        commit(Repo,commitMSG);
    }


    public void commit(Repository Repo, String commitMSG) {

        HashMap<String,byte[]> LoadStageMap = LoadStageMap();

        HashMap<String,byte[]> LoaddeleteMap = LoaddeleteMap();

        if (LoadStageMap.size() == 0 && LoaddeleteMap.size() == 0) {
            throw new IllegalArgumentException("No changes added to the commit.");
        }

        HashMap<String,String> LoadbranchMap = LoadbranchMap();
        HashMap<String,Commit> LoadshatocmMap = LoadshatocmMap();
        HashMap<String,String> LoadcommittreeMap = LoadcommittreeMap();
        String LoadCMheadSHA = LoadCMheadSHA();

        String commitParent = LoadCMheadSHA;
        Date commitTime = new Date();


        HashMap<String,String> LoadblopMap = LoadblobMap();
        HashMap<String,byte[]> LoadshatoblobMap = LoadshatoblobMap();

        for (String rmFile : LoaddeleteMap.keySet()) {
            LoadblopMap.remove(rmFile);
        }

        for (String keys: LoadStageMap.keySet()) {
            String SHACodeofContent = TurnSHA.Tosha1(LoadStageMap.get(keys));
            LoadblopMap.put(keys,SHACodeofContent);
            LoadshatoblobMap.put(SHACodeofContent,(LoadStageMap.get(keys)));
        }
        //HMtrigger.save(LoadblobMap,GetblobFile().getParent(),BLOB);
        // this time commit

        SaveLoadshatoblobMap(LoadshatoblobMap);

        Commit commitCommit = new Commit(commitMSG,commitParent,commitTime,LoadblopMap);
        String commit_Sha = TurnSHA.Tosha1(commitCommit);

        String crrBranch = LoadcrrBranch();

        //LoadcommittreeMap.put(commitCommit,LoadshatocmMap.get(LoadheadSHA));
        LoadCMheadSHA = commit_Sha;
        LoadcommittreeMap.put(commit_Sha,commitCommit.GetParent());
        LoadbranchMap.put(crrBranch,commit_Sha);
        LoadshatocmMap.put(commit_Sha,commitCommit);

        SaveLoadCMheadSHA(LoadCMheadSHA);
        SaveLoadbranchMap(LoadbranchMap);
        SaveLoadcommittreeMp(LoadcommittreeMap);
        SaveLoadshatocmMap(LoadshatocmMap);


        LoadStageMap.clear();
        SaveLoadStageMap(LoadStageMap);

        LoaddeleteMap.clear();
        SaveLoadDeleteMap(LoaddeleteMap);

        System.out.print("==="+"\n");
        System.out.print("crrCMBlob:   "+LoadblopMap+"\n");
        System.out.print("==="+"\n");
        System.out.print("Stage Status:   "+LoadStageMap+"\n");
        System.out.print("==="+"\n");
        System.out.print("Delete status:   "+LoaddeleteMap+"\n");
        System.out.print("==="+"\n");
        System.out.print("Committree Status:   "+LoadcommittreeMap+"\n");
        System.out.print("==="+"\n");
        System.out.print("Head:   "+LoadCMheadSHA+"\n");
        System.out.print("==="+"\n");
        System.out.print("Branch:   "+LoadbranchMap+"\n");
        System.out.print("==="+"\n");
    }

    @Override
    public boolean WrongOperands(ArrayList<String> Operands) {
        if (Operands == null || Operands.size() == 0) {
            throw new IllegalArgumentException(
                    "Please enter a commit message.");
        }
        return Operands.size() != 1 ;
    }

    @Override
    public boolean NeedRepo() {return true;}
}
