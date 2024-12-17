package com.origamimc.main;

import com.origamimc.OrigamiInstance;
import com.origamimc.Logger;
import com.origamimc.BungeeLogger;
import com.origamimc.WaterfallLogger;
import com.origamimc.commands.BungeeOrigamiCommands;
import com.origamimc.configurations.BungeeOrigamiConfiguration;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BungeeOrigamiMain extends Plugin {
    private static final Map<String, OrigamiInstance> origamiInstances = new HashMap<>();

    public static Map<String, OrigamiInstance> getOrigamiInstances() {
        return origamiInstances;
    }

    public Logger logger;
    private static BungeeOrigamiMain instance;
    private File dataDirectory;

    @Override
    public void onEnable() {
        instance = this;

        this.dataDirectory = getDataFolder();

        try {
            // try to assign waterfall logger
            Method getSLF4JLoggerMethod = this.getClass().getMethod("getSLF4JLogger");
            if (getSLF4JLoggerMethod != null) {
                getProxy().getPluginManager().registerCommand(this, new BungeeOrigamiCommands(
                        "origami",
                        "origami.command",
                        "origami-waterfall"));
                this.logger = new WaterfallLogger((org.slf4j.Logger) getSLF4JLoggerMethod.invoke(this));
            }
        } catch (NoSuchMethodException e) {
            getProxy().getPluginManager().registerCommand(this, new BungeeOrigamiCommands(
                    "origami",
                    "origami.command",
                    "origami-bungee"));
            this.logger = new BungeeLogger(getLogger());
        } catch (IllegalAccessException | InvocationTargetException e) {
            // Handle reflection errors during method invocation
            throw new RuntimeException("Error invoking getSLF4JLogger method.", e);
        } catch (Exception ex) {
            // Catch any other unexpected exceptions
            throw new RuntimeException("Error initializing logger", ex);
        }
        OrigamiSetup setup = new BungeeOrigamiSetup();

        File programsFolder = new File(dataDirectory, "programs");
        boolean ignored = programsFolder.mkdirs();
        File[] files = programsFolder.listFiles();
        if (files != null) {
            for (File program : files) {
                if (program.isDirectory()) {
                    origamiInstances.put(program.getName().toLowerCase(), new OrigamiInstance(
                            program,
                            program.getName(),
                            new BungeeOrigamiConfiguration(),
                            setup));
                }
            }
        }
    }

    @Override
    public void onDisable() {
        for (OrigamiInstance origamiInstance : BungeeOrigamiMain.getOrigamiInstances().values()) {
            origamiInstance.stop(false);
        }
    }

    public static BungeeOrigamiMain getInstance() {
        return instance;
    }

    public static class BungeeOrigamiSetup extends OrigamiSetup {

        @Override
        public void saveResource(String resource) {
            File file = new File(BungeeOrigamiMain.getInstance().dataDirectory, resource);
            boolean ignored1 = file.getParentFile().mkdirs();
            try {
                boolean ignored2 = file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                for (byte b : getResource(resource).readAllBytes()) {
                    outputStream.write(b);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public @NotNull InputStream getResource(String resource) {
            InputStream stream = getClass().getClassLoader().getResourceAsStream(resource);
            assert stream != null;
            return stream;
        }

        @Override
        public @NotNull Logger getLogger() {
            return BungeeOrigamiMain.getInstance().logger;
        }
    }
}