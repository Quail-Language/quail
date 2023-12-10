package me.tapeline.quail.qdk.translator.generators.code.effects;

import me.tapeline.quail.qdk.translator.generators.code.CodeGenerator;
import me.tapeline.quail.qdk.translator.generators.source.QSimpleSource;
import me.tapeline.quail.qdk.translator.generators.source.QSourceGenerator;
import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public class EffectGenerator extends CodeGenerator {

    private final CodeGenerator value;
    private final TokenType effectType;

    public EffectGenerator(Token token, TokenType effectType, CodeGenerator value) {
        super(token);
        this.value = value;
        this.effectType = effectType;
    }

    private QSourceGenerator generateSourceForAssert(QSourceGenerator valueSource, @Nullable CodeGenerator context) {
        String[] imports = ArrayUtils.addAll(new String[] {
                "me.tapeline.quailj.typing.classes.errors.QAssertionException"
        }, valueSource.getImports());
        return new QSimpleSource(token, imports,
                "if (" + StringUtils.join(valueSource.getLines(), " ") + ".isFalse())",
                "    error(new QAssertionException(\"Assertion failed. Expected true, but got false\"))"
        );
    }

    private QSourceGenerator generateSourceForImport(QSourceGenerator valueSource, @Nullable CodeGenerator context) {
        throw new IllegalArgumentException("Import is not yet supported");
    }

    private QSourceGenerator generateSourceForStrike(QSourceGenerator valueSource, @Nullable CodeGenerator context) {
        String[] imports = ArrayUtils.addAll(new String[] {
                "me.tapeline.quailj.typing.classes.QObject",
                "me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException",
                "me.tapeline.quailj.runtime.RuntimeStriker",
        }, valueSource.getImports());
        return new QSimpleSource(token, imports,
                "QObject strikeAmount = " + StringUtils.join(valueSource.getLines(), " ") + ";",
                        "if (!strikeAmount.isNum())",
                        "    error(new QUnsuitableTypeException(\"Number\", strikeAmount));",
                        "if (strikeAmount.numValue() < 1)",
                        "    error(new QUnsuitableTypeException(\"Expected n > 1, but got \" + strikeAmount.numValue(),",
                        "            strikeAmount));",
                        "throw new RuntimeStriker(",
                        "        RuntimeStriker.Type.BREAK,",
                        "        ((int) strikeAmount.numValue()),",
                        "        currentToken.getCharacter(), currentToken.getLine(), currentToken.getLength()",
                        ");"
        );
    }

    private QSourceGenerator generateSourceForThrow(QSourceGenerator valueSource, @Nullable CodeGenerator context) {
        String[] imports = ArrayUtils.addAll(new String[] {
                "me.tapeline.quailj.typing.classes.QObject",
                "me.tapeline.quailj.typing.classes.errors.QUnsuitableTypeException",
                "me.tapeline.quailj.runtime.RuntimeStriker",
        }, valueSource.getImports());
        return new QSimpleSource(token, imports,
                "throw new RuntimeStriker(",
                "        RuntimeStriker.Type.EXCEPTION,",
                "        " + StringUtils.join(valueSource.getLines(), " ") + ",",
                "        currentToken.getCharacter(), currentToken.getLine(), currentToken.getLength()",
                ");"
        );
    }

    @Override
    public QSourceGenerator generateSource(@Nullable CodeGenerator context) {
        QSourceGenerator valueSource = value.generateSource(this);
        switch (effectType) {
            case EFFECT_ASSERT: return generateSourceForAssert(valueSource, context);
            case EFFECT_IMPORT: return generateSourceForImport(valueSource, context);
            case EFFECT_STRIKE: return generateSourceForStrike(valueSource, context);
            case EFFECT_THROW: return generateSourceForThrow(valueSource, context);
        }
        throw new IllegalArgumentException("Effect type " + effectType + "does not fall to any of existing types");
    }
}
