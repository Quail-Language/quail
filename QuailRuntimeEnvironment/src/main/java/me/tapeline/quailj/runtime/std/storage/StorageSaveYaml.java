package me.tapeline.quailj.runtime.std.storage;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.*;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageSaveYaml extends QBuiltinFunc {

    public StorageSaveYaml(Runtime runtime) {
        super(
                "saveYaml",
                Arrays.asList(
                        new FuncArgument(
                               "data",
                                QObject.Val(),
                                new int[] {ModifierConstants.DICT},
                                LiteralFunction.Argument.POSITIONAL
                        ),
                        new FuncArgument(
                               "path",
                                QObject.Val(),
                                new int[] {ModifierConstants.STR},
                                LiteralFunction.Argument.POSITIONAL
                        )
                ),
                runtime,
                runtime.getMemory(),
                false
        );
    }

    private Object constructYaml(QObject object) {
        if (object instanceof QList) {
            return ((QList) object).getValues().stream().map(this::constructYaml);
        } else if (object instanceof QNull) {
            return null;
        } else if (object instanceof QNumber) {
            return object.numValue();
        } else if (object instanceof QBool) {
            return object.boolValue();
        } else if (object instanceof QString) {
            return object.strValue();
        } else if (object instanceof QDict) {
            Map<String, Object> contents = new HashMap<>();
            for (Map.Entry<String, QObject> entry : ((QDict) object).getValues().entrySet())
                contents.put(entry.getKey(), constructYaml(entry.getValue()));
            return contents;
        } else return object.toString();
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        Yaml yaml = new Yaml();
        return Val(yaml.dump(constructYaml(args.get("data"))));
    }

}
