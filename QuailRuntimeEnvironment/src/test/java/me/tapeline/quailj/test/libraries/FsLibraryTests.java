package me.tapeline.quailj.test.libraries;

import me.tapeline.quailj.launcher.QuailLauncher;
import me.tapeline.quailj.typing.classes.QObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FsLibraryTests {

    @Test
    public void test1() {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchAndHandleErrors(
                new String[] {"run", "testAssets/libraryTests/fs/create_exist_delete.q"});
        Assertions.assertEquals(
                "111000",
                returnValue.toString()
        );
    }

    @Test
    public void test2() {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchAndHandleErrors(
                new String[] {"run", "testAssets/libraryTests/fs/queries.q"});
        Assertions.assertEquals(
                "[test]__test__UTF-81010dGVzdA==",
                returnValue.toString()
        );
    }

}
