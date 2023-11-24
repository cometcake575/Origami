package com.origamimc.commands;

import com.origamimc.OrigamiInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrigamiCommands {
    public List<String> getSuggestions(String[] args, Map<String, OrigamiInstance> origamiInstances) {
        if (args.length == 1) {
            return new ArrayList<>() {{
                for (String instanceName : origamiInstances.keySet()) {
                    if (origamiInstances.get(instanceName).isCanSendCommands()) {
                        add(instanceName);
                    }
                }
            }};
        } else if (args.length == 2) {
            return new ArrayList<>() {{
                add("stop");
                add("start");
                add("kill");
                add("restart");
                add("command");
            }};
        }
        return new ArrayList<>();
    }

    public boolean runCommand(String[] args, Map<String, OrigamiInstance> origamiInstances) {
        StringBuilder origamiCommand = new StringBuilder();
        if (args.length < 2) return true;
        String origamiInstanceName = args[0];
        OrigamiInstance origamiInstance = origamiInstances.get(origamiInstanceName.toLowerCase());
        if (origamiInstance == null) return true;
        switch (args[1]) {
            case "command" -> {
                for (int arg = 2; arg < args.length; arg++) {
                    origamiCommand.append(args[arg]).append(" ");
                }
                if (!origamiInstance.isRunning()) {
                    origamiInstance.start();
                }
                origamiInstance.sendCommand(origamiCommand.toString(), false);
            }
            case "start" -> {
                if (!origamiInstance.isRunning()) {
                    origamiInstance.start();
                }
            }
            case "stop" -> {
                if (origamiInstance.isRunning()) {
                    origamiInstance.stop(false);
                }
            }
            case "kill" -> {
                if (origamiInstance.isRunning()) {
                    origamiInstance.stop(true);
                }
            }
            case "restart" -> {
                origamiInstance.stop(false);
                origamiInstance.start();
            }
        }
        return false;
    }
}
