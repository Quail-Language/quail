package me.tapeline.quailj.runtime;

import me.tapeline.quailj.debugging.DebugServer;
import me.tapeline.quailj.io.DefaultIO;
import me.tapeline.quailj.io.IO;
import me.tapeline.quailj.lexing.*;
import me.tapeline.quailj.parsing.Parser;
import me.tapeline.quailj.parsing.ParserException;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.effects.*;
import me.tapeline.quailj.parsing.nodes.expression.*;
import me.tapeline.quailj.parsing.nodes.generators.*;
import me.tapeline.quailj.parsing.nodes.literals.*;
import me.tapeline.quailj.parsing.nodes.sections.*;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.preprocessing.Preprocessor;
import me.tapeline.quailj.preprocessing.PreprocessorException;
import me.tapeline.quailj.runtime.librarymanagement.LibraryCache;
import me.tapeline.quailj.runtime.librarymanagement.LibraryLoader;
import me.tapeline.quailj.runtime.std.basic.classes.dict.*;
import me.tapeline.quailj.runtime.std.basic.classes.list.*;
import me.tapeline.quailj.runtime.std.basic.classes.string.*;
import me.tapeline.quailj.runtime.std.basic.classes.object.ObjectFuncTable;
import me.tapeline.quailj.runtime.std.basic.io.*;
import me.tapeline.quailj.runtime.std.basic.math.*;
import me.tapeline.quailj.runtime.std.basic.common.*;
import me.tapeline.quailj.runtime.std.basic.numbers.*;
import me.tapeline.quailj.runtime.std.event.EventLibrary;
import me.tapeline.quailj.runtime.std.fs.FSLibrary;
import me.tapeline.quailj.runtime.std.ji.JILibrary;
import me.tapeline.quailj.runtime.std.qml.QMLLibrary;
import me.tapeline.quailj.typing.classes.*;
import me.tapeline.quailj.typing.classes.errors.*;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.AlternativeCall;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.utils.IntFlags;
import me.tapeline.quailj.utils.TextUtils;
import org.jetbrains.annotations.Nullable;

import static me.tapeline.quailj.typing.classes.QObject.Val;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Runtime {

    private final File scriptHome;
    private final File scriptFile;
    private final Node root;
    private final boolean doProfile;
    private final boolean doDebug;
    private final String code;
    private final IO io;
    private Node current = new Node(Token.UNDEFINED) {
        @Override
        public String stringRepr() { return null; }
    };
    private final Memory memory;
    private final List<AsyncRuntimeWorker> asyncRuntimeWorkers = new ArrayList<>();
    private final LibraryCache libraryCache;
    private final LibraryLoader libraryLoader;
    private final Set<String> librariesRoots;
    public int nextLineToStop = -1;

    public Runtime() {
        this(null, "", new File(""),
                new File(""), new DefaultIO(), false, false);
    }

    public Runtime(Node root, String code, File scriptFile, File scriptHome,
                   IO io, boolean doProfile, boolean doDebug) {
        this.scriptHome = scriptHome;
        this.scriptFile = scriptFile;
        this.root = root;
        this.doProfile = doProfile;
        this.io = io;
        this.memory = new Memory();
        this.code = code;
        this.doDebug = doDebug;
        librariesRoots = new HashSet<>();
        librariesRoots.add("$cwd$/?");
        librariesRoots.add("$script$/?");
        libraryCache = new LibraryCache();
        libraryLoader = new LibraryLoader(librariesRoots);
        registerDefaults();
        if (io instanceof DefaultIO)
            ((DefaultIO) io).setDefaultCwd(scriptHome.getAbsolutePath());
        io.resetCwd();
    }

    public File getScriptFile() {
        return scriptFile;
    }

    public File getScriptHome() {
        return scriptHome;
    }

    public Node getRoot() {
        return root;
    }

    public boolean isDoProfile() {
        return doProfile;
    }

    public IO getIo() {
        return io;
    }

    public Node getCurrent() {
        return current;
    }

    public Memory getMemory() {
        return memory;
    }

    public String getCode() {
        return code;
    }

    private void registerDefaults() {
        memory.set("Object", QObject.superObject);
        QObject.superObject.set("table", new ObjectFuncTable(this));

        memory.set("List", QList.prototype);
        QList.prototype.set("add", new ListFuncAdd(this));
        QList.prototype.set("addAll", new ListFuncAddAll(this));
        QList.prototype.set("clear", new ListFuncClear(this));
        QList.prototype.set("count", new ListFuncCount(this));
        QList.prototype.set("find", new ListFuncFind(this));
        QList.prototype.set("findFromRight", new ListFuncFindFromRight(this));
        QList.prototype.set("get", new ListFuncGet(this));
        QList.prototype.set("insert", new ListFuncInsert(this));
        QList.prototype.set("joined", new ListFuncJoined(this));
        QList.prototype.set("pop", new ListFuncPop(this));
        QList.prototype.set("remove", new ListFuncRemove(this));
        QList.prototype.set("removeElementAt", new ListFuncRemoveElementAt(this));
        QList.prototype.set("reversed", new ListFuncReversed(this));
        QList.prototype.set("set", new ListFuncSet(this));
        QList.prototype.set("size", new ListFuncSize(this));
        QList.prototype.set("sort", new ListFuncSort(this));
        QList.prototype.set("sorted", new ListFuncSorted(this));
                
        memory.set("String", QString.prototype);
        QString.prototype.set("capitalized", new StringFuncCapitalized(this));
        QString.prototype.set("centered", new StringFuncCentered(this));
        QString.prototype.set("chars", new StringFuncChars(this));
        QString.prototype.set("count", new StringFuncCount(this));
        QString.prototype.set("decoded64", new StringFuncDecoded64(this));
        QString.prototype.set("encoded64", new StringFuncEncoded64(this));
        QString.prototype.set("endsWith", new StringFuncEndsWith(this));
        QString.prototype.set("find", new StringFuncFind(this));
        QString.prototype.set("findFromRight", new StringFuncFindFromRight(this));
        QString.prototype.set("format", new StringFuncFormat(this));
        QString.prototype.set("inBetweenOf", new StringFuncInBetweenOf(this));
        QString.prototype.set("isAlphabetical", new StringFuncIsAlphabetical(this));
        QString.prototype.set("isAlphaNumeric", new StringFuncIsAlphaNumeric(this));
        QString.prototype.set("isLower", new StringFuncIsLower(this));
        QString.prototype.set("isNumeric", new StringFuncIsNumeric(this));
        QString.prototype.set("isUpper", new StringFuncIsUpper(this));
        QString.prototype.set("lowerCased", new StringFuncLowerCased(this));
        QString.prototype.set("replaceAll", new StringFuncReplaceAll(this));
        QString.prototype.set("replaceFirst", new StringFuncReplaceFirst(this));
        QString.prototype.set("reversed", new StringFuncReversed(this));
        QString.prototype.set("size", new StringFuncSize(this));
        QString.prototype.set("split", new StringFuncSplit(this));
        QString.prototype.set("startsWith", new StringFuncStartsWith(this));
        QString.prototype.set("swappedCase", new StringFuncSwappedCase(this));
        QString.prototype.set("upperCased", new StringFuncUpperCased(this));

        memory.set("Dict", QDict.prototype);
        QDict.prototype.set("assemblePairs", new DictFuncAssemblePairs(this));
        QDict.prototype.set("clear", new DictFuncClear(this));
        QDict.prototype.set("containsKey", new DictFuncContainsKey(this));
        QDict.prototype.set("get", new DictFuncGet(this));
        QDict.prototype.set("keys", new DictFuncKeys(this));
        QDict.prototype.set("pairs", new DictFuncPairs(this));
        QDict.prototype.set("set", new DictFuncSet(this));
        QDict.prototype.set("size", new DictFuncSize(this));
        QDict.prototype.set("values", new DictFuncValues(this));

        memory.set("Bool", QBool.prototype);
        memory.set("Func", QFunc.prototype);
        memory.set("Number", QNumber.prototype);
        memory.set("Null", QNull.prototype);

        memory.set("Exception", QException.prototype);
        memory.set("AssertionException", QAssertionException.prototype);
        memory.set("CircularDependencyException", QCircularDependencyException.prototype);
        memory.set("DerivationException", QDerivationException.prototype);
        memory.set("IOException", QIOException.prototype);
        memory.set("IterationNotStartedException", QIterationNotStartedException.prototype);
        memory.set("IterationStopException", QIterationStopException.prototype);
        memory.set("UnsuitableTypeException", QUnsuitableTypeException.prototype);
        memory.set("UnsuitableValueException", QUnsuitableValueException.prototype);
        memory.set("UnsupportedConversionException", QUnsupportedConversionException.prototype);
        memory.set("UnsupportedIterationException", QUnsupportedIterationException.prototype);
        memory.set("UnsupportedOperationException", QUnsupportedOperationException.prototype);
        memory.set("UnsupportedStepSubscriptException", QUnsupportedStepSubscriptException.prototype);
        memory.set("UnsupportedSubscriptException", QUnsupportedSubscriptException.prototype);
        memory.set("UnsupportedUnaryOperationException", QUnsupportedUnaryOperationException.prototype);
        memory.set("IndexOutOfBoundsException", QIndexOutOfBoundsException.prototype);

        memory.set("all", new FuncAll(this));
        memory.set("any", new FuncAny(this));
        memory.set("clone", new FuncClone(this));
        memory.set("copy", new FuncCopy(this));
        memory.set("enumerate", new FuncEnumerate(this));
        memory.set("map", new FuncMap(this));
        memory.set("hash", new FuncHash(this));
        memory.set("millis", new FuncMillis(this));
        memory.set("zip", new FuncZip(this));
        memory.set("input", new FuncInput(this));
        memory.set("print", new FuncPrint(this));
        memory.set("put", new FuncPut(this));
        memory.set("abs", new FuncAbs(this));
        memory.set("acos", new FuncAcos(this));
        memory.set("asin", new FuncAsin(this));
        memory.set("atan", new FuncAtan(this));
        memory.set("atan2", new FuncAtan2(this));
        memory.set("cos", new FuncCos(this));
        memory.set("cosh", new FuncCosh(this));
        memory.set("sum", new FuncSum(this));
        memory.set("max", new FuncMax(this));
        memory.set("min", new FuncMin(this));
        memory.set("sin", new FuncSin(this));
        memory.set("sinh", new FuncSinh(this));
        memory.set("tan", new FuncTan(this));
        memory.set("tanh", new FuncTanh(this));
        memory.set("bin", new FuncBin(this));
        memory.set("dec", new FuncDec(this));
        memory.set("hex", new FuncHex(this));
        memory.set("oct", new FuncOct(this));
        memory.set("readFile", new FuncReadFile(this));
        memory.set("writeFile", new FuncWriteFile(this));

        libraryLoader.addBuiltinLibrary(new QMLLibrary());
        libraryLoader.addBuiltinLibrary(new EventLibrary());
        libraryLoader.addBuiltinLibrary(new JILibrary());
        libraryLoader.addBuiltinLibrary(new FSLibrary());
    }

    public void error(String message) throws RuntimeStriker {
        Token token = current.getToken();
        throw new RuntimeStriker(
                message, token.getCharacter(), token.getLine(), token.getLength()
        );
    }

    public void error(QException error) throws RuntimeStriker {
        Token token = current.getToken();
        throw new RuntimeStriker(
                RuntimeStriker.Type.EXCEPTION,
                error,
                token.getCharacter(), token.getLine(), token.getLength()
        );
    }

    private void begin(Node node) {
        node.executionStart = System.currentTimeMillis();
    }

    private void end(Node node) {
        node.executionTime = System.currentTimeMillis() - node.executionStart;
    }

    public QObject runString(String code, Memory scope)
            throws PreprocessorException, LexerException, ParserException {
        Preprocessor preprocessor = new Preprocessor(code, scriptHome);
        String preprocessedCode = preprocessor.preprocess();

        Lexer lexer = new Lexer(preprocessedCode);
        List<Token> tokens = lexer.scan();

        Parser parser = new Parser(preprocessedCode, tokens);
        BlockNode parsedCode = parser.parse();

        Runtime runtime = new Runtime(parsedCode, preprocessedCode, scriptFile, scriptHome,
                io, doProfile, false);
        QObject returnValue = QObject.Val(0);
        try {
            runtime.run(parsedCode, scope);
        } catch (RuntimeStriker striker) {
            if (striker.getType() == RuntimeStriker.Type.RETURN)
                returnValue = striker.getCarryingReturnValue();
            else if (striker.getType() == RuntimeStriker.Type.EXCEPTION) {
                System.err.println("Runtime error:");
                System.err.println(striker.formatError(preprocessedCode));
                return striker.getCarryingError();
            } else if (striker.getType() == RuntimeStriker.Type.EXIT)
                returnValue = QObject.Val(striker.getExitCode());
        }
        return returnValue;
    }

    private void loopFor(QObject iterable, List<String> iterators, JavaAction action, Memory enclosing)
            throws RuntimeStriker {
        int iteratorSize = iterators.size();
        int remainingStrikePower = 0;
        iterable.iterateStart(this);
        while (true) {
            try {
                QObject next = iterable.iterateNext(this);
                if (iteratorSize == 1)
                    enclosing.table.put(this, iterators.get(0), next);
                else if (next.isList()) {
                    List<QObject> list = next.listValue();
                    if (list.size() != iteratorSize)
                        error("Unpacking failed. List size = " + list.size() + "; Iterators = " + iteratorSize);
                    for (int i = 0; i < iteratorSize; i++)
                        enclosing.table.put(this, iterators.get(i), list.get(i));
                } else if (iteratorSize == 2) {
                    String keyVar = iterators.get(0);
                    String valVar = iterators.get(1);
                    next.getTable().forEach((key, value) -> {
                        enclosing.table.put(keyVar, Val(key));
                        enclosing.table.put(valVar, value);
                    });
                } else error("Iterator unpacking error. Unknown error");
                action.action(this, enclosing);
            } catch (RuntimeStriker striker) {
                if (striker.getType() == RuntimeStriker.Type.BREAK) {
                    break;
                } else if (striker.getType() == RuntimeStriker.Type.CONTINUE)
                    continue;
                else if (striker.getType() == RuntimeStriker.Type.EXCEPTION &&
                        striker.getCarryingError().instanceOf(QIterationStopException.prototype))
                    break;
                else throw striker;
            }
        }
    }

    public void loopThrough(QObject startObject, QObject endObject,
                            @Nullable QObject stepObject,
                            boolean isIncluding, String iterator,
                            JavaAction action, Memory enclosing) throws RuntimeStriker {
        if (!startObject.isNum())
            error(new QUnsuitableTypeException("Number start", startObject));
        if (!endObject.isNum())
            error(new QUnsuitableTypeException("Number end", endObject));
        if (stepObject != null && !stepObject.isNum())
            error(new QUnsuitableTypeException("Number step", stepObject));
        double start = startObject.numValue();
        double end = endObject.numValue();
        double step;
        if (stepObject != null)
            step = stepObject.numValue();
        else
            step = start <= end ? 1 : -1;
        boolean isIncreasing = start <= end;
        double numIterator = start;
        enclosing.set(this, iterator, Val(numIterator));
        while (true) {
            try {
                if (isIncluding && (
                        (isIncreasing && numIterator > end)
                                ||
                                (!isIncreasing && numIterator < end)
                ) || !isIncluding && (
                        (isIncreasing && numIterator >= end)
                                ||
                                (!isIncreasing && numIterator <= end)
                )) break;
                action.action(this, enclosing);
                numIterator += step;
                enclosing.set(this, iterator, Val(numIterator));
            } catch (RuntimeStriker striker) {
                if (striker.getType() == RuntimeStriker.Type.BREAK) {
                    break;
                } else if (striker.getType() == RuntimeStriker.Type.CONTINUE)
                    continue;
                else throw striker;
            }
        }
    }

    private QObject performBinaryOperation(QObject operandA, TokenType op, QObject operandB)
            throws RuntimeStriker {
        switch (op) {
            case PLUS: return operandA.sum(this, operandB);
            case MINUS: return operandA.subtract(this, operandB);
            case MULTIPLY: return operandA.multiply(this, operandB);
            case DIVIDE: return operandA.divide(this, operandB);
            case INTDIV: return operandA.intDivide(this, operandB);
            case MODULO: return operandA.modulo(this, operandB);
            case POWER: return operandA.power(this, operandB);
            case SHIFT_LEFT: return operandA.shiftLeft(this, operandB);
            case SHIFT_RIGHT: return operandA.shiftRight(this, operandB);
            case AND: return operandA.and(this, operandB);
            case OR: return operandA.or(this, operandB);
            case GREATER: return operandA.greater(this, operandB);
            case GREATER_EQUAL: return operandA.greaterEqual(this, operandB);
            case LESS: return operandA.less(this, operandB);
            case LESS_EQUAL: return operandA.lessEqual(this, operandB);
            case EQUALS: return operandA.equalsObject(this, operandB);
            case NOT_EQUALS: return operandA.notEqualsObject(this, operandB);
            case INSTANCEOF: return Val(operandA.instanceOf(operandB));
        }
        return null;
    }

    private QObject performUnaryOperation(TokenType op, QObject operandA)
            throws RuntimeStriker {
        switch (op) {
            case NOT: return operandA.not(this);
            case MINUS: return operandA.negate(this);
        }
        return null;
    }

    public QObject run(Node node, Memory scope) throws RuntimeStriker {
        if (doDebug) {
            if (DebugServer.breakpoints.containsKey(scriptFile) &&
                DebugServer.breakpoints.get(scriptFile).contains(node.getToken().getLine()) ||
                node.getToken().getLine() == this.nextLineToStop) {
                try {
                    DebugServer.enterBreakpoint(this, scope, node.getToken().getLine());
                    DebugServer.breakpoints.get(scriptFile).remove(node.getToken().getLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (doProfile) begin(node);
        current = node;
        Token currentToken = current.getToken();
        if (node instanceof AsyncNode) {
            AsyncRuntimeWorker worker = new AsyncRuntimeWorker(((AsyncNode) node).expression, this, scope);
            asyncRuntimeWorkers.add(worker);
            worker.start();
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof EffectNode) {
            EffectNode thisNode = (EffectNode) node;
            switch (thisNode.getToken().getType()) {
                case EFFECT_ASSERT:
                    if (run(thisNode.value, scope).isFalse())
                        error(new QAssertionException("Assertion failed. Expected true, but got false"));
                    break;
                case EFFECT_THROW:
                    throw new RuntimeStriker(
                            RuntimeStriker.Type.EXCEPTION,
                            run(thisNode.value, scope),
                            currentToken.getCharacter(), currentToken.getLine(), currentToken.getLength()
                    );
                case EFFECT_STRIKE:
                    QObject strikeAmount = run(thisNode.value, memory);
                    if (!strikeAmount.isNum())
                        error(new QUnsuitableTypeException("Number", strikeAmount));
                    if (strikeAmount.numValue() < 1)
                        error(new QUnsuitableTypeException("Expected n > 1, but got " + strikeAmount.numValue(),
                                strikeAmount));
                    throw new RuntimeStriker(
                            RuntimeStriker.Type.BREAK,
                            ((int) strikeAmount.numValue()),
                            currentToken.getCharacter(), currentToken.getLine(), currentToken.getLength()
                    );
                case EFFECT_IMPORT:
                    String target = run(thisNode.value, scope).strValue();
                    String targetContents;
                    try {
                        targetContents = io.readFile(scriptHome.getAbsolutePath() + "/" + target);
                    } catch (IOException e) {
                        error(new QIOException(e.toString()));
                        return Val();
                    }
                    try {
                        return runString(targetContents, scope);
                    } catch (PreprocessorException e) {
                        error("Preprocessor error:\n" + new QException(e.getMessage()));
                    } catch (ParserException e) {
                        error("Parser error:\n" + new QException(e.formatError()));
                    } catch (LexerException e) {
                        error("Lexer error:\n" + new QException(e.formatError()));
                    }
                    break;
            }
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof InstructionNode) {
            InstructionNode thisNode = (InstructionNode) node;
            switch (thisNode.getToken().getType()) {
                case INSTRUCTION_BREAK:
                    throw new RuntimeStriker(
                            RuntimeStriker.Type.BREAK, 1,
                            currentToken.getCharacter(), currentToken.getLine(), currentToken.getLength()
                    );
                case INSTRUCTION_CONTINUE:
                    throw new RuntimeStriker(
                            RuntimeStriker.Type.CONTINUE,
                            currentToken.getCharacter(), currentToken.getLine(), currentToken.getLength()
                    );
            }
        } else if (node instanceof ReturnNode) {
            ReturnNode thisNode = (ReturnNode) node;
            throw new RuntimeStriker(
                    RuntimeStriker.Type.RETURN,
                    thisNode.value == null? Val() : run(((ReturnNode) node).value, scope),
                    currentToken.getCharacter(), currentToken.getLine(), currentToken.getLength()
            );
        } else if (node instanceof UseNode) {
            UseNode thisNode = (UseNode) node;
            QObject library = libraryLoader.loadLibrary(this, libraryCache, thisNode.path);
            scope.set(this, thisNode.var, library);
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof AssignNode) {
            AssignNode thisNode = (AssignNode) node;
            QObject value = run(thisNode.value, scope);
            VariableNode variable = (VariableNode) thisNode.variable;
            if (scope.contains(variable.name) && !ModifierConstants.isLocal(variable.accessModifiers))
                scope.set(this, variable.name, value);
            else {
                if (!ModifierConstants.matchesOnAssign(variable.modifiers, value))
                    error("Attempt to assign wrong data type to clarified variable");
                int[] modifiers = variable.modifiers.clone();
                if (modifiers.length > 0 && ModifierConstants.isFinal(variable.accessModifiers))
                    modifiers[0] |= ModifierConstants.FINAL_ASSIGNED;
                scope.set(variable.name, value, modifiers);
            }
            if (doProfile) end(node);
            return value;
        } else if (node instanceof BinaryOperatorNode) {
            BinaryOperatorNode thisNode = (BinaryOperatorNode) node;
            QObject operandA = run(thisNode.left, scope);
            QObject operandB = run(thisNode.right, scope);
            List<QObject> listA = null;
            List<QObject> listB = null;
            List<QObject> result = null;
            if (thisNode.getToken().getMod() != TokenModifier.SINGULAR_MOD) {
                listA = operandA.listValue();
                listB = operandB.listValue();
                result = new ArrayList<>();
                if (listA == null) {
                    error(new QUnsuitableTypeException(operandA,
                            "Cannot perform unwrapping array operation on non-list"));
                    return Val();
                }
                if (listB == null) {
                    error(new QUnsuitableTypeException(operandB,
                            "Cannot perform unwrapping array operation on non-list"));
                    return Val();
                }
                if (listA.size() != listB.size())
                    error(new QUnsuitableValueException(
                            "Cannot perform unwrapping array operation on lists with different sizes",
                            Val(new ArrayList<>(Arrays.asList(operandA, operandB)))
                    ));
            }
            if (thisNode.getToken().getMod() == TokenModifier.ARRAY_MOD && listA != null) {
                int count = listA.size();
                for (int i = 0; i < count; i++) {
                    QObject value = performBinaryOperation(
                            listA.get(i),
                            thisNode.op,
                            listB.get(i)
                    );
                    if (value == null)
                        error("Unknown binary operation");
                    result.add(value);
                }
                return QObject.Val(result);
            } else if (thisNode.getToken().getMod() == TokenModifier.MATRIX_MOD && listA != null) {
                int countX = listA.size();
                for (int x = 0; x < countX; x++) {
                    QObject subA = listA.get(x);
                    QObject subB = listB.get(x);
                    if (!subA.isList() || !subB.isList() ||
                            ((QList) subA).getValues().size() != ((QList) subB).getValues().size())
                        error(new QUnsuitableValueException(
                                "Sublists do not match requirements (not list // not same size)",
                                Val(new ArrayList<>(Arrays.asList(subA, subB))))
                        );
                    List<QObject> sublistA = subA.listValue();
                    List<QObject> sublistB = subB.listValue();
                    List<QObject> subResult = new ArrayList<>();
                    int countY = sublistA.size();
                    for (int y = 0; y < countY; y++) {
                        QObject value = performBinaryOperation(
                                sublistA.get(y),
                                thisNode.op,
                                sublistB.get(y)
                        );
                        if (value == null)
                            error("Unknown binary operation");
                        subResult.add(value);
                    }
                    result.add(QObject.Val(subResult));
                }
                return QObject.Val(result);
            }
            QObject value = performBinaryOperation(operandA, thisNode.op, operandB);
            if (value == null)
                error("Unknown binary operation");
            if (doProfile) end(node);
            return value;
        } else if (node instanceof CallNode) {
            CallNode thisNode = (CallNode) node;
            QObject callee = null, parent = null;
            if (thisNode.isFieldCall) {
                parent = run(thisNode.parentField, scope);
                callee = parent.get(thisNode.field);
            } else
                callee = run(thisNode.callee, scope);
            List<QObject> args = new ArrayList<>();
            int argsCount = thisNode.args.size();
            for (int i = 0; i < argsCount; i++)
                args.add(run(thisNode.args.get(i), scope));
            HashMap<String, QObject> kwargs = new HashMap<>();
            for (Map.Entry<String, Node> entry : thisNode.kwargs.entrySet())
                kwargs.put(entry.getKey(), run(entry.getValue(), scope));
            if (callee instanceof QFunc && parent != null && thisNode.isFieldCall) {
                if (parent.isDict() && parent.dictValue().containsKey(thisNode.field))
                    return callee.call(this, args, kwargs);
                if (!parent.isPrototype())
                    return parent.callFromThis(this, callee, args, kwargs);
            }
            if (doProfile) end(node);
            return callee.call(this, args, kwargs);
        } else if (node instanceof FieldReferenceNode) {
            FieldReferenceNode thisNode = (FieldReferenceNode) node;
            QObject value = run(thisNode.parent, scope).getOverridable(this, thisNode.value);
            if (doProfile) end(node);
            return value;
        } else if (node instanceof FieldSetNode) {
            FieldSetNode thisNode = ((FieldSetNode) node);
            QObject object = run(thisNode.parent, scope);
            QObject value = run(thisNode.value, scope);
            object.setOverridable(this, thisNode.field, value);
            if (doProfile) end(node);
            return value;
        } else if (node instanceof IndexingNode) {
            IndexingNode thisNode = ((IndexingNode) node);
            QObject value = run(thisNode.collection, scope).index(this, run(thisNode.index, scope));
            if (doProfile) end(node);
            return value;
        } else if (node instanceof IndexSetNode) {
            IndexSetNode thisNode = ((IndexSetNode) node);
            QObject object = run(thisNode.collection, scope);
            QObject value = run(thisNode.value, scope);
            object.indexSet(this, run(thisNode.index, scope), value);
            if (doProfile) end(node);
            return value;
        } else if (node instanceof SubscriptNode) {
            SubscriptNode thisNode = ((SubscriptNode) node);
            QObject object = run(thisNode.collection, scope);
            QObject start = QObject.Val(), end = QObject.Val(), step = QObject.Val();
            if (thisNode.start != null)
                start = run(thisNode.start, scope);
            if (thisNode.end != null)
                end = run(thisNode.end, scope);
            if (thisNode.step != null)
                step = run(thisNode.step, scope);
            QObject value;
            if (step.isNull())
                value = object.subscriptStartEnd(this, start, end);
            else
                value = object.subscriptStartEndStep(this, start, end, step);
            if (doProfile) end(node);
            return value;
        } else if (node instanceof TypecastNode) {
            TypecastNode thisNode = ((TypecastNode) node);
            QObject subject = run(thisNode.expr, scope);
            QObject value = Val();
            if (IntFlags.check(thisNode.cast, ModifierConstants.NUM))
                value = subject.convertToNumber(this);
            else if (IntFlags.check(thisNode.cast, ModifierConstants.BOOL))
                value = subject.convertToBool(this);
            else if (IntFlags.check(thisNode.cast, ModifierConstants.STR))
                value = subject.convertToString(this);
            else error(new QUnsupportedConversionException(subject, TextUtils.modifierToStringRepr(thisNode.cast)));
            if (doProfile) end(node);
            return value;
        } else if (node instanceof UnaryOperatorNode) {
            UnaryOperatorNode thisNode = ((UnaryOperatorNode) node);
            QObject operand = run(thisNode.value, scope);
            List<QObject> list = null;
            List<QObject> result = null;
            if (thisNode.getToken().getMod() != TokenModifier.SINGULAR_MOD) {
                list = operand.listValue();
                result = new ArrayList<>();
                if (list == null) {
                    error(new QUnsuitableTypeException(operand,
                            "Cannot perform unwrapping array operation on non-list"));
                    return Val();
                }
            }
            if (thisNode.getToken().getMod() == TokenModifier.ARRAY_MOD && list != null) {
                int count = list.size();
                for (QObject qObject : list) {
                    QObject value = performUnaryOperation(
                            thisNode.op,
                            qObject
                    );
                    if (value == null)
                        error("Unknown unary operation");
                    result.add(value);
                }
                return QObject.Val(result);
            } else if (thisNode.getToken().getMod() == TokenModifier.MATRIX_MOD && list != null) {
                int countX = list.size();
                for (QObject sub : list) {
                    if (!sub.isList())
                        error(new QUnsuitableTypeException("List", sub));
                    List<QObject> sublist = sub.listValue();
                    List<QObject> subResult = new ArrayList<>();
                    int countY = sublist.size();
                    for (QObject qObject : sublist) {
                        QObject value = performUnaryOperation(
                                thisNode.op,
                                qObject
                        );
                        if (value == null)
                            error("Unknown unary operation");
                        subResult.add(value);
                    }
                    result.add(QObject.Val(subResult));
                }
                return QObject.Val(result);
            }
            QObject value = performUnaryOperation(thisNode.op, operand);
            if (value == null)
                error("Unknown unary operation");
            if (doProfile) end(node);
            return value;
        } else if (node instanceof DictForGeneratorNode) {
            DictForGeneratorNode thisNode = ((DictForGeneratorNode) node);
            Memory enclosing = new Memory(scope);
            QDict generated = Val(new HashMap<>());
            enclosing.table.put(this, "_this", generated);
            QObject iterable = run(thisNode.iterable, scope);
            loopFor(iterable, thisNode.iterators, (runtime, memory) -> {
                if (thisNode.condition != null) {
                    QObject condition = run(thisNode.condition, memory);
                    if (condition.isTrue())
                        generated.getValues().put(
                                run(thisNode.key, memory).toString(),
                                run(thisNode.value, memory)
                        );
                } else generated.getValues().put(
                        run(thisNode.key, memory).toString(),
                        run(thisNode.value, memory)
                );
                return Val();
            }, enclosing);
            if (doProfile) end(node);
            return generated;
        } else if (node instanceof DictThroughGeneratorNode) {
            DictThroughGeneratorNode thisNode = ((DictThroughGeneratorNode) node);
            Memory enclosing = new Memory(scope);
            QDict generated = Val(new HashMap<>());
            enclosing.table.put(this, "_this", generated);
            QObject start = run(thisNode.range.rangeStart, scope);
            QObject end = run(thisNode.range.rangeEnd, scope);
            QObject step = thisNode.range.rangeStep != null? run(thisNode.range.rangeStep, scope) : null;
            loopThrough(start, end, step, thisNode.range.isIncluding, thisNode.iterator.name, (runtime, memory) -> {
                if (thisNode.condition != null) {
                    QObject condition = run(thisNode.condition, memory);
                    if (condition.isTrue())
                        generated.getValues().put(
                                run(thisNode.key, memory).toString(),
                                run(thisNode.value, memory)
                        );
                } else generated.getValues().put(
                        run(thisNode.key, memory).toString(),
                        run(thisNode.value, memory)
                );
                return Val();
            }, enclosing);
            if (doProfile) end(node);
            return generated;
        } else if (node instanceof ListForGeneratorNode) {
            ListForGeneratorNode thisNode = ((ListForGeneratorNode) node);
            Memory enclosing = new Memory(scope);
            QList generated = Val(new ArrayList<>());
            enclosing.table.put(this, "_this", generated);
            QObject iterable = run(thisNode.iterable, scope);
            loopFor(iterable, thisNode.iterators, (runtime, memory) -> {
                if (thisNode.condition != null) {
                    QObject condition = run(thisNode.condition, memory);
                    if (condition.isTrue())
                        generated.getValues().add(run(thisNode.value, memory));
                } else
                    generated.getValues().add(run(thisNode.value, memory));
                return Val();
            }, enclosing);
            if (doProfile) end(node);
            return generated;
        } else if (node instanceof ListThroughGeneratorNode) {
            ListThroughGeneratorNode thisNode = ((ListThroughGeneratorNode) node);
            Memory enclosing = new Memory(scope);
            QList generated = Val(new ArrayList<>());
            enclosing.table.put(this, "_this", generated);
            QObject start = run(thisNode.range.rangeStart, scope);
            QObject end = run(thisNode.range.rangeEnd, scope);
            QObject step = thisNode.range.rangeStep != null? run(thisNode.range.rangeStep, scope) : null;
            loopThrough(start, end, step, thisNode.range.isIncluding, thisNode.iterator.name, (runtime, memory) -> {
                if (thisNode.condition != null) {
                    QObject condition = run(thisNode.condition, memory);
                    if (condition.isTrue())
                        generated.getValues().add(run(thisNode.value, memory));
                } else
                    generated.getValues().add(run(thisNode.value, memory));
                return Val();
            }, enclosing);
            if (doProfile) end(node);
            return generated;
        } else if (node instanceof RangeNode) {
            RangeNode thisNode = ((RangeNode) node);
            List<QObject> generated = new ArrayList<>();
            if (thisNode.rangeStart == null || thisNode.rangeEnd == null)
                error("Attempt to make indefinite loop. Start or end of range is null");
            QObject startObject = run(thisNode.rangeStart, scope);
            QObject endObject = run(thisNode.rangeEnd, scope);
            QObject stepObject = null;
            if (thisNode.rangeStep != null)
                stepObject = run(thisNode.rangeStep, scope);
            if (!startObject.isNum())
                error(new QUnsuitableTypeException("Number start", startObject));
            if (!endObject.isNum())
                error(new QUnsuitableTypeException("Number end", endObject));
            if (stepObject != null && !stepObject.isNum())
                error(new QUnsuitableTypeException("Number step", stepObject));
            double start = startObject.numValue();
            double end = endObject.numValue();
            double step;
            if (stepObject != null)
                step = stepObject.numValue();
            else
                step = start <= end ? 1 : -1;
            boolean isIncreasing = start <= end;
            boolean isIncluding = thisNode.isIncluding;
            double numIterator = start;
            while ((!isIncluding || ((!isIncreasing || !(numIterator > end))
                    &&
                    (isIncreasing || !(numIterator < end))))
                    &&
                    (isIncluding || ((!isIncreasing || !(numIterator >= end))
                            &&
                            (isIncreasing || !(numIterator <= end))))) {
                generated.add(Val(numIterator));
                numIterator += step;
            }
            QObject value = Val(generated);
            if (doProfile) end(node);
            return value;
        } else if (node instanceof LiteralBool) {
            LiteralBool thisNode = ((LiteralBool) node);
            QObject value = Val(thisNode.value);
            if (doProfile) end(node);
            return value;
        } else if (node instanceof LiteralClass) {
            LiteralClass thisNode = ((LiteralClass) node);
            String className = thisNode.name;
            QObject parent;
            Memory classScope = new Memory(scope);
            if (thisNode.like != null)  parent = run(thisNode.like, scope);
            else                        parent = QObject.superObject;
            for (Map.Entry<String, LiteralFunction> entry : thisNode.methods.entrySet())
                run(entry.getValue(), classScope);
            int contentsCount = thisNode.contents.size();
            for (int i = 0; i < contentsCount; i++)
                if (thisNode.contents.get(i).variable.modifiers.length == 0)
                    classScope.set(
                            this,
                            thisNode.contents.get(i).variable.name,
                            run(thisNode.contents.get(i).value, classScope)
                    );
                else
                    classScope.set(
                            thisNode.contents.get(i).variable.name,
                            run(thisNode.contents.get(i).value, classScope),
                            thisNode.contents.get(i).variable.modifiers
                    );
            int nodeCount = thisNode.initialize.size();
            for (int i = 0; i < nodeCount; i++)
                run(thisNode.initialize.get(i), classScope);
            QObject classObject = parent.extendAs(this, className);
            classObject.setTable(classScope.table);
            scope.set(this, thisNode.name, classObject);
            if (doProfile) end(node);
            return classObject;
        } else if (node instanceof LiteralDict) {
            LiteralDict thisNode = ((LiteralDict) node);
            HashMap<String, QObject> data = new HashMap<>();
            int length = thisNode.keys.size();
            for (int i = 0; i < length; i++)
                data.put(
                        run(thisNode.keys.get(i), scope).convertToString(this).strValue(),
                        run(thisNode.values.get(i), scope)
                );
            QObject dict = Val(data);
            if (doProfile) end(node);
            return dict;
        } else if (node instanceof LiteralFunction) {
            LiteralFunction thisNode = ((LiteralFunction) node);
            List<FuncArgument> arguments = new ArrayList<>();
            for (LiteralFunction.Argument arg : thisNode.args)
                arguments.add(FuncArgument.fromParsedArgument(this, arg));
            if (scope.table.containsKey(thisNode.name) &&
                scope.table.get(thisNode.name).isFunc()) {
                QFunc overriddenFunction = ((QFunc) scope.table.get(thisNode.name));
                overriddenFunction.getAlternatives().add(new AlternativeCall(arguments, thisNode.code));
            } else {
                QFunc constructedFunction = new QFunc(
                        thisNode.name,
                        arguments,
                        thisNode.code,
                        this,
                        thisNode.isStatic,
                        new ArrayList<>(),
                        scope
                );
                scope.set(this, thisNode.name, constructedFunction);
                return constructedFunction;
            }
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof LiteralLambda) {
            LiteralLambda thisNode = ((LiteralLambda) node);
            List<FuncArgument> arguments = new ArrayList<>();
            int argCount = thisNode.args.size();
            for (int i = 0; i < argCount; i++)
                arguments.add(FuncArgument.fromParsedArgument(this, thisNode.args.get(i)));
            QFunc func = new QFunc(
                    "anonymous#" + thisNode.hashCode(),
                    arguments,
                    thisNode.statement,
                    this,
                    false,
                    new ArrayList<>(),
                    scope
            );
            if (doProfile) end(node);
            return func;
        } else if (node instanceof LiteralList) {
            LiteralList thisNode = ((LiteralList) node);
            List<QObject> values = new ArrayList<>();
            int objectCount = thisNode.values.size();
            for (int i = 0; i < objectCount; i++)
                values.add(run(thisNode.values.get(i), scope));
            if (doProfile) end(node);
            return Val(values);
        } else if (node instanceof LiteralNull) {
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof LiteralNum) {
            LiteralNum thisNode = ((LiteralNum) node);
            if (doProfile) end(node);
            return Val(thisNode.value);
        } else if (node instanceof LiteralStr) {
            LiteralStr thisNode = ((LiteralStr) node);
            if (doProfile) end(node);
            return Val(thisNode.value);
        } else if (node instanceof BlockNode) {
            BlockNode thisNode = ((BlockNode) node);
            int blockSize = thisNode.nodes.size();
            for (int i = 0; i < blockSize; i++)
                run(thisNode.nodes.get(i), scope);
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof ForNode) {
            ForNode thisNode = ((ForNode) node);
            QObject iterable = run(thisNode.iterable, scope);
            int iteratorSize = thisNode.iterators.size();
            Memory enclosing = new Memory(scope);
            int remainingStrikePower = 0;
            iterable.iterateStart(this);
            while (true) {
                try {
                    QObject next = iterable.iterateNext(this);
                    if (iteratorSize == 1)
                        enclosing.table.put(this, thisNode.iterators.get(0), next);
                    else if (next.isList()) {
                        List<QObject> list = next.listValue();
                        if (list.size() != iteratorSize)
                            error("Unpacking failed. List size = " + list.size() + "; Iterators = " + iteratorSize);
                        for (int i = 0; i < iteratorSize; i++)
                            enclosing.table.put(this, thisNode.iterators.get(i), list.get(i));
                    } else if (iteratorSize == 2) {
                        String keyVar = thisNode.iterators.get(0);
                        String valVar = thisNode.iterators.get(1);
                        next.getTable().forEach((key, value) -> {
                            enclosing.table.put(keyVar, Val(key));
                            enclosing.table.put(valVar, value);
                        });
                    } else error("Iterator unpacking error. Unknown error");

                    run(thisNode.code, enclosing);
                } catch (RuntimeStriker striker) {
                    if (striker.getType() == RuntimeStriker.Type.BREAK) {
                        if (striker.getStrikePower() > 1) remainingStrikePower = striker.getStrikePower() - 1;
                        break;
                    } else if (striker.getType() == RuntimeStriker.Type.CONTINUE)
                        continue;
                    else if (striker.getType() == RuntimeStriker.Type.EXCEPTION &&
                             striker.getCarryingError().instanceOf(QIterationStopException.prototype))
                        break;
                    else throw striker;
                }
            }
            if (doProfile) end(node);
            if (remainingStrikePower > 0) throw new RuntimeStriker(
                    RuntimeStriker.Type.BREAK, remainingStrikePower,
                    currentToken.getCharacter(), currentToken.getLine(), currentToken.getLength()
            );
            return Val();
        } else if (node instanceof IfNode) {
            IfNode thisNode = ((IfNode) node);
            int conditions = thisNode.conditions.size();
            for (int i = 0; i < conditions; i++) {
                QObject condition = run(thisNode.conditions.get(i), scope);
                if (condition.isTrue()) {
                    run(thisNode.branches.get(i), scope);
                    if (doProfile) end(node);
                    return Val();
                }
            }
            if (thisNode.elseBranch != null)
                run(thisNode.elseBranch, scope);
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof LoopStopNode) {
            LoopStopNode thisNode = ((LoopStopNode) node);
            int remainingStrikePower = 0;
            while (true) {
                try {
                    run(thisNode.code, scope);
                    QObject condition = run(thisNode.condition, scope);
                    if (!condition.isTrue()) break;
                } catch (RuntimeStriker striker) {
                    if (striker.getType() == RuntimeStriker.Type.BREAK) {
                        if (striker.getStrikePower() > 1) remainingStrikePower = striker.getStrikePower() - 1;
                        break;
                    } else if (striker.getType() == RuntimeStriker.Type.CONTINUE)
                        continue;
                    else throw striker;
                }
            }
            if (doProfile) end(node);
            if (remainingStrikePower > 0) throw new RuntimeStriker(
                    RuntimeStriker.Type.BREAK, remainingStrikePower,
                    currentToken.getCharacter(), currentToken.getLine(), currentToken.getLength()
            );
            return Val();
        } else if (node instanceof ThroughNode) {
            ThroughNode thisNode = ((ThroughNode) node);
            String iterator = thisNode.iterator;
            if (thisNode.rangeStart == null || thisNode.rangeEnd == null)
                error("Attempt to make indefinite loop. Start or end of range is null");
            QObject startObject = run(thisNode.rangeStart, scope);
            QObject endObject = run(thisNode.rangeEnd, scope);
            QObject stepObject = null;
            if (thisNode.rangeStep != null)
                stepObject = run(thisNode.rangeStep, scope);
            if (!startObject.isNum())
                error(new QUnsuitableTypeException("Number start", startObject));
            if (!endObject.isNum())
                error(new QUnsuitableTypeException("Number end", endObject));
            if (stepObject != null && !stepObject.isNum())
                error(new QUnsuitableTypeException("Number step", stepObject));
            double start = startObject.numValue();
            double end = endObject.numValue();
            double step;
            if (stepObject != null)
                step = stepObject.numValue();
            else
                step = start <= end ? 1 : -1;
            boolean isIncreasing = start <= end;
            boolean isIncluding = thisNode.isIncluding;
            int remainingStrikePower = 0;
            QNumber numIterator = QObject.Val(start);
            scope.set(iterator, Val(), new int[] {ModifierConstants.FINAL});
            scope.set(this, iterator, numIterator);
            while (true) {
                try {
                    if (isIncluding && (
                            (isIncreasing && numIterator.getValue() > end)
                                    ||
                                    (!isIncreasing && numIterator.getValue() < end)
                    ) || !isIncluding && (
                            (isIncreasing && numIterator.getValue() >= end)
                                    ||
                                    (!isIncreasing && numIterator.getValue() <= end)
                    )) break;
                    run(thisNode.code, scope);
                    numIterator.setValue(numIterator.getValue() + step);
                } catch (RuntimeStriker striker) {
                    if (striker.getType() == RuntimeStriker.Type.BREAK) {
                        if (striker.getStrikePower() > 1) remainingStrikePower = striker.getStrikePower() - 1;
                        break;
                    } else if (striker.getType() == RuntimeStriker.Type.CONTINUE)
                        continue;
                    else throw striker;
                }
            }
            if (doProfile) end(node);
            if (remainingStrikePower > 0) throw new RuntimeStriker(
                    RuntimeStriker.Type.BREAK, remainingStrikePower,
                    currentToken.getCharacter(), currentToken.getLine(), currentToken.getLength()
            );
            return Val();
        } else if (node instanceof TryNode) {
            TryNode thisNode = ((TryNode) node);
            Memory localScope = new Memory(scope);
            try {
                run(thisNode.code, localScope);
            } catch (RuntimeStriker striker) {
                if (striker.getType() != RuntimeStriker.Type.EXCEPTION) {
                    if (doProfile) end(node);
                    if (striker.getType() == RuntimeStriker.Type.RETURN)
                        throw striker;
                    return Val();
                }
                QObject error = striker.getCarryingError();
                for (CatchClause clause : thisNode.catchClauses) {
                    if (clause.instance == null || error.instanceOf(run(clause.instance, localScope))) {
                        localScope.table.put(this, clause.var, error);
                        run(clause.code, localScope);
                        if (doProfile) end(node);
                        return Val();
                    }
                }
            }
            if (doProfile) end(node);
            return Val();
        } else if (node instanceof WhileNode) {
            WhileNode thisNode = ((WhileNode) node);
            int remainingStrikePower = 0;
            while (true) {
                try {
                    QObject condition = run(thisNode.condition, scope);
                    if (!condition.isTrue()) break;
                    run(thisNode.code, scope);
                } catch (RuntimeStriker striker) {
                    if (striker.getType() == RuntimeStriker.Type.BREAK) {
                        if (striker.getStrikePower() > 1) remainingStrikePower = striker.getStrikePower() - 1;
                        break;
                    } else if (striker.getType() == RuntimeStriker.Type.CONTINUE)
                        continue;
                    else throw striker;
                }
            }
            if (doProfile) end(node);
            if (remainingStrikePower > 0) throw new RuntimeStriker(
                    RuntimeStriker.Type.BREAK, remainingStrikePower,
                    currentToken.getCharacter(), currentToken.getLine(), currentToken.getLength()
            );
            return Val();
        } else if (node instanceof VariableNode) {
            VariableNode thisNode = ((VariableNode) node);
            QObject value = scope.get(thisNode.name, thisNode);
            if (doProfile) end(node);
            return value;
        }
        return null;
    }


}
