package com.origamimc.configurations;

import java.io.File;

public abstract class OrigamiConfiguration {
    public boolean getBoolean(String path) {
        return Boolean.TRUE.equals(get(path));
    }
    public String getString(String path) {
        Object o = get(path);
        if (o == null) return null;
        if (!(o instanceof String)) return o.toString();
        return (String) o;
    }
    public abstract Object get(String path);
    public abstract void load(File file);
}
