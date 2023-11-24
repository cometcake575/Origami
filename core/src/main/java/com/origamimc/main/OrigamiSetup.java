package com.origamimc.main;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.InputStream;
import java.util.logging.Level;

public abstract class OrigamiSetup {
    public OrigamiSetup() {
        saveResource("examples/default-example.yml");
        saveResource("examples/discordbot-example.yml");
        saveResource("examples/server-example.yml");
        saveResource("examples/velocity-example.yml");
    }
    public void log(String message, Level level) {
        if (level == Level.INFO) {
            getLogger().info(message);
        } else if (level == Level.WARNING) {
            getLogger().warn(message);
        } else if (level == Level.SEVERE) {
            getLogger().error(message);
        }
    }

    public abstract void saveResource(String resource);
    public abstract @NotNull InputStream getResource(String resource);
    public abstract @NotNull Logger getLogger();
}
