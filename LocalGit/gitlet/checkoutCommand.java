package gitlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by intel66 on 12/21/2017.
 */
public class checkoutCommand extends Repository implements CommandInterface {

    private HashMap<String,String> _TrackedinCrr;
    @Override
    public void runTheAction(Repository Repo, ArrayList<String> Operands) {
        //Commit CMtrigger = new Commit();
        if (Operands.size() == 1) {
            String TarBranchName = Operands.get(0);
            HashMap<String, String> LoadbranchMap = LoadbranchMap();
            String LoadCMheadSHA = LoadCMheadSHA();
            if (LoadbranchMap.containsKey(TarBranchName) == false) {
                throw new IllegalArgumentException("No such branch exists.");
            }
            String crrBranch = LoadcrrBranch();
            if (TarBranchName.equals(crrBranch)) {
                throw new IllegalArgumentException("No need to checkout the current branch.");
            }

            if (cantRunCheckoutBranch(TarBranchName)) {
                throw new IllegalArgumentException("There is an untracked file in the way; delete it or add it first.");
            }

            HashMap<String,String> LoadcrrBlobMP = GetCrrBranchBlobInfo();
            HashMap<String, String> TrackedinCrr = LoadcrrBlobMP;

            HashMap<String,byte[]> LoaddeleteMap = LoaddeleteMap();

            // delete all txt tracked in crrBranch
            for (String filename : TrackedinCrr.keySet()) {
                Path target = Paths.get(System.getProperty("user.dir")).resolve(filename);
                if (Files.exists(target) && !Files.isDirectory(target)) {
                    try {
                        Files.delete(target);
                    } catch (IOException i) {
                        throw new IllegalArgumentException("KJHGG");
                    }
                }
            }


            // put all document in target Branch in workingDir
            //HashMap<String,String> LoadbranchMap = HMtrigger.load(GetbranchFile().getParent(),BRANCH);
            //String crrBranch = STtrigger.load(GetcrrBranchName().getParent(),CRRBRANCH);
            HashMap<String,Commit> LoadshatocmMap = LoadshatocmMap();
            Commit tarBranchCommit =  LoadshatocmMap.get(LoadbranchMap.get(TarBranchName));

            HashMap<String,String> tarCommitMp = tarBranchCommit.GetCMBlobInfo();
            HashMap<String,byte[]> Loadshatoblob = LoadshatoblobMap();


            for (String insertFileName : tarCommitMp.keySet()) {
                byte[] contentByte = Loadshatoblob.get(tarCommitMp.get(insertFileName));
                File ff = new File(insertFileName);
                Utils.writeContents(ff, contentByte);
            }

            SaveLoadCrrBranchName(TarBranchName);
            SaveLoadbranchMap(LoadbranchMap);
            //STtrigger.save(LoadbranchMap.get(TarBranchName),GetheadFile().getParent(), HEAD);


            HashMap<String,byte[]> LoadStageMap = LoadStageMap();
                    //HMtrigger.load(GetstageFile().getParent(),STAGE);
            LoadStageMap.clear();
            SaveLoadStageMap(LoadStageMap);
            //HMtrigger.save(LoadStageMap,GetstageFile().getParent(),STAGE);


            LoaddeleteMap.clear();
            SaveLoadDeleteMap(LoaddeleteMap);
            //HMtrigger.save(LoaddeleteMap,GetstageFile().getParent(),DELETE);



        } else if (Operands.size() == 2) {

            String filename = Operands.get(1);
            //String LoadheadSHA = STtrigger.load(GetheadFile().getParent(),HEAD);
            HashMap<String,Commit> LoadshatocmMap = LoadshatocmMap();
            HashMap<String,byte[]> LoadshatoBlobMap = LoadshatoblobMap();
                    //HMtrigger.load(GetshatoBlobFile().getParent(),SHATOBLOB);
            HashMap<String,byte[]> LoadStageMap = LoadStageMap();
                    //HMtrigger.load(GetstageFile().getParent(),STAGE);

            HashMap<String,byte[]> CrrBlobFileToContent = CrrBlobFileToContent();
            if (CrrBlobFileToContent.containsKey(filename) == false) {throw new IllegalArgumentException("File does not exist in that commit.");}
            else{
                File ff = new File(filename);
                Utils.writeContents(ff,CrrBlobFileToContent.get(filename));
            }

            LoadStageMap.remove(filename);
            SaveLoadStageMap(LoadStageMap);
            //HMtrigger.save(LoadStageMap,GetstageFile().getParent(),STAGE);

            /*
            Commit headCommit = LoadshatocmMap.get(LoadheadSHA);
            HashMap<String,String> headBlobSHA = headCommit.GetShaBlobName();
            HashMap<String,String> LoadBlobMap = LoadshatoBlobMap.get(headBlobSHA);
            if (LoadBlobMap.containsKey(filename) == false) {throw new IllegalArgumentException("File does not exist in that commit.");}
            else {
                String contentString = LoadBlobMap.get(filename);
                String writeContent = contentString;
                File ff = new File(filename);
                Utils.writeContents(ff,writeContent.getBytes());
            }
            if (LoadStageMap.containsKey(filename)) {
                LoadStageMap.remove(filename);
                HMtrigger.save(LoadStageMap,GetstageFile().getParent(),STAGE);
            }
*/

        } else if (Operands.size() == 3) {

            String commitID = Operands.get(0);
            String filename = Operands.get(2);

            HashMap<String,Commit> LoadshatocmMap = LoadshatocmMap();
                    //HMtrigger.load(GetshatocmFile().getParent(),SHATOCM);

            if (LoadshatocmMap.containsKey(commitID)) {

                Commit IDCommit = LoadshatocmMap.get(commitID);
                HashMap<String,String> IDFilenametoSHA = IDCommit.GetCMBlobInfo();

                HashMap<String,byte[]> LoadshatoBlobMap = LoadshatoblobMap();
                        //HMtrigger.load(GetshatoBlobFile().getParent(),SHATOBLOB);
                if (IDFilenametoSHA.containsKey(filename) == false) {throw new IllegalArgumentException("File does not exist in that commit.");}
                else{
                    File ff = new File(filename);
                    Utils.writeContents(ff,LoadshatoBlobMap.get(IDFilenametoSHA.get(filename)));
                }

                HashMap<String,byte[]> LoadStageMap = LoadStageMap();
                        //HMtrigger.load(GetstageFile().getParent(),STAGE);
                LoadStageMap.remove(filename);
                SaveLoadStageMap(LoadStageMap);
                //HMtrigger.save(LoadStageMap,GetstageFile().getParent(),STAGE);
                //   duplication finish!


            } else {
                throw new IllegalArgumentException("No commit with that id exists.");
            }
        } //else {throw new IllegalArgumentException("should be banned by WrongOperand method.");}
    }

    @Override
    public boolean WrongOperands(ArrayList<String> Operands) {
        return (Operands.size() == 1
                || (Operands.size() == 2 && Operands.get(0).equals("--"))
                || (Operands.size() == 3 && Operands.get(1).equals("--"))) == false;
    }

    @Override
    public boolean NeedRepo() {return true;}

    // recall WorkingDIR's all txt document in the form of (filename, content in STRING)


    //Iff a working file is untracked in the current branch and would be overwritten or deleted by the checkout
    public boolean cantRunCheckoutBranch(String targetBranch) {
        // if all workingDir txt tracked in crrBranch, then will not occur problem during checkout
        if (WRDIRtxtUntrackinCrrBranch().size() == 0) {return false;}
        HashMap<String,byte[]> notTrackedinCrrList = WRDIRtxtUntrackinCrrBranch();

        // overwritten == same filename in checkout branch
        boolean overwritten = false;

        HashMap<String,String> LoadbranchMap = LoadbranchMap();
                //HMtrigger.load(GetbranchFile().getParent(),BRANCH);
        //String crrBranch = STtrigger.load(GetcrrBranchName().getParent(),CRRBRANCH);
        HashMap<String,Commit> LoadshatocmMap = LoadshatocmMap();
                //HMtrigger.load(GetshatocmFile().getParent(),SHATOCM);

        Commit tarBranchCommit =  LoadshatocmMap.get(LoadbranchMap.get(targetBranch));

        HashMap<String,String> tarCommitMp = tarBranchCommit.GetCMBlobInfo();

        for (String key : notTrackedinCrrList.keySet()) {
            if (tarCommitMp.containsKey(key)) {
                overwritten = true;
                System.out.println("untracked txt will be overwritten!.");
            }
        }

        // Delete == tracked in crrBranch && not in checkout branch
        boolean _delete = false; // weird, the txt here must be untracked, impossible existed here with tracked condition

        return overwritten;
    }

}
