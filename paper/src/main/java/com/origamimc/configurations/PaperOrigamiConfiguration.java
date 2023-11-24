package com.origamimc.configurations;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PaperOrigamiConfiguration extends OrigamiConfiguration {
    private final FileConfiguration configuration = new YamlConfiguration();
    @Override
    public Object get(String path) {
        return configuration.get(path);
    }

    @Override
    public void load(File file) {
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
}
