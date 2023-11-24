package com.origamimc.main;

import com.origamimc.OrigamiInstance;
import com.origamimc.configurations.VelocityOrigamiConfiguration;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Plugin(
        id = "origami",
        name = "Origami",
        version = "1.0.0",
        description = "Run other jar files inside of Velocity servers",

        authors = {"cometcake575"}
)
public class VelocityOrigamiMain {
    private static final Map<String, OrigamiInstance> origamiInstances = new HashMap<>();

    public static Map<String, OrigamiInstance> getOrigamiInstances() {
        return origamiInstances;
    }

    public final Logger logger;
    private static VelocityOrigamiMain instance;
    private final Path dataDirectory;
    @Inject
    public VelocityOrigamiMain(Logger logger, @DataDirectory Path dataDirectory) {
        instance = this;

        this.dataDirectory = dataDirectory;


        this.logger = logger;
        OrigamiSetup setup = new VelocityOrigamiSetup();

        File programsFolder = new File(dataDirectory.toFile(), "programs");
        boolean ignored = programsFolder.mkdirs();
        File[] files = programsFolder.listFiles();
        if (files != null) {
            for (File program : files) {
                if (program.isDirectory()) {
                    origamiInstances.put(program.getName().toLowerCase(), new OrigamiInstance(
                            program,
                            program.getName(),
                            new VelocityOrigamiConfiguration(),
                            setup
                    ));
                }
            }
        }
    }
    @Subscribe
    @SuppressWarnings("unused")
    public void onProxyShutdown(ProxyShutdownEvent ignored) {
        for (OrigamiInstance origamiInstance : VelocityOrigamiMain.getOrigamiInstances().values()) {
            origamiInstance.stop(false);
        }
    }

    public static VelocityOrigamiMain getInstance() {
        return instance;
    }

    public static class VelocityOrigamiSetup extends OrigamiSetup {

        @Override
        public void saveResource(String resource) {
            File file = new File(VelocityOrigamiMain.getInstance().dataDirectory.toFile(), resource);
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
            return VelocityOrigamiMain.getInstance().logger;
        }
    }
}