package com.origamimc.commands;

import com.origamimc.main.PaperOrigamiMain;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PaperOrigamiCommands implements OrigamiCommands, CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        OrigamiPermissionHolder permissionHolder;
        if (sender instanceof Player player) {
            permissionHolder = new PaperOrigamiPermissionHolder(player);
        } else permissionHolder = OrigamiPermissionHolder.consolePermissions;
        if (runCommand(args, PaperOrigamiMain.getOrigamiInstances(), permissionHolder)) {
            sender.sendMessage(Component.text("Command usage: /origami <instance> <input>"));
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return getSuggestions(args, PaperOrigamiMain.getOrigamiInstances());
    }
}
