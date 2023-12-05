package me.tapeline.quailj.test.runtime.typing;

import me.tapeline.quailj.launcher.LauncherException;
import me.tapeline.quailj.launcher.QuailLauncher;
import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.preprocessing.PreprocessorException;
import me.tapeline.quailj.typing.classes.QObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class ModifierTests {

    @Test
    public void testOneSimpleCase() throws
            LauncherException, PreprocessorException, ParserException, LexerException {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchWithAnonymousCode(
                "" +
                        "try {\n" +
                        "  num a = 123\n" +
                        "  return a\n" +
                        "} catch as e {\n" +
                        "  return -1\n" +
                        "}",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "123",
                returnValue.toString()
        );
        QObject returnValue2 = launcher.launchWithAnonymousCode(
                "" +
                        "try {\n" +
                        "  num a = false\n" +
                        "  return a\n" +
                        "} catch as e {\n" +
                        "  return -1\n" +
                        "}",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "-1",
                returnValue2.toString()
        );
    }

    @Test
    public void testOneComplexCase() throws
            LauncherException, PreprocessorException, ParserException, LexerException {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchWithAnonymousCode(
                "" +
                        "a = 321\n" +
                        "try {\n" +
                        "  local num a = 123\n" +
                        "  return a\n" +
                        "} catch as e {\n" +
                        "  return -1\n" +
                        "}",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "123",
                returnValue.toString()
        );
        QObject returnValue2 = launcher.launchWithAnonymousCode(
                "" +
                        "a = 321\n" +
                        "try {\n" +
                        "  local num a = false\n" +
                        "  return a\n" +
                        "} catch as e {\n" +
                        "  return -1\n" +
                        "}",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "-1",
                returnValue2.toString()
        );
    }

    @Test
    public void testMultipleCases() throws
            LauncherException, PreprocessorException, ParserException, LexerException {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchWithAnonymousCode(
                "" +
                        "try {\n" +
                        "  num | string a = 123\n" +
                        "  a = \"b\"\n" +
                        "  return a\n" +
                        "} catch as e {\n" +
                        "  return -1\n" +
                        "}",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "b",
                returnValue.toString()
        );
        QObject returnValue2 = launcher.launchWithAnonymousCode(
                "" +
                        "try {\n" +
                        "  num | string a = 123\n" +
                        "  a = \"b\"\n" +
                        "  a = false\n" +
                        "  return a\n" +
                        "} catch as e {\n" +
                        "  return -1\n" +
                        "}",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "-1",
                returnValue2.toString()
        );
    }

    @Test
    public void testFinalized() throws
            LauncherException, PreprocessorException, ParserException, LexerException {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchWithAnonymousCode(
                "" +
                        "try {\n" +
                        "  final a = 123\n" +
                        "  return a\n" +
                        "} catch as e {\n" +
                        "  return -1\n" +
                        "}",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "123",
                returnValue.toString()
        );
        QObject returnValue2 = launcher.launchWithAnonymousCode(
                "" +
                        "try {\n" +
                        "  final a = 123\n" +
                        "  a = 321\n" +
                        "  return a\n" +
                        "} catch as e {\n" +
                        "  return -1\n" +
                        "}",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "-1",
                returnValue2.toString()
        );
    }

    @Test
    public void testPredeclaredFinalized() throws
            LauncherException, PreprocessorException, ParserException, LexerException {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchWithAnonymousCode(
                "" +
                        "final a\n" +
                        "try {\n" +
                        "  a = 123\n" +
                        "  return a\n" +
                        "} catch as e {\n" +
                        "  return -1\n" +
                        "}",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "123",
                returnValue.toString()
        );
        QObject returnValue2 = launcher.launchWithAnonymousCode(
                "" +
                        "final a\n" +
                        "try {\n" +
                        "  a = 123\n" +
                        "  a = 321\n" +
                        "  return a\n" +
                        "} catch as e {\n" +
                        "  return -1\n" +
                        "}",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "-1",
                returnValue2.toString()
        );
    }

}
