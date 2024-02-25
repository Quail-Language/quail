package me.tapeline.quailj;

import me.tapeline.quailj.launcher.QuailLauncher;
import me.tapeline.quailj.typing.classes.QObject;

// TODO fix error display
// TODO fix in operator and add contains method to list
// TODO revise flag documentation
// TODO fix suppressing of uncaught exceptions in try-catch
// TODO add pi and e to math

public class Main {

    public static void main(String[] args) {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchAndHandleErrors(args);
        if (returnValue != null && GlobalFlags.displayReturnValue)
            System.out.println("Script finished with result " + returnValue);
        System.exit(0);
    }

}
