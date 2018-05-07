package gitlet;

import java.util.ArrayList;

/**
 * Created by intel66 on 12/25/2017.
 */
public interface CommandInterface {
    public void runTheAction(Repository Repo, ArrayList<String> Operands);

    public boolean WrongOperands(ArrayList<String> Operands);

    public boolean NeedRepo();
}
