package runtime.execution;

import me.tapeline.quailj.launcher.LauncherException;
import me.tapeline.quailj.launcher.QuailLauncher;
import me.tapeline.quailj.lexing.LexerException;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.preprocessing.PreprocessorException;
import me.tapeline.quailj.typing.classes.QObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class RuntimeExecGeneratorsTests {

    @Test
    public void listForGenerator() throws LauncherException, PreprocessorException, ParserException, LexerException {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchWithAnonymousCode(
                "" +
                        "a = [i for i in 0:10]\n" +
                        "b = [i * 10 for i in 0:10 if i % 2 == 0]\n" +
                        "return [a, b]",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "[[0, 1, 2, 3, 4, 5, 6, 7, 8, 9], [0, 20, 40, 60, 80]]",
                returnValue.toString()
        );
    }

    @Test
    public void dictForGenerator() throws LauncherException, PreprocessorException, ParserException, LexerException {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchWithAnonymousCode(
                "" +
                        "a = {string(i) = i for i in 0:10}\n" +
                        "b = {string(i) = i * 10 for i in 0:10 if i % 2 == 0}\n" +
                        "return [a, b]",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "[{\"0\" = 0, \"1\" = 1, \"2\" = 2, \"3\" = 3, " +
                        "\"4\" = 4, \"5\" = 5, \"6\" = 6, \"7\" = 7, \"8\" = 8, \"9\" = 9}, " +
                        "{\"0\" = 0, \"2\" = 20, \"4\" = 40, \"6\" = 60, \"8\" = 80}]",
                returnValue.toString()
        );
    }

    @Test
    public void listThroughGenerator() throws LauncherException, PreprocessorException, ParserException, LexerException {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchWithAnonymousCode(
                "" +
                        "a = [i through 0:10 as i]\n" +
                        "b = [i * 10 through 0:10 as i if i % 2 == 0]\n" +
                        "return [a, b]",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "[[0, 1, 2, 3, 4, 5, 6, 7, 8, 9], [0, 20, 40, 60, 80]]",
                returnValue.toString()
        );
    }

    @Test
    public void dictThroughGenerator() throws LauncherException, PreprocessorException, ParserException, LexerException {
        QuailLauncher launcher = new QuailLauncher();
        QObject returnValue = launcher.launchWithAnonymousCode(
                "" +
                        "a = {string(i) = i through 0:10 as i}\n" +
                        "b = {string(i) = i * 10 through 0:10 as i if i % 2 == 0}\n" +
                        "return [a, b]",
                new File(""),
                new String[] {"run", "anonymous"}
        );
        Assertions.assertEquals(
                "[{\"0\" = 0, \"1\" = 1, \"2\" = 2, \"3\" = 3, " +
                        "\"4\" = 4, \"5\" = 5, \"6\" = 6, \"7\" = 7, \"8\" = 8, \"9\" = 9}, " +
                        "{\"0\" = 0, \"2\" = 20, \"4\" = 40, \"6\" = 60, \"8\" = 80}]",
                returnValue.toString()
        );
    }

}
