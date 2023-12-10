package me.tapeline.quailj;

import me.tapeline.quailj.launcher.QuailLauncher;
import me.tapeline.quailj.typing.classes.QObject;

// TODO debugger
// TODO fix error display
// TODO create classes for all exceptions

public class Main {

    public static void main(String[] args) {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchAndHandleErrors(args);
        if (returnValue != null)
            System.out.println("Script finished with result " + returnValue);
    }

}
