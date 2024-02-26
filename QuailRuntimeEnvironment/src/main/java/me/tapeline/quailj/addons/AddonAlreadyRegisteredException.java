package me.tapeline.quailj.addons;

public class AddonAlreadyRegisteredException extends Exception {

    protected QuailAddon addon;

    public AddonAlreadyRegisteredException(QuailAddon addon) {
        super("Addon " + addon + " already registered");
        this.addon = addon;
    }

    public QuailAddon getAddon() {
        return addon;
    }

}
