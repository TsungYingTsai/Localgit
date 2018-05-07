package gitlet;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 * Created by intel66 on 12/25/2017.
 */
public class TurnSHA implements Serializable{
    static String Tosha1(Object vals){

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(vals);

            byte[] valsByte = md.digest(bos.toByteArray());
            Formatter result = new Formatter();
            for (byte b : valsByte) {
                result.format("%02x", b);
            }

            return result.toString();
        } catch (IOException excp) {
            throw new IllegalArgumentException("System does not support SHA-1");
        } catch (NoSuchAlgorithmException ee) {
            throw new IllegalArgumentException("System does not support SHA-1.!!!");
        }
    }

}
