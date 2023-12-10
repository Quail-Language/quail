package me.tapeline.quail.qdk.libconverter;

import java.net.URL;
import java.net.URLClassLoader;

public class CustomClassLoader extends URLClassLoader {

    public CustomClassLoader(URL[] urls) {
        super(urls);
    }

    public void addURL(URL url) {
        super.addURL(url);
    }

}
