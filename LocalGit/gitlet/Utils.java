package gitlet;

import java.io.*;
import java.nio.file.Files;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import java.nio.file.Path;
import java.nio.file.Paths;

/** Assorted utilities.
 *  @author P. N. Hilfinger
 */
class Utils {

    public static void main(String... args) {

        //define the fix path
        Path base_1 = Paths.get(System.getProperty("user.dir"));
        //Path base_2 = Paths.get(System.getProperty("user.dir"));
        System.out.println(base_1);


        //resolve Topic.txt file
        Path path_1 = base_1.resolve("Topic457654");
        System.out.println(path_1.toString());

        // TODO: 創建資料夾
        try {
            Files.createDirectories(path_1);
        } catch (IOException e) {
            //fail to create directory
            e.printStackTrace();
        }



        //resolve Demo.txt file
        Path path_2 = base_1.resolve("Demo");
        System.out.println(path_2.toString());
        System.out.println(path_1.getParent());




        System.out.println("test restrictedDelete");

        byte[] bytelist = new byte[] {1,101,102};
        String aa = Utils.sha1(bytelist);
        System.out.println(aa);

        System.out.println("finish testing restrictedDelete");




        Path TheworkingDir = Paths.get(System.getProperty("user.dir"));
        Path filePathRR = TheworkingDir.resolve("QQAQQ.txt");
        try {
            byte[] AAQAA = Files.readAllBytes(filePathRR);
            File ff = new File("WTF.txt");
            Utils.writeContents(ff,AAQAA);
            System.out.println(AAQAA.equals(Files.readAllBytes(TheworkingDir.resolve("WTF.txt"))));
        } catch (IOException i) {throw new IllegalArgumentException("lkjhgfyujnbjhhjkk");}

        byte[] RERE = "REBABY".getBytes();





        System.out.println("test write and read content");
        byte[] bytelist_2 = "kjhgfghjkjhg".getBytes();
        File xcv = new File("oopp.txt");
        Utils.writeContents(xcv,bytelist_2);
        File xcv_2 = new File("oopp_2.txt");
        Utils.writeContents(xcv_2,bytelist_2);
        byte[] Byte_xcv = Utils.readContents(xcv);
        byte[] Byte_xcv_2 = Utils.readContents(xcv_2);
        System.out.println(Byte_xcv);
        System.out.println(Byte_xcv_2);
        System.out.println(xcv == xcv_2);



        System.out.println("test plainFilenamesIn");
        Path PFI_1 = Paths.get(System.getProperty("user.dir"));
        List<String> plainFilenamesInTest = Utils.plainFilenamesIn("gitlet");
        System.out.println(plainFilenamesInTest);
        System.out.println(plainFilenamesInTest.get(3));
        String pl = " Main.java";
        System.out.println(plainFilenamesInTest.get(3) == pl);


    }

    /* SHA-1 HASH VALUES. */

    /** Returns the SHA-1 hash of the concatenation of VALS, which may
     *  be any mixture of byte arrays and Strings. */
    //TODO:  byte array or stings 的混合物
    static String sha1(Object... vals) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            for (Object val : vals) {
                if (val instanceof byte[]) {
                    md.update((byte[]) val);
                } else if (val instanceof String) {
                    md.update(((String) val).getBytes(StandardCharsets.UTF_8));
                } else {
                    throw new IllegalArgumentException("improper type to sha1");
                }


            }
            Formatter result = new Formatter();
            for (byte b : md.digest()) {
                result.format("%02x", b);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException excp) {
            throw new IllegalArgumentException("System does not support SHA-1");
        }
    }

    /** Returns the SHA-1 hash of the concatenation of the strings in
     *  VALS. */
    static String sha1(List<Object> vals) {
        return sha1(vals.toArray(new Object[vals.size()]));
    }

    /* FILE DELETION */

    /** Deletes FILE if it exists and is not a directory.  Returns true
     *  if FILE was deleted, and false otherwise.  Refuses to delete FILE
     *  and throws IllegalArgumentException unless the directory designated by
     *  FILE also contains a directory named .gitlet. */
    //TODO 如果不是資料夾且存在 --> 刪除且回傳TRUE;
    static boolean restrictedDelete(File file) {
        if (!(new File(file.getParentFile(), ".gitlet")).isDirectory()) {
            throw new IllegalArgumentException("not .gitlet working directory");
        }
        if (!file.isDirectory()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /** Deletes the file named FILE if it exists and is not a directory.
     *  Returns true if FILE was deleted, and false otherwise.  Refuses
     *  to delete FILE and throws IllegalArgumentException unless the
     *  directory designated by FILE also contains a directory named .gitlet. */
    static boolean restrictedDelete(String file) {
        return restrictedDelete(new File(file));
    }

    /* READING AND WRITING FILE CONTENTS */

    /** Return the entire contents of FILE as a byte array.  FILE must
     *  be a normal file.  Throws IllegalArgumentException
     *  in case of problems. */
    static byte[] readContents(File file) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("must be a normal file");
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /** Write the entire contents of BYTES to FILE, creating or overwriting
     *  it as needed.  Throws IllegalArgumentException in case of problems. */
    static void writeContents(File file, byte[] bytes) {
        try {
            if (file.isDirectory()) {
                throw
                        new IllegalArgumentException("cannot overwrite directory");
            }
            Files.write(file.toPath(), bytes);
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /* DIRECTORIES */

    /** Filter out all but plain files. */
    private static final FilenameFilter PLAIN_FILES =
            new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return new File(dir, name).isFile();
                }
            };

    /** Returns a list of the names of all plain files in the directory DIR, in
     *  lexicographic order as Java Strings.  Returns null if DIR does
     *  not denote a directory. */
    static List<String> plainFilenamesIn(File dir) {
        String[] files = dir.list(PLAIN_FILES);
        if (files == null) {
            return null;
        } else {
            Arrays.sort(files);
            return Arrays.asList(files);
        }
    }

    /** Returns a list of the names of all plain files in the directory DIR, in
     *  lexicographic order as Java Strings.  Returns null if DIR does
     *  not denote a directory. */
    static List<String> plainFilenamesIn(String dir) {
        return plainFilenamesIn(new File(dir));
    }

}
