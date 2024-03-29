package me.tapeline.quailj.runtime;

import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;
import me.tapeline.quailj.utils.IntFlags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Memory {

    public final Table table = new Table();
    public final Memory enclosing;

    public Memory() {
        this.enclosing = null;
    }

    public Memory(Memory m) {
        this.enclosing = m;
    }

    public boolean contains(String key) {
        if (table.containsKey(key)) return true;
        if (enclosing != null) return enclosing.contains(key);
        return false;
    }

    public void set(Runtime runtime, String id, QObject value) throws RuntimeStriker {
        if (enclosing != null) {
            if (enclosing.contains(id))
                enclosing.set(runtime, id, value);
            else
                table.put(runtime, id, value);
        } else table.put(runtime, id, value);
    }

    public void set(String id, QObject value, int[] modifiers) {
        table.put(id, value, modifiers);
    }

    public void set(String id, QObject value) {
        table.put(id, value);
    }

    public QObject get(String id) {
        QObject value = null;
        if (table.getValues().containsKey(id))
            value = table.get(id);
        else if (enclosing != null) value = enclosing.get(id);

        if (value == null) {
            value = QObject.Val();
            table.put(id, value);
        }
        return value;
    }

    public QObject get(String id, VariableNode node) {
        QObject alreadyUsed = null;
        if (table.getValues().containsKey(id))
            alreadyUsed = table.get(id);
        else if (enclosing != null) alreadyUsed = enclosing.get(id);
        if (alreadyUsed == null) {
            QObject newObject = QObject.Val();
            if (node.modifiers.length > 0) {
                if (IntFlags.check(node.modifiers[0], ModifierConstants.NUM))
                    newObject = QObject.Val(0);
                else if (IntFlags.check(node.modifiers[0], ModifierConstants.BOOL))
                    newObject = QObject.Val(false);
                else if (IntFlags.check(node.modifiers[0], ModifierConstants.LIST))
                    newObject = QObject.Val(new ArrayList<>());
                else if (IntFlags.check(node.modifiers[0], ModifierConstants.STR))
                    newObject = QObject.Val("");
                else if (IntFlags.check(node.modifiers[0], ModifierConstants.DICT))
                    newObject = QObject.Val(new HashMap<>());
            }
            table.put(id, newObject, node.modifiers);
            return newObject;
        } else return alreadyUsed;
    }

    public Set<String> getAllKeys() {
        Set<String> keys = table.keySet();
        if (enclosing != null)
            keys.addAll(enclosing.getAllKeys());
        return keys;
    }

    public Set<Map.Entry<String, QObject>> getAllEntries() {
        Set<Map.Entry<String, QObject>> entries = table.getValues().entrySet();
        if (enclosing != null)
            entries.addAll(enclosing.getAllEntries());
        return entries;
    }

}
