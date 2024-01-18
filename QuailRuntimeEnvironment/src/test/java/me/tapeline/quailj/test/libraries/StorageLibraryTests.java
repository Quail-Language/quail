package me.tapeline.quailj.test.libraries;

import me.tapeline.quailj.launcher.QuailLauncher;
import me.tapeline.quailj.typing.classes.QObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StorageLibraryTests {

    @Test
    public void testJson() {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchAndHandleErrors(
                new String[] {"run", "testAssets/libraryTests/storage/json.q"});
        Assertions.assertEquals(
                "[[a, 1], [b, 2]]",
                returnValue.toString()
        );
    }

    @Test
    public void testYaml() {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchAndHandleErrors(
                new String[] {"run", "testAssets/libraryTests/storage/yaml.q"});
        Assertions.assertEquals(
                "[[a, 1], [b, 2]]",
                returnValue.toString()
        );
    }

}
