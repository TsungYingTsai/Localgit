package gitlet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by intel66 on 12/25/2017.
 */
public class Repository {

    private Path _workingDir;
    private Path _gitletDir;
    private Path _commitDir;
    private Path _blobsDir;

    private Path _branch;
    private Path _stage;
    private Path _track;
    private Path _blob;
    private Path _head;
    private Path _committree;
    private Path _shatocm;
    private Path _delete;
    private Path _shatoblob;
    private Path _crrbranch;

    public final String MASTER = "master";
    public final String BRANCH = "branch";
    public final String STAGE = "stage";
    public final String TRACK = "track";
    public final String BLOB = "blobb";
    public final String HEAD = "head";
    public final String COMMITTREE = "committree";
    public final String SHATOCM = "shatocm";
    public final String DELETE = "delete";
    public final String SHATOBLOB = "shatoblob";
    public final String CRRBRANCH = "crrbranch";

    protected Translate<HashMap> HMtrigger;
    protected Translate<String> STtrigger;



    private boolean _gitletDirOpen;

    Repository () {

        this._workingDir = Paths.get(System.getProperty("user.dir"));
        this._gitletDir = _workingDir.resolve(".gitlet");
        this._commitDir = _gitletDir.resolve("commit");
        this._blobsDir = _gitletDir.resolve("blobs");

        this.HMtrigger = new Translate<>();
        this.STtrigger = new Translate<>();



        this._branch = _gitletDir.resolve(BRANCH);
        this._stage = _commitDir.resolve(STAGE);
        this._track = _commitDir.resolve(TRACK);
        this._blob = _blobsDir.resolve(BLOB);
        this._head = _gitletDir.resolve(HEAD);
        this._shatocm = _gitletDir.resolve(SHATOCM);
        this._committree = _gitletDir.resolve(COMMITTREE);
        this._delete = _commitDir.resolve(DELETE);
        this._shatoblob = _blobsDir.resolve(SHATOBLOB);
        this._crrbranch = _gitletDir.resolve(CRRBRANCH);

        this._gitletDirOpen = (Files.exists(_gitletDir));

    }

    public boolean GetgitletDirOpen() {return this._gitletDirOpen;}

    public Path GetworkingDir() {return this._workingDir;}
    public Path GetgitletDir() {return this._gitletDir;}
    public Path GetcommitDir() {return this._commitDir;}
    public Path GetblobsDir() {return this._blobsDir;}

    public Path GetbranchFile() {return this._branch;}
    public Path GetheadFile() {return this._head;}
    public Path GetstageFile() {return this._stage;}
    public Path GettrackFile() {return this._track;}
    public Path GetblobFile() {return this._blob;}
    public Path GetcommittreeFile() {return this._committree;}
    public Path GetshatocmFile() {return this._shatocm;}
    public Path GetdeleteFile() {return this._delete;}
    public Path GetshatoBlobFile() {return this._shatoblob;}
    public Path GetcrrBranchName() {return this._crrbranch;}

    //public boolean is_Delete() {return this._isDelete;}

    public Translate<HashMap> getHMTrigger() {return this.HMtrigger;}

    //public void isDeleteopen() {this._isDelete = true;}
    //public void isDeleteclose() {this._isDelete = false;}

    /**
     * TODO: initCommand Method
     */
    public void initialize() {

        // Ready to test if .gitlet if existed
        if (_gitletDirOpen == true) {
            throw new IllegalStateException(
                    "A gitlet version-control system already exists in the current directory.");
        }
        try {
            Files.createDirectory(_gitletDir);
            Files.createDirectory(_commitDir);
            Files.createDirectory(_blobsDir);

        } catch (IOException e2) {
            throw new IllegalArgumentException("some Directory has been created");
        }
        /**
         * construct initial Commit for init
         */
        String initMSG = "initial commit";
        String initParent = null;
        Date initTime = new Date();
        HashMap<String, String> blobMap = new HashMap<>();


        //Commit initialCommit = new Commit(initMSG,initParent,initTime,blobMap, TrackMap);
        Commit initialCommit = new Commit(initMSG,initParent,initTime,blobMap);

/**
 * construct blob, file to "real content"
 */
/*
        HashMap<String, String> blobMap = new HashMap<>();
        HMtrigger.save(blobMap,_blob.getParent(), BLOB);
        */
        /**
         *  constuct <SHAcodeOfContent : byte[] content>, for blobs
         */
        HashMap<String,byte[]> shatoblobMap = new HashMap<>();
        HMtrigger.save(shatoblobMap,_shatoblob.getParent(),SHATOBLOB);

        /**
         * construct SHA code for init Commit
         */
        String init_ShaCM = TurnSHA.Tosha1(initialCommit);
        //String init_ShaCM = TurnSHA.Tosha1("LOL");
        //String init_ShaCM = Utils.sha1(initialCommit.toString());
        //String init_ShaCM = null;
        /**
         * construct Branch to save any branch needed, and set "master" as default one
         */
        HashMap<String,String> branchMap = new HashMap<>();
        branchMap.put(MASTER,init_ShaCM);
        HMtrigger.save(branchMap,_branch.getParent(), BRANCH);

        /**
         * to point out the current Branch!
         */
        String crrBranch = MASTER;
        STtrigger.save(crrBranch,_crrbranch.getParent(),CRRBRANCH);

/**
 * construct HEAD to point out current commit
 */
        STtrigger.save(init_ShaCM,_head.getParent(),HEAD);

/**
 * contrsuct committree
 */

        HashMap<String,String> committreeMap = new HashMap<>();
        committreeMap.put(init_ShaCM,null);
        HMtrigger.save(committreeMap,_committree.getParent(), COMMITTREE);

/**
 * contrsuct ShaToCm_tree from SHA- code to get the Commit information
 */
        HashMap<String,Commit> shatocmMap = new HashMap<>();
        shatocmMap.put(init_ShaCM,initialCommit);
        HMtrigger.save(shatocmMap,_shatocm.getParent(), SHATOCM);

/**
 * contrsuct Stage -- ready for commit
 */
        HashMap<String,byte[]> StageMap = new HashMap<>();
        HMtrigger.save(StageMap,_stage.getParent(), STAGE);

/**
 * construct Remove to record deleted files
 */
        HashMap<String,byte[]> deleteMap = new HashMap<>();
        HMtrigger.save(deleteMap,_delete.getParent(), DELETE);
    }



    // recall WorkingDIR's all txt document in the form of (filename, content in byte[])
    public HashMap<String,byte[]> WorkingDirPathList() {
        HashMap<String,byte[]> txtList = new HashMap<>();
        try {
            for (Path entry : Files.newDirectoryStream(GetworkingDir())) {
                if (Files.isDirectory(entry) == false && entry.getFileName().toString().endsWith(".txt")) {
                    txtList.put(entry.getFileName().toString(), Files.readAllBytes(entry));
                }
            }
        } catch (IOException excp) {throw new IllegalArgumentException("not in the valid workng directory.");}
        return txtList;
    }

    // return txt hashmap in WorkingDir not tracked in crrTrack (filename -> content in byte[])
    public HashMap<String,byte[]> WRDIRtxtUntrackinCrrBranch() {
        //Commit CMtrigger = new Commit();
        /*
        //HashMap<String,String> crrCMBlob = new HashMap<>();
        HashMap<String,String> LoadbranchMap = HMtrigger.load(GetbranchFile().getParent(),BRANCH);
        String crrBranch = STtrigger.load(GetcrrBranchName().getParent(),CRRBRANCH);
        HashMap<String,Commit> LoadshatocmMap = HMtrigger.load(GetshatocmFile().getParent(),SHATOCM);
        Commit crrCommit =  LoadshatocmMap.get(LoadbranchMap.get(crrBranch));
*/

        HashMap<String,String> crrBlobFileToContentMp = GetCrrBranchBlobInfo();
        HashMap<String,byte[]> LoaddeleteMP = HMtrigger.load(GetdeleteFile().getParent(),DELETE);

        System.out.println("crrBlobFileToContentMp:   "+crrBlobFileToContentMp);
        HashMap<String,byte[]> WorkingDirPathList = WorkingDirPathList();
        HashMap<String,byte[]> UNtrackedMP = new HashMap<>();
        for (String keys : WorkingDirPathList.keySet()) {
            UNtrackedMP.put(keys,WorkingDirPathList.get(keys));
        }

        for (String keys : WorkingDirPathList.keySet()) {
            if (crrBlobFileToContentMp.containsKey(keys)) {
                if //(new String(crrBlobFileToContentMp.get(keys)).equals(new String(WorkingDirPathList.get(keys))))
                 (LoaddeleteMP.keySet().contains(keys) == false){
                    //TrackedinCrr.put(keys, WorkingDirPathList.get(keys));
                    UNtrackedMP.remove(keys);
                }
            }
        }

        // after filter the remaining WorkingList are those withour tracking
        //this._TrackedinCrr = TrackedinCrr;
System.out.println("UNtracked Map is :  " + UNtrackedMP);
        return UNtrackedMP;
    }

    public HashMap<String,String> GetCrrBranchBlobInfo() { // get the current branch head imformaton... and get Commit data Filename -> FIleContentInSHA
        HashMap<String, String> LoadbranchMap = HMtrigger.load(GetbranchFile().getParent(), BRANCH);
        String crrBranch = STtrigger.load(GetcrrBranchName().getParent(), CRRBRANCH);
        HashMap<String, Commit> LoadshatocmMap = HMtrigger.load(GetshatocmFile().getParent(), SHATOCM);
        Commit crrCommit = LoadshatocmMap.get(LoadbranchMap.get(crrBranch));

        HashMap<String,String> crrCommitBlobMP = crrCommit.GetCMBlobInfo();
        return crrCommitBlobMP;
    }

    public HashMap<String,byte[]> CrrBlobFileToContent() { // same as above, but get the real content in txt fille
        HashMap<String,byte[]> BlobFileToNamw = new HashMap<>();
        HashMap<String,String> crrCommitBlobMP = GetCrrBranchBlobInfo();
        HashMap<String,byte[]> LoadShaToblobP = HMtrigger.load(GetshatoBlobFile().getParent(),SHATOBLOB);
        for (String key: crrCommitBlobMP.keySet()) {
            BlobFileToNamw.put(key,LoadShaToblobP.get(crrCommitBlobMP.get(key)));
        }
        return BlobFileToNamw;
    }

    public HashMap<String,String> GetBlobFiles(String CommitSHAcode) { // get blob info from SHA code.... file -> contentSHA

        HashMap<String, Commit> LoadshatocmMap = HMtrigger.load(GetshatocmFile().getParent(), SHATOCM);
        Commit CommitContent = LoadshatocmMap.get(CommitSHAcode);

        HashMap<String,String> CommitBlobMP = CommitContent.GetCMBlobInfo();
        /*
        HashMap<String,byte[]> BlobFileToNamw = new HashMap<>();
        HashMap<String,byte[]> LoadShaToblobP = HMtrigger.load(GetshatoBlobFile().getParent(),SHATOBLOB);

        for (String key: CommitBlobMP.keySet()) {
            BlobFileToNamw.put(key,LoadShaToblobP.get(CommitBlobMP.get(key)));
        }
        */
        return CommitBlobMP;
    }

    public HashMap<String,String> BranchCommitHistory(String BranchName) {
        HashMap<String,String> historyList = new HashMap<>();
        HashMap<String,String> LoadbranchMap = HMtrigger.load(GetbranchFile().getParent(),BRANCH);
        HashMap<String,Commit> LoadshatocmMap = HMtrigger.load(GetshatocmFile().getParent(),SHATOCM);
        System.out.println("The Commit history is for  ... " + BranchName +" branch");
        String headSHA = LoadbranchMap.get(BranchName);

        while (true) {
            //System.out.println(headSHA);
            Commit headCommit = LoadshatocmMap.get(headSHA);
            String parentSHA = headCommit.GetParent();
            historyList.put(headSHA,parentSHA);
            if (parentSHA == null) {break;}
            headSHA = parentSHA;
        }
        return historyList;
    }

    public HashMap<String,byte[]> LoaddeleteMap() {
        return HMtrigger.load(GetdeleteFile().getParent(),DELETE);
    }

    public HashMap<String,byte[]> LoadStageMap() {
        return HMtrigger.load(GetstageFile().getParent(),STAGE);
    }

    public HashMap<String,String> LoadblobMap() {
        return GetCrrBranchBlobInfo();
    }

    public HashMap<String,String> LoadbranchMap() {
        return HMtrigger.load(GetbranchFile().getParent(),BRANCH);
    }

    public HashMap<String,Commit> LoadshatocmMap() {
        return HMtrigger.load(GetshatocmFile().getParent(),SHATOCM);
    }

    public HashMap<String,byte[]> LoadshatoblobMap() {
        return HMtrigger.load(GetshatoBlobFile().getParent(),SHATOBLOB);
    }

    public HashMap<String,String> LoadcommittreeMap() {
        return HMtrigger.load(GetcommittreeFile().getParent(),COMMITTREE);
    }

    public String LoadCMheadSHA() {
        return STtrigger.load(GetheadFile().getParent(),HEAD);
    }

    public String LoadcrrBranch() {
        return STtrigger.load(GetcrrBranchName().getParent(),CRRBRANCH);
    }

    //TODO: -----------------------------------------------------------------

    public void SaveLoadStageMap(HashMap<String,byte[]> LoadStageMap) {
        HMtrigger.save(LoadStageMap,GetstageFile().getParent(),STAGE);
    }

    public void SaveLoadDeleteMap(HashMap<String,byte[]> LoadDeleteMap) {
        HMtrigger.save(LoadDeleteMap, GetdeleteFile().getParent(), DELETE);
    }

    public void SaveLoadshatoblobMap(HashMap<String,byte[]> LoadshatoblobMap) {
        HMtrigger.save(LoadshatoblobMap,GetshatoBlobFile().getParent(),SHATOBLOB);
    }

    public void SaveLoadCMheadSHA(String LoadCMheadSHA) {
        STtrigger.save(LoadCMheadSHA,GetheadFile().getParent(),HEAD);
    }

    public void SaveLoadbranchMap(HashMap<String,String> LoadbranchMap) {
        HMtrigger.save(LoadbranchMap,GetbranchFile().getParent(),BRANCH);
    }

    public void SaveLoadcommittreeMp(HashMap<String,String> LoadcommittreeMap) {
        HMtrigger.save(LoadcommittreeMap,GetcommittreeFile().getParent(),COMMITTREE);
    }

    public void SaveLoadshatocmMap(HashMap<String,Commit> LoadshatocmMap) {
        HMtrigger.save(LoadshatocmMap,GetshatocmFile().getParent(),SHATOCM);
    }

    public void SaveLoadCrrBranchName(String TarBranchName) {
        STtrigger.save(TarBranchName,GetcrrBranchName().getParent(),CRRBRANCH);
    }

}
