package gitlet;

/**
 * Created by intel66 on 12/25/2017.
 */
public class Main {
    public static void main(String... args) {

        CommandController Controller = new CommandController();
        Controller.addCommandFromArgs("init",new initCommand());
        Controller.addCommandFromArgs("add",new addCommand());
        Controller.addCommandFromArgs("rm",new removeCommand());
        Controller.addCommandFromArgs("commit",new commitCommand());
        Controller.addCommandFromArgs("log",new logCommand());
        Controller.addCommandFromArgs("global-log",new globallogCommand());
        Controller.addCommandFromArgs("find",new findCommand());
        Controller.addCommandFromArgs("status",new statusCommand());
        Controller.addCommandFromArgs("branch",new branchCommand());
        Controller.addCommandFromArgs("checkout",new checkoutCommand());
        Controller.addCommandFromArgs("rm-branch",new rmbranchCommand());
        Controller.addCommandFromArgs("reset",new resetCommand());
        Controller.addCommandFromArgs("merge",new mergeCommand());



        // Repo at the current working dir!
        Repository Repo = new Repository();

        try {
            Controller.execute(Repo, args);
        } catch (IllegalStateException | IllegalArgumentException e) {
            //throw new IllegalArgumentException("please insert valid command.");
            System.out.println("Error : "+e.getMessage());
        }

    }
}
