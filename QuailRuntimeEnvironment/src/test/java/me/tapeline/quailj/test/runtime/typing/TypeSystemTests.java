package me.tapeline.quailj.test.runtime.typing;

import me.tapeline.quailj.runtime.Memory;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.RuntimeStriker;
import me.tapeline.quailj.typing.classes.*;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.typing.utils.FuncArgument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TypeSystemTests {

    public final Memory memory = new Memory();
    public final Runtime runtime = new Runtime();

    @Test
    public void builtinTypesInitialization() {
        QBool bool = QObject.Val(true);
        QNumber num = QObject.Val(3.14);
        QString str = QObject.Val("hello");
        QNull nullValue = QObject.Val();
        Assertions.assertTrue(
                bool.getValue() && num.getValue() == 3.14 && str.getValue().equals("hello")
        );
    }

    class LetterClassConstructor extends QBuiltinFunc {
        public LetterClassConstructor() {
            super(
                    "_constructor",
                    Arrays.asList(
                            new FuncArgument("me", QObject.Val(), new int[0], 0),
                            new FuncArgument("type", QObject.Val(), new int[0], 0),
                            new FuncArgument("capital", QObject.Val(), new int[0], 0),
                            new FuncArgument("small", QObject.Val(), new int[0], 0)
                    ),
                    null,
                    memory,
                    false
            );
        }

        @Override
        public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList)
                throws RuntimeStriker {
            args.get("me").set(runtime, "type", args.get("type"));
            args.get("me").set(runtime, "capital", args.get("capital"));
            args.get("me").set(runtime, "small", args.get("small"));
            return args.get("me");
        }
    }

    @Test
    public void basicClassAndObjectConstruction() throws RuntimeStriker {
        QObject classObject = QObject.superObject.extendAs(runtime, "Letter");
        classObject.set(runtime, "type", QObject.Val());
        classObject.set(runtime, "capital", QObject.Val());
        classObject.set(runtime, "small", QObject.Val());
        classObject.set(runtime, "_constructor", new LetterClassConstructor());

        QObject object = classObject.derive(runtime);
        object.callFromThis(runtime, "_constructor", new ArrayList<>(Arrays.asList(
                QObject.Val("vowel"),
                QObject.Val("A"),
                QObject.Val("a")
        )), new HashMap<>());

        Assertions.assertTrue(
                object.get("type").strValue().equals("vowel") &&
                        object.get("capital").strValue().equals("A") &&
                        object.get("small").strValue().equals("a")
        );
        Assertions.assertTrue(object.instanceOf(classObject));
        Assertions.assertTrue(object.instanceOf(QObject.superObject));

        object.set(runtime, "a", QObject.Val(1));

        Assertions.assertTrue(classObject.get("a").isNull());
    }

    class VowelClassConstructor extends QBuiltinFunc {
        public VowelClassConstructor() {
            super(
                    "_constructor",
                    Arrays.asList(
                            new FuncArgument("me", QObject.Val(), new int[0], 0),
                            new FuncArgument("capital", QObject.Val(), new int[0], 0),
                            new FuncArgument("small", QObject.Val(), new int[0], 0)
                    ),
                    null,
                    memory,
                    false
            );
        }

        @Override
        public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList)
                throws RuntimeStriker {
            args.get("me").set(runtime, "type", QObject.Val("vowel"));
            args.get("me").set(runtime, "capital", args.get("capital"));
            args.get("me").set(runtime, "small", args.get("small"));
            return args.get("me");
        }
    }

    class ConsonantClassConstructor extends QBuiltinFunc {
        public ConsonantClassConstructor() {
            super(
                    "_constructor",
                    Arrays.asList(
                            new FuncArgument("me", QObject.Val(), new int[0], 0),
                            new FuncArgument("capital", QObject.Val(), new int[0], 0),
                            new FuncArgument("small", QObject.Val(), new int[0], 0)
                    ),
                    null,
                    memory,
                    false
            );
        }

        @Override
        public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList)
                throws RuntimeStriker {
            args.get("me").set(runtime, "type", QObject.Val("consonant"));
            args.get("me").set(runtime, "capital", args.get("capital"));
            args.get("me").set(runtime, "small", args.get("small"));
            return args.get("me");
        }
    }

    class AClassConstructor extends QBuiltinFunc {
        public AClassConstructor() {
            super(
                    "_constructor",
                    Collections.singletonList(
                            new FuncArgument("me", QObject.Val(), new int[0], 0)
                    ),
                    null,
                    memory,
                    false
            );
        }

        @Override
        public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList)
                throws RuntimeStriker {
            args.get("me").set(runtime, "type", QObject.Val("vowel"));
            args.get("me").set(runtime, "capital", QObject.Val("A"));
            args.get("me").set(runtime, "small", QObject.Val("a"));
            return args.get("me");
        }
    }

    @Test
    public void basicClassInheritance() throws RuntimeStriker {
        QObject letterClassObject = QObject.superObject.extendAs(runtime, "Letter");
        letterClassObject.set(runtime, "type", QObject.Val());
        letterClassObject.set(runtime, "capital", QObject.Val());
        letterClassObject.set(runtime, "small", QObject.Val());
        letterClassObject.set(runtime, "_constructor", new LetterClassConstructor());

        QObject vowelClassObject = letterClassObject.extendAs(runtime, "Vowel");
        vowelClassObject.set(runtime, "_constructor", new VowelClassConstructor());

        QObject consonantClassObject = letterClassObject.extendAs(runtime, "Consonant");
        consonantClassObject.set(runtime, "_constructor", new ConsonantClassConstructor());

        QObject aClassObject = vowelClassObject.extendAs(runtime, "LetterA");
        aClassObject.set(runtime, "_constructor", new AClassConstructor());

        QObject objectI = letterClassObject.derive(runtime);
        objectI.callFromThis(runtime, "_constructor", new ArrayList<>(Arrays.asList(
                QObject.Val("vowel"),
                QObject.Val("I"),
                QObject.Val("i")
        )), new HashMap<>());

        QObject vowelE = vowelClassObject.derive(runtime);
        vowelE.callFromThis(runtime, "_constructor", new ArrayList<>(Arrays.asList(
                QObject.Val("E"),
                QObject.Val("e")
        )), new HashMap<>());

        QObject consonantB = consonantClassObject.derive(runtime);
        consonantB.callFromThis(runtime, "_constructor", new ArrayList<>(Arrays.asList(
                QObject.Val("B"),
                QObject.Val("b")
        )), new HashMap<>());

        QObject a = aClassObject.derive(runtime);
        a.callFromThis(runtime, "_constructor", new ArrayList<>(), new HashMap<>());

        Assertions.assertTrue(
                objectI.get("type").strValue().equals("vowel") &&
                        objectI.get("capital").strValue().equals("I") &&
                        objectI.get("small").strValue().equals("i")
        );
        Assertions.assertTrue(
                vowelE.get("type").strValue().equals("vowel") &&
                        vowelE.get("capital").strValue().equals("E") &&
                        vowelE.get("small").strValue().equals("e")
        );
        Assertions.assertTrue(
                consonantB.get("type").strValue().equals("consonant") &&
                        consonantB.get("capital").strValue().equals("B") &&
                        consonantB.get("small").strValue().equals("b")
        );
        Assertions.assertTrue(
                a.get("type").strValue().equals("vowel") &&
                        a.get("capital").strValue().equals("A") &&
                        a.get("small").strValue().equals("a")
        );

        Assertions.assertTrue(objectI.instanceOf(letterClassObject));
        Assertions.assertTrue(objectI.instanceOf(QObject.superObject));

        Assertions.assertTrue(vowelE.instanceOf(letterClassObject));
        Assertions.assertTrue(vowelE.instanceOf(vowelClassObject));
        Assertions.assertFalse(vowelE.instanceOf(consonantClassObject));

        Assertions.assertTrue(consonantB.instanceOf(letterClassObject));
        Assertions.assertTrue(consonantB.instanceOf(consonantClassObject));
        Assertions.assertFalse(consonantB.instanceOf(vowelClassObject));

        Assertions.assertTrue(a.instanceOf(letterClassObject));
        Assertions.assertTrue(a.instanceOf(vowelClassObject));
        Assertions.assertTrue(a.instanceOf(aClassObject));
        Assertions.assertFalse(a.instanceOf(consonantClassObject));
    }

    class RealClassConstructor extends QBuiltinFunc {
        public RealClassConstructor() {
            super(
                    "_constructor",
                    Arrays.asList(
                            new FuncArgument("me", QObject.Val(), new int[0], 0),
                            new FuncArgument("n", QObject.Val(), new int[0], 0),
                            new FuncArgument("d", QObject.Val(), new int[0], 0)
                    ),
                    null,
                    memory,
                    false
            );
        }

        @Override
        public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList)
                throws RuntimeStriker {
            args.get("me").set(runtime, "numerator", args.get("n"));
            args.get("me").set(runtime, "denominator", args.get("d"));
            ((QNumber) args.get("me")).setValue(args.get("n").numValue() / args.get("d").numValue());
            return args.get("me");
        }
    }

    @Test
    public void inheritanceFromBuiltinClasses() throws RuntimeStriker {
        QObject realClassObject = QNumber.prototype.extendAs(runtime, "Real");
        realClassObject.set(runtime, "numerator", QObject.Val());
        realClassObject.set(runtime, "denominator", QObject.Val());
        realClassObject.set(runtime, "_constructor", new RealClassConstructor());

        QObject realObject = realClassObject.derive(runtime);
        realObject.callFromThis(runtime, "_constructor", new ArrayList<>(Arrays.asList(
                QObject.Val(3),
                QObject.Val(5)
        )), new HashMap<>());

        Assertions.assertInstanceOf(QNumber.class, realObject);
        Assertions.assertTrue(
                realObject.get("numerator").numValue() == 3 &&
                        realObject.get("denominator").numValue() == 5
        );
        Assertions.assertEquals(0.6, realObject.numValue());
        Assertions.assertEquals(1.6, realObject.sum(runtime, QObject.Val(1)).numValue());
    }

}
