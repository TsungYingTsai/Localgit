package gitlet;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by intel66 on 12/25/2017.
 */
public class Commit extends Repository implements Serializable{

    private String _message;
    private String _parent;
    private Date _currentTime;
    private HashMap<String,String> _filenameToblobSHA; // <Filename, content SHA-1 code>

    Commit(String message, String parent, Date currentTime, HashMap<String,String> filenameToblobSHA) {
       if (message == null || message.length() == 0 || message == "") {
            throw new IllegalArgumentException("Please enter a commit message.");
        }

        _message = message;
        _parent = parent;
        _currentTime = currentTime;
        _filenameToblobSHA = filenameToblobSHA;
        //_trackCM = SHAtoTrack;
    }

    public String GetMessage() {return this._message;}
    public String GetParent() {return this._parent;}
    public Date GetCurrentTime() {return this._currentTime;}
    public HashMap<String,String> GetCMBlobInfo() {return this._filenameToblobSHA;}
    //public HashMap<String,byte[]> Gettrackhis() {return this._trackCM;}


    public String toPrint() {
        Date pp = this._currentTime;
        SimpleDateFormat ft =
                new SimpleDateFormat ("EEE MMM d HH:mm:ss yyyy Z");
        String dateStr = ft.format(pp);
        return "===\n" + "Commit " + TurnSHA.Tosha1(this) + "\n"
                + "Date: " + dateStr + "\n" + this._message + "\n";
    }

    // read txt content
    public String ReadTXTcontent(String filename) {
        Path filepath = Paths.get(System.getProperty("user.dir")).resolve(filename);
        HashMap<String,String> fileToSha = new HashMap<>();
        try {
            String Content = new String(Files.readAllBytes(filepath));
            String SHAcode= TurnSHA.Tosha1(Content);
            fileToSha.put(filename,SHAcode);
            return SHAcode;
        } catch (IOException excp) {throw new IllegalArgumentException("file not existed in targeted Path");}
    }

}
