package com.origamimc;

import com.origamimc.configurations.OrigamiConfiguration;
import com.origamimc.main.OrigamiSetup;

import java.io.*;
import java.util.logging.Level;

public class OrigamiInstance {
    private OutputStream outputStream;
    private Process process;
    private final OrigamiSetup origamiSetup;

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
    private final OrigamiConfiguration config;
    private Thread errorThread;
    private Thread inputThread;
    String name;
    private boolean canSendCommands;

    public boolean isCanSendCommands() {
        return canSendCommands;
    }

    public OrigamiInstance(File programFolder, String name, OrigamiConfiguration config, OrigamiSetup setup) {
        this.name = name;
        this.origamiSetup = setup;
        running = false;
        this.config = config;
        File configFile = new File(programFolder, "origami.yml");
        if (!configFile.exists()) {
            InputStream is = origamiSetup.getResource("origami.yml");
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
        config.load(configFile);
        String fileName = config.getString("executing.jar-name");
        if (fileName == null) {
            origamiSetup.log("Malformed origami.yml in %s".formatted(programFolder), Level.WARNING);
            jarFile = null;
            return;
        }
        canSendCommands = config.getBoolean("console.commands-enabled");
        jarFile = new File(programFolder, fileName);
        commandPermission = config.getString("console.command-permission");
        start();
    }

    private String commandPermission;

    public String getCommandPermission() {
        return commandPermission;
    }

    public void start() {
        if (running) stop(false);
        try {
            String command = config.getString("executing.start-command");
            if (command == null) {
                origamiSetup.log("Malformed origami.yml in %s".formatted(jarFile.getParentFile().getPath()), Level.WARNING);
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
            InputStream in = process.getInputStream();
            InputStream err = process.getErrorStream();
            inputThread = new Thread(() -> {

                BufferedReader inputReader = new BufferedReader(new InputStreamReader(in));

                String line;
                try {
                    while ((line = inputReader.readLine()) != null) {
                        origamiSetup.log("%s output: %s".formatted(name, line), Level.INFO);
                    }
                } catch (IOException e) {
                    inputThread.interrupt();
                }
            });
            
            errorThread = new Thread(() -> {

                BufferedReader errorReader = new BufferedReader(new InputStreamReader(err));

                String line;
                try {
                    while ((line = errorReader.readLine()) != null) {
                        origamiSetup.log("%s error: %s".formatted(name, line), Level.SEVERE);
                    }
                } catch (IOException e) {
                    inputThread.interrupt();
                }
            });

            errorThread.start();
            inputThread.start();
        }
    }

    public void stop(boolean force) {
        if (!running) return;
        running = false;
        String stopCommand = config.getString("executing.stop-command");
        if (force || stopCommand == null || stopCommand.equals("NONE")) process.destroy();
        else sendCommand(stopCommand, true);
        origamiSetup.log("Program '%s' is shutting down".formatted(name), Level.INFO);
    }
}
