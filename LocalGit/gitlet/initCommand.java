package gitlet;

import java.util.ArrayList;

/**
 * Created by intel66 on 12/25/2017.
 */
public class initCommand implements CommandInterface{
    @Override
    public void runTheAction(Repository Repo, ArrayList<String> Operands) {
        Repo.initialize();
    }

    @Override
    public boolean WrongOperands(ArrayList<String> Operands) {return Operands.size() != 0;}

    @Override
    public boolean NeedRepo() {return false;}

}
