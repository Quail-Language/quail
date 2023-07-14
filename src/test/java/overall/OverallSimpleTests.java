package overall;

import me.tapeline.quailj.launcher.QuailLauncher;
import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.*;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.utils.FuncArgument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
