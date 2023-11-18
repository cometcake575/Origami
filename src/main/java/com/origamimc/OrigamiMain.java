package com.origamimc;

import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class OrigamiMain extends JavaPlugin implements CommandExecutor, TabCompleter {

    private static final Map<String, OrigamiInstance> origamiInstances = new HashMap<>();

    public static Map<String, OrigamiInstance> getOrigamiInstances() {
        return origamiInstances;
    }

    private static OrigamiMain instance;

    public static OrigamiMain getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveResource("examples/discordbot-example.yml", true);
        saveResource("examples/server-example.yml", true);
        saveResource("examples/velocity-example.yml", true);
        PluginCommand command = getCommand("origami");
        if (command != null) command.setExecutor(new OrigamiCommands());
        File programsFolder = new File(getDataFolder(), "programs");
        boolean ignored = programsFolder.mkdirs();
        File[] files = programsFolder.listFiles();
        if (files != null) {
            for (File program : files) {
                if (program.isDirectory()) {
                    origamiInstances.put(program.getName().toLowerCase(), new OrigamiInstance(program, program.getName()));
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
}