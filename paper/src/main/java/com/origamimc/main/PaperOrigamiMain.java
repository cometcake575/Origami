package com.origamimc.main;

import com.origamimc.OrigamiInstance;
import com.origamimc.commands.PaperOrigamiCommands;
import com.origamimc.configurations.PaperOrigamiConfiguration;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import org.slf4j.Logger;

public class PaperOrigamiMain extends JavaPlugin implements CommandExecutor, TabCompleter {

    private static final Map<String, OrigamiInstance> origamiInstances = new HashMap<>();

    public static Map<String, OrigamiInstance> getOrigamiInstances() {
        return origamiInstances;
    }

    private static PaperOrigamiMain instance;

    public static PaperOrigamiMain getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        OrigamiSetup setup = new PaperOrigamiSetup();
        saveResource("examples/default-example.yml", true);
        saveResource("examples/discordbot-example.yml", true);
        saveResource("examples/server-example.yml", true);
        saveResource("examples/velocity-example.yml", true);
        PluginCommand command = getCommand("origami");

        if (command != null) command.setExecutor(new PaperOrigamiCommands());
        File programsFolder = new File(getDataFolder(), "programs");
        boolean ignored = programsFolder.mkdirs();
        File[] files = programsFolder.listFiles();
        if (files != null) {
            for (File program : files) {
                if (program.isDirectory()) {
                    origamiInstances.put(program.getName().toLowerCase(), new OrigamiInstance(
                            program,
                            program.getName(),
                            new PaperOrigamiConfiguration(),
                            setup
                    ));
                }
            }
        }
    }

    @Override
    public void onDisable() {
        for (OrigamiInstance origamiInstance : origamiInstances.values()) {
            origamiInstance.stop(false);
        }
    }

    public static class PaperOrigamiSetup extends OrigamiSetup {

        @Override
        public void saveResource(String resource) {
            PaperOrigamiMain.getInstance().saveResource(resource, true);
        }

        @Override
        public @NotNull InputStream getResource(String resource) {
            InputStream stream = PaperOrigamiMain.getInstance().getResource(resource);
            assert stream != null;
            return stream;
        }

        @Override
        public @NotNull Logger getLogger() {
            return PaperOrigamiMain.getInstance().getSLF4JLogger();
        }
    }
}