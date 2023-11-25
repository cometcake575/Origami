package com.origamimc.main;

import com.origamimc.OrigamiInstance;
import com.origamimc.commands.WaterfallOrigamiCommands;
import com.origamimc.configurations.WaterfallOrigamiConfiguration;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class WaterfallOrigamiMain extends Plugin {
    private static final Map<String, OrigamiInstance> origamiInstances = new HashMap<>();

    public static Map<String, OrigamiInstance> getOrigamiInstances() {
        return origamiInstances;
    }

    public Logger logger;
    private static WaterfallOrigamiMain instance;
    private File dataDirectory;

    @Override
    public void onEnable() {
        instance = this;

        this.dataDirectory = getDataFolder();

        getProxy().getPluginManager().registerCommand(this, new WaterfallOrigamiCommands(
                "origami",
                "origami.command",
                "origami-waterfall"
        ));

        this.logger = getSLF4JLogger();
        OrigamiSetup setup = new WaterfallOrigamiSetup();

        File programsFolder = new File(dataDirectory, "programs");
        boolean ignored = programsFolder.mkdirs();
        File[] files = programsFolder.listFiles();
        if (files != null) {
            for (File program : files) {
                if (program.isDirectory()) {
                    origamiInstances.put(program.getName().toLowerCase(), new OrigamiInstance(
                            program,
                            program.getName(),
                            new WaterfallOrigamiConfiguration(),
                            setup
                    ));
                }
            }
        }
    }

    @Override
    public void onDisable() {
        for (OrigamiInstance origamiInstance : WaterfallOrigamiMain.getOrigamiInstances().values()) {
            origamiInstance.stop(false);
        }
    }

    public static WaterfallOrigamiMain getInstance() {
        return instance;
    }

    public static class WaterfallOrigamiSetup extends OrigamiSetup {

        @Override
        public void saveResource(String resource) {
            File file = new File(WaterfallOrigamiMain.getInstance().dataDirectory, resource);
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
            return WaterfallOrigamiMain.getInstance().logger;
        }
    }
}