package gitlet;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by intel66 on 12/25/2017.
 */
public class UnitTest extends Repository{

    @Test
    public void bytetpp(){
        Commit QAQ = new Commit("KJHUI",null,null,null);
        System.out.println(Paths.get(System.getProperty("user.dir")));
        Path jjii = Paths.get(System.getProperty("user.dir"));
        byte[] apl = new byte[]{2,3,4};
        HashMap<byte[],byte[]> opop = new HashMap<>();
        opop.put(apl,apl);
        String POP = TurnSHA.Tosha1(QAQ);
        System.out.println(POP);
    }

    @Test
    public void plainFilenamesInTest() {
        List<String> plainFilenamesInTest = Utils.plainFilenamesIn("gitlet");
        System.out.println(plainFilenamesInTest);
        System.out.println(plainFilenamesInTest.get(3));
        String pl = " Main.java";
        System.out.println(plainFilenamesInTest.get(3).equals(pl));
    }

    @Test
    public void testDeleteFile() {
        Path target = Paths.get(System.getProperty("user.dir")).resolve("11.txt");
        if (!Files.isDirectory(target)) {
            try {
                Files.delete(target);
            } catch (IOException i) {
                throw new IllegalArgumentException("KJHGG");
            }
        }
    }

    @Test
    public void ConflictTest() {
        ConflictStorage("QWE.txt",null,null);
    }

    @Test
    public void WriteTest() {
        byte[] contentByte = "!!!!!".getBytes();
        File ff = new File("oiioio");
        Utils.writeContents(ff, contentByte);
    }

    public void ConflictStorage(String fileName, String crrBlobSHAcode, String GivenBlobSHAcode) {

        //HashMap<String,byte[]> Loadshatoblob = HMtrigger.load(GetshatoBlobFile().getParent(),SHATOBLOB);
        String crrContent = "";
        if (crrBlobSHAcode != null) {
            //crrContent = new String(Loadshatoblob.get(crrBlobSHAcode));
        }
        String GivenContent = "";
        if (GivenBlobSHAcode != null) {
            //GivenContent = new String(Loadshatoblob.get(GivenBlobSHAcode));
        }

        String Allcontent = "<<<<<<< HEAD\n" +
                crrContent + " =======\n" +
                GivenContent + " contents of file in given branch>>>>>>>\n";

        byte[] contentByte = Allcontent.getBytes();
        File ff = new File(fileName);
        Utils.writeContents(ff, contentByte);
    }

    @Test
    public void igaga() {
        List<Integer> trytry = new ArrayList<>();
        trytry.add(1);
        trytry.add(2);
        trytry.add(3);
        trytry.add(4);
        for (int i = 0; i < trytry.size(); i++) {
            System.out.println(i);
        }
        System.out.println("try ++i");
        for (int i = 0; i < trytry.size(); ++i) {
            System.out.println(i);
            System.out.println(i);
        }
    }
}
