package com.origamimc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class OrigamiCommands implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        StringBuilder origamiCommand = new StringBuilder();
        if (args.length < 2) return false;
        String origamiInstanceName = args[0];
        OrigamiInstance origamiInstance = OrigamiMain.getOrigamiInstances().get(origamiInstanceName);
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
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return new ArrayList<>() {{
                for (String instanceName : OrigamiMain.getOrigamiInstances().keySet()) {
                    if (OrigamiMain.getOrigamiInstances().get(instanceName).isCanSendCommands()) {
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
}
