package gitlet;

import com.sun.tools.javac.code.Attribute;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by intel66 on 1/13/2018.
 */
public class mergeCommand extends Repository implements CommandInterface {
    @Override
    public void runTheAction(Repository Repo, ArrayList<String> Operands) {
        String givenBranch = Operands.get(0);
        HashMap<String,String> LoadStageMap = HMtrigger.load(GetstageFile().getParent(),STAGE);
        HashMap<String,String> LoaddeleteMap = HMtrigger.load(GetdeleteFile().getParent(),DELETE);
        if (LoadStageMap.size() != 0 || LoaddeleteMap.size() != 0) {
            throw new IllegalArgumentException("You have uncommitted changes.");
        }

        HashMap<String, String> LoadbranchMap = HMtrigger.load(GetbranchFile().getParent(), BRANCH);
        if (LoadbranchMap.containsKey(givenBranch) == false) {
            throw new IllegalArgumentException("A branch with that name does not exist.");
        }

        String crrBranch = STtrigger.load(GetcrrBranchName().getParent(),CRRBRANCH);
        if (crrBranch.equals(givenBranch)) {
            throw new IllegalArgumentException("Cannot merge a branch with itself.");
        }

        //TODO: one more fai;ure case hasn't come up with

        HashMap<String,byte[]> WRDIRtxtUntrackinCrrBranch = WRDIRtxtUntrackinCrrBranch();
        String GivenBranchHead = LoadbranchMap.get(givenBranch);
        HashMap<String,Commit> LoadshatocmMap = LoadshatocmMap();
                //HMtrigger.load(GetshatocmFile().getParent(),SHATOCM);
        Commit GivenBranchCMInfo = LoadshatocmMap.get(GivenBranchHead);
        HashMap<String,String> GivenBranchTrack = GivenBranchCMInfo.GetCMBlobInfo();

        for (String fileUNtrack : WRDIRtxtUntrackinCrrBranch.keySet()) {
            if (GivenBranchTrack.containsKey(fileUNtrack)) {
                throw new IllegalArgumentException("There is an untracked file in the way; delete it or add it first.");
            }
        }

        HashMap<String,String> CrrBranchHistory = BranchCommitHistory(crrBranch);
        if (CrrBranchHistory.containsKey(GivenBranchHead)) {
            throw new IllegalArgumentException("Given branch is an ancestor of the current branch.");
        }

        HashMap<String,String> GivenBranchHistory = BranchCommitHistory(givenBranch);
        String CrrBranchHead = LoadbranchMap.get(crrBranch);
        if (GivenBranchHistory.containsKey(CrrBranchHead)) {
            LoadbranchMap.put(crrBranch,GivenBranchHead);
            SaveLoadbranchMap(LoadbranchMap);
            //HMtrigger.save(LoadbranchMap,GetbranchFile().getParent(), BRANCH);
            throw new IllegalArgumentException("Current branch fast-forwarded.");
        }

        String splitPoint = FindSplitPoint(CrrBranchHead,CrrBranchHistory,GivenBranchHead,GivenBranchHistory);


        HashMap<String,String> splitPointBlob = GetBlobFiles(splitPoint);
        HashMap<String,String> crrPointBlob = GetBlobFiles(CrrBranchHead);
        HashMap<String,String> givenPointBlob = GetBlobFiles(GivenBranchHead);
        System.out.println(splitPointBlob);
        System.out.println(crrPointBlob);
        System.out.println(givenPointBlob);

        ArrayList<String> crrConflictList = new ArrayList<>();
        ArrayList<String> GivenConflictList = new ArrayList<>();

        HashMap<String,byte[]> Loadshatoblob = LoadshatoblobMap();
                //HMtrigger.load(GetshatoBlobFile().getParent(),SHATOBLOB);

        boolean isConflict = false;

        for (String fileInSplit : splitPointBlob.keySet()) {
            if (givenPointBlob.containsKey(fileInSplit) == true) { // given branch has the "SAME NAME" in fileinSplit
                if (splitPointBlob.get(fileInSplit).equals(givenPointBlob.get(fileInSplit)) == true) {}
                // (FOR ABOVE) present in Split and unchanged in given branch (same name same content)...  (NO.4, 5, 6!)
                else {
                    if (crrPointBlob.containsKey(fileInSplit) == false) { // absent in Current!  (NO.3!)
                        //TODO: Conflict Occurs
                        ConflictStorage(fileInSplit,null,givenPointBlob.get(fileInSplit));
                        isConflict = true;
                    }
                    else {
                        if (splitPointBlob.get(fileInSplit).equals(crrPointBlob.get(fileInSplit)) == true) { // unchanged from Split (or same as Split) (NO.2!)
                            // TODO: checkout from given branch and stage it!
                            checkAndStage(fileInSplit,Loadshatoblob.get(givenPointBlob.get(fileInSplit)));
                        }
                        else { // modified from Split (different from Split)
                            if (givenPointBlob.get(fileInSplit).equals(crrPointBlob.get(fileInSplit)) == true) {} // the same? Nothing! (NO.1-1!)
                            else {
                                //TODO: conflict (NO.1-2!!)
                                ConflictStorage(fileInSplit,null,givenPointBlob.get(fileInSplit));
                                isConflict = true;
                            }
                        }
                    }
                }
            }
            else {
                if (crrPointBlob.containsKey(fileInSplit) == false) {continue;}  // absent in current branch (NO.9!)
                else {
                    if (splitPointBlob.get(fileInSplit).equals(crrPointBlob.get(fileInSplit)) == true) { // the same as Split (NO.8!)
                        //TODO: remove + untrack!
                        removeCommand TriggerforRemove = new removeCommand();
                        TriggerforRemove.remove(Repo,fileInSplit);

                    }
                    else { // different from Split branch (NO.7!)
                        // TODO: conflict!
                        ConflictStorage(fileInSplit,null,givenPointBlob.get(fileInSplit));
                        isConflict = true;
                    }
                }
            }
            crrPointBlob.remove(fileInSplit);
            givenPointBlob.remove(fileInSplit);
        }
        // deal with files not existed in Split
        for (String fileInGiven : givenPointBlob.keySet()) { // deal with remaining files in given
            if (crrPointBlob.containsKey(fileInGiven) == false) { // not existed in crrBranch  (NO.11)
                //TODO: checkout + stage
                checkAndStage(fileInGiven,Loadshatoblob.get(givenPointBlob.get(fileInGiven)));
            }
            else {
                if (givenPointBlob.get(fileInGiven).equals(crrPointBlob.get(fileInGiven)) == true) {} // the same! (NO.12)
                else {
                    //TODO: conflict!  differentt document ()NO.13)
                    ConflictStorage(fileInGiven,null,givenPointBlob.get(fileInGiven));
                    isConflict = true;
                }
            }
            crrPointBlob.remove(fileInGiven);
            givenPointBlob.remove(fileInGiven);
        }
        for (String fileInCrr : crrPointBlob.keySet()) { // NO.10,   files only existed in crrBranch
            crrPointBlob.remove((fileInCrr));
        }

        if (isConflict == true) {
            System.out.println("Encountered a merge conflict.");
        }

        commitCommand CommitTrigger = new commitCommand();
        String commitMSG = "Merged "+ givenBranch + " into "+ crrBranch+ " .";
        CommitTrigger.commit(Repo,commitMSG);

    }

    public void ConflictStorage(String fileName, String crrBlobSHAcode, String GivenBlobSHAcode) {

        HashMap<String,byte[]> Loadshatoblob = LoadshatoblobMap();
                //HMtrigger.load(GetshatoBlobFile().getParent(),SHATOBLOB);
        String crrContent = "";
        if (crrBlobSHAcode != null) {
            crrContent = new String(Loadshatoblob.get(crrBlobSHAcode));
        }
        String GivenContent = "";
        if (GivenBlobSHAcode != null) {
            GivenContent = new String(Loadshatoblob.get(GivenBlobSHAcode));
        }

        String Allcontent = "<<<<<<< HEAD\n" +
                crrContent + " =======\n" +
                GivenContent + " >>>>>>>\n";

        byte[] contentByte = Allcontent.getBytes();
        File ff = new File(fileName);
        Utils.writeContents(ff, contentByte);
    }

    public void checkAndStage(String filename, byte[] contentOfGiven) {
        HashMap<String,byte[]> LoadStageMap = LoadStageMap();
                //HMtrigger.load(GetstageFile().getParent(),STAGE);
        LoadStageMap.put(filename,contentOfGiven);
        SaveLoadStageMap(LoadStageMap);
        //HMtrigger.save(LoadStageMap,GetstageFile().getParent(),STAGE);

        File ff = new File(filename);
        Utils.writeContents(ff, contentOfGiven);
    }




    public String FindSplitPoint(String CrrBranchHead,HashMap<String,String> CrrBranchHistory,
                                 String GivenBranchHead, HashMap<String,String>GivenBranchHistory) {

        String SplitPoint = null;
        while (true) {
            if (GivenBranchHistory.containsKey(CrrBranchHead) == false) {
                CrrBranchHead = CrrBranchHistory.get(CrrBranchHead);
            }
            else {SplitPoint = CrrBranchHead; break;}
        }

        if (SplitPoint == null) {
            throw new IllegalArgumentException("Bug existed in FindSplitPoint Method");
        }
        return  SplitPoint;
    }


    @Override
    public boolean WrongOperands(ArrayList<String> Operands) {return Operands.size() != 1;}

    @Override
    public boolean NeedRepo() {return true;}
}
