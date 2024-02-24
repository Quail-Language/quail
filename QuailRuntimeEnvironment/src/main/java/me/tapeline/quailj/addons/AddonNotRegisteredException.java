package me.tapeline.quailj.addons;

public class AddonNotRegisteredException extends Exception {

    protected String addon;

    public AddonNotRegisteredException(String addon) {
        super("Addon " + addon + " is not registered");
        this.addon = addon;
    }

    public String getAddon() {
        return addon;
    }

}
