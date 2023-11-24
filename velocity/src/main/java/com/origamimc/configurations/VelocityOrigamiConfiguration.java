package com.origamimc.configurations;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class VelocityOrigamiConfiguration extends OrigamiConfiguration {
    private Map<String, Object> node;
    private static final Yaml yaml = new Yaml();
    @Override
    @SuppressWarnings("unchecked")
    public Object get(String path) {
        String[] data = path.split("\\.");
        int i = 0;
        Map<String, Object> search = node;
        for (String s : data) {
            i++;
            if (i < data.length) {
                if (search.get(s) == null) return null;
                search = (Map<String, Object>) search.get(s);
            } else return search.get(s);
            node.get(s);
        }
        return null;
    }

    @Override
    public void load(File file) {
        try {
            node = yaml.load(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
