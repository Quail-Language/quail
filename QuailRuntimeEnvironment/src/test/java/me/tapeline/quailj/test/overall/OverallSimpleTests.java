package me.tapeline.quailj.test.overall;

import me.tapeline.quailj.launcher.QuailLauncher;
import me.tapeline.quailj.typing.classes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OverallSimpleTests {

    @Test
    public void test1() {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchAndHandleErrors(
                new String[] {"run", "testAssets/overall/simple/test1.q"});
        Assertions.assertEquals(
                "1234567891012345678910",
                returnValue.toString()
        );
    }

}
