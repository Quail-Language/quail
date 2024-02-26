package me.tapeline.quailj.addons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QuailAddonRegistry {

    protected static HashMap<String, QuailAddon> registeredAddons = new HashMap<>();

    public static void registerAddon(QuailAddon addon) throws AddonAlreadyRegisteredException {
        if (registeredAddons.containsKey(addon.getName()))
            throw new AddonAlreadyRegisteredException(addon);
        registeredAddons.put(addon.getName(), addon);
    }

    public static QuailAddon getAddonByName(String name) {
        return registeredAddons.get(name);
    }

    public static List<QuailAddon> getAddons() {
        return new ArrayList<>(registeredAddons.values());
    }

    public static void unregisterAddon(String name) throws AddonNotRegisteredException {
        if (!registeredAddons.containsKey(name))
            throw new AddonNotRegisteredException(name);
        registeredAddons.remove(name);
    }

    public static boolean addonRegistered(String name) {
        return registeredAddons.containsKey(name);
    }

}
