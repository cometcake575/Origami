package com.origamimc;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class OrigamiInstance {
    private OutputStream outputStream;
    private Process process;

    public boolean isRunning() {
        return running;
    }

    public void sendCommand(String command, boolean force) {
        if (!force && !canSendCommands) return;
        try {
            outputStream.write((command + "\n").getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final File jarFile;
    private boolean running;
    private final FileConfiguration config;
    private Thread thread;
    String name;
    private boolean canSendCommands;

    public boolean isCanSendCommands() {
        return canSendCommands;
    }

    public OrigamiInstance(File programFolder, String name) {
        this.name = name;
        running = false;
        config = new YamlConfiguration();
        File configFile = new File(programFolder, "origami.yml");
        if (!configFile.exists()) {
            InputStream is = OrigamiMain.getInstance().getResource("origami.yml");
            assert is != null;
            BufferedInputStream inputStream = new BufferedInputStream(is);
            try {
                boolean ignored = configFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (BufferedOutputStream fileOutputStream = new BufferedOutputStream(new FileOutputStream(configFile))) {
                while (is.available() > 0) {
                    fileOutputStream.write(inputStream.readAllBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            config.load(configFile);
        } catch (InvalidConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
        String fileName = config.getString("executing.jar-name");
        if (fileName == null) {
            OrigamiMain.getInstance().getLogger().warning("Malformed origami.yml in %s".formatted(programFolder));
            jarFile = null;
            return;
        }
        canSendCommands = config.getBoolean("console.commands-enabled");
        jarFile = new File(programFolder, fileName);
        start();
    }

    public void start() {
        if (running) stop(false);
        try {
            String command = config.getString("executing.start-command");
            if (command == null) {
                OrigamiMain.getInstance().getLogger().warning("Malformed origami.yml in %s".formatted(jarFile.getParentFile().getPath()));
                return;
            }
            command = command.replace("JARFILE", jarFile.getName());
            process = Runtime.getRuntime().exec(command, new String[0], jarFile.getParentFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        running = true;
        outputStream = process.getOutputStream();

        if (config.getBoolean("console.output-enabled")) {
            Bukkit.broadcast(Component.text(1));
            InputStream in = process.getInputStream();
            InputStream err = process.getErrorStream();
            thread = new Thread(() -> {

                BufferedReader inputReader = new BufferedReader(new InputStreamReader(in));
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(err));

                String line;
                try {
                    while ((line = inputReader.readLine()) != null) {
                        OrigamiMain.getInstance().getLogger().info("%s output: %s".formatted(name, line));
                    }
                } catch (IOException e) {
                    thread.interrupt();
                }

                try {
                    while ((line = errorReader.readLine()) != null) {
                        OrigamiMain.getInstance().getLogger().severe("%s error: %s".formatted(name, line));
                    }
                } catch (IOException e) {
                    thread.interrupt();
                }
            });

            thread.start();
        }
    }

    public void stop(boolean force) {
        if (!running) return;
        running = false;
        String stopCommand = config.getString("executing.stop-command");
        if (force || stopCommand == null || stopCommand.equals("NONE")) process.destroy();
        else sendCommand(stopCommand, true);
        OrigamiMain.getInstance().getLogger().info("Program %s shutting down".formatted(name));
    }
}
