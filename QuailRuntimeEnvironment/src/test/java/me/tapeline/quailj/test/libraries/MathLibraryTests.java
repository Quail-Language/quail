package me.tapeline.quailj.test.libraries;

import me.tapeline.quailj.launcher.QuailLauncher;
import me.tapeline.quailj.typing.classes.QObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MathLibraryTests {

    @Test
    public void test1() {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchAndHandleErrors(
                new String[] {"run", "testAssets/libraryTests/math/t1.q"});
        Assertions.assertEquals(
                "24",
                returnValue.toString()
        );
    }

}
