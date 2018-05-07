package gitlet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by intel66 on 12/25/2017.
 */
public class CommandController {
    private HashMap<String, CommandInterface> CommandList;
    public CommandController() {this.CommandList = new HashMap<>();}


    // if I have to add each execution and to write their own command, it will be very repeated and redundant. So use CommandInterface here !
    public void addCommandFromArgs (String InputArgs, CommandInterface ActionByInput) {
        this.CommandList.put(InputArgs, ActionByInput);
    }

    // execute the action requested from args
    public void execute (Repository Repo, String[] FromArgs) {

        // execute the Main Action

        // execute the detail Action from operands


        if (FromArgs == null || FromArgs.length == 0 ) {
            throw new IllegalArgumentException("Please enter a command.");
        }

        String InputArgs = FromArgs[0];
        CommandInterface theCommand = this.CommandList.get(InputArgs);

        ArrayList<String> Operands = new ArrayList<>();
        for (int i = 1; i < FromArgs.length ; i += 1) {Operands.add(FromArgs[i]);}



        if (theCommand.WrongOperands(Operands) == true) {
            throw new IllegalArgumentException("Incorrect operands.");
        }

        if (theCommand == null) {
            throw new IllegalArgumentException(
                    "No command with that name exists.");
        }



        if (theCommand.NeedRepo() && Repo.GetgitletDirOpen() == false) {
            throw new IllegalStateException(
                    "Not in an initialized gitlet directory.");
        }

        // run the Action for Operands!
        theCommand.runTheAction(Repo, Operands);


    }
}
