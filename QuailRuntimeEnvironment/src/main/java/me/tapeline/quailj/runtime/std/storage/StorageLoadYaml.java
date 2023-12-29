package me.tapeline.quailj.runtime.std.storage;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yaml.snakeyaml.Yaml;

import java.util.*;

public class StorageLoadYaml extends QBuiltinFunc {

    public StorageLoadYaml(Runtime runtime) {
        super(
                "loadYaml",
                Arrays.asList(
                        new FuncArgument(
                               "data",
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

    private QObject parseYaml(Object obj) {
        if (obj instanceof Map<?, ?>) {
            Map<?, ?> yamlObject = (Map<?, ?>) obj;
            HashMap<String, QObject> contents = new HashMap<>();
            for (Object key : yamlObject.keySet())
                contents.put(key.toString(), parseYaml(yamlObject.get(key)));
            return Val(contents);
        } else if (obj instanceof List<?>) {
            List<?> yamlList = (List<?>) obj;
            List<QObject> contents = new ArrayList<>();
            yamlList.forEach(entry -> contents.add(parseYaml(entry)));
            return Val(contents);
        } else if (obj instanceof Number) {
            return Val((Double) obj);
        } else if (obj instanceof Boolean) {
            return Val(((Boolean) obj));
        } else if (obj instanceof String) {
            return Val(((String) obj));
        } else if (obj == null) {
            return Val();
        }
        return Val();
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args, List<QObject> argList) throws RuntimeStriker {
        Yaml yaml = new Yaml();
        return parseYaml(yaml.load(args.get("data").strValue()));
    }

}
