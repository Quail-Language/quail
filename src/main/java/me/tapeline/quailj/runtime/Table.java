package me.tapeline.quailj.runtime;

import me.tapeline.quailj.typing.classes.QObject;
import me.tapeline.quailj.typing.modifiers.ModifierConstants;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.function.BiConsumer;

public class Table {

    private final HashMap<String, QObject> values = new HashMap<>();
    private final HashMap<String, Integer[]> modifiers = new HashMap<>();

    public Table() { }

    public Table(HashMap<String, QObject> values) {
        this.values.putAll(values);
    }

    public Table(HashMap<String, QObject> values, HashMap<String, Integer[]> modifiers) {
        this(values);
        this.modifiers.putAll(modifiers);
    }

    public void put(String name, QObject value) throws RuntimeStriker {
        if (modifiers.containsKey(name)) {
            Integer[] modifier = modifiers.get(name);
            boolean hadMatch = false;
            if (modifier.length == 1) hadMatch = ModifierConstants.matchesOnAssign(modifier[0], value);
            else for (Integer flags : modifier)
                if (ModifierConstants.matchesOnAssign(flags, value)) {
                    hadMatch = true;
                    break;
                }
            if (!hadMatch)
                Runtime.error("Attempt to assign wrong data to clarified variable");
        } else {
            values.put(name, value);
        }
    }

    public void put(String name, QObject value, Integer[] modifier) {
        values.put(name, value);
        modifiers.put(name, modifier);
    }

    public boolean containsKey(String name) {
        return values.containsKey(name);
    }

    public QObject get(String name) {
        return values.get(name);
    }

    public Set<String> keySet() {
        return values.keySet();
    }

    public int size() {
        return values.size();
    }

    public Collection<QObject> values() {
        return values.values();
    }

    public HashMap<String, QObject> getValues() {
        return values;
    }

    public Integer[] getModifiersFor(String key) {
        return modifiers.get(key);
    }

    public void putAll(Table table) {
        table.values.forEach((k, d) -> {
            values.put(k, d);
            modifiers.put(k, table.modifiers.get(k));
        });
    }

    public void putAll(HashMap<String, QObject> table) {
        values.putAll(table);
    }

    public void forEach(BiConsumer<String, QObject> consumer) {
        values.forEach(consumer);
    }

    public void clear() {
        values.clear();
        modifiers.clear();
    }

}
