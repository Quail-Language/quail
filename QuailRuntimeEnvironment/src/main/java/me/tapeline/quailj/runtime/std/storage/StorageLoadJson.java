package me.tapeline.quailj.runtime.std.storage;

import me.tapeline.quailj.parsing.nodes.literals.LiteralFunction;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.classes.QDict;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.utils.FuncArgument;
import me.tapeline.quailj.typing.classes.utils.QBuiltinFunc;
import me.tapeline.quailj.runtime.RuntimeStriker;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class StorageLoadJson extends QBuiltinFunc {

    public StorageLoadJson(Runtime runtime) {
        super(
                "loadJson",
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

    private QObject parseJson(Object obj) {
        if (obj instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) obj;
            HashMap<String, QObject> contents = new HashMap<>();
            for (String key : jsonObject.keySet())
                contents.put(key, parseJson(jsonObject.get(key)));
            return Val(contents);
        } else if (obj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) obj;
            List<QObject> contents = new ArrayList<>();
            jsonArray.forEach(entry -> contents.add(parseJson(entry)));
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
        JSONObject jsonObject = new JSONObject(Objects.requireNonNull(args.get("data").strValue()));
        return parseJson(jsonObject);
    }

}
