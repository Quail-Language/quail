package me.tapeline.quailj.test.libraries;

import me.tapeline.quailj.launcher.QuailLauncher;
import me.tapeline.quailj.typing.classes.QObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class JILibraryTests {

    @Test
    public void testObjectsFieldsMethods() {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchAndHandleErrors(
                new String[] {"run", "testAssets/libraryTests/ji/objects_fields_methods.q"});
        Assertions.assertEquals(
                "This is my stringThis is my string2",
                returnValue.toString()
        );
    }

    @Test
    public void testClassesAndConstructors() {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchAndHandleErrors(
                new String[] {"run", "testAssets/libraryTests/ji/classes_objects.q"});
        Assertions.assertEquals(
                "c3",
                returnValue.toString()
        );
    }

    @Test
    public void testNewClasses() {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchAndHandleErrors(
                new String[] {"run", "testAssets/libraryTests/ji/new_classes.q"});
        Assertions.assertEquals(
                "36",
                returnValue.toString()
        );
    }

}
