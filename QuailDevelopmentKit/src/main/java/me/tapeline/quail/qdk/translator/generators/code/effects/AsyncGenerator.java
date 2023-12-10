package me.tapeline.quail.qdk.translator.generators.code.effects;

import me.tapeline.quail.qdk.translator.generators.code.CodeGenerator;
import me.tapeline.quail.qdk.translator.generators.source.QSimpleSource;
import me.tapeline.quail.qdk.translator.generators.source.QSourceGenerator;
import me.tapeline.quailj.lexing.Token;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import static me.tapeline.quail.qdk.translator.GenUtils.*;

public class AsyncGenerator extends CodeGenerator {

    private final CodeGenerator value;

    public AsyncGenerator(Token token, CodeGenerator value) {
        super(token);
        this.value = value;
    }

    @Override
    public QSourceGenerator generateSource(@Nullable CodeGenerator context) {
        QSourceGenerator valueSource = value.generateSource(this);
        String[] imports = ArrayUtils.addAll(new String[] {
                "me.tapeline.quailj.runtime.JavaAction",
                "me.tapeline.quailj.runtime.AsyncRuntimeWorker",
                "me.tapeline.quailj.runtime.RuntimeStriker",
                "me.tapeline.quailj.runtime.Runtime",
                "me.tapeline.quailj.runtime.Memory",
                "me.tapeline.quailj.typing.classes.QNull"
        }, valueSource.getImports());
        return new QSimpleSource(token, imports,
                "JavaAction action" + hashCode() + " = new JavaAction() {",
                "    public QObject action(Runtime runtime, Memory memory) throws RuntimeStriker {",
                StringUtils.join(tabulate(valueSource.getLines(), 2), "\n"),
                "        return QNull.globalNull",
                "    }",
                "};",
                "AsyncRuntimeWorker worker = new AsyncRuntimeWorker(, this, scope);",
                "asyncRuntimeWorkers.add(worker);",
                "worker.start();"
        );
    }
}
