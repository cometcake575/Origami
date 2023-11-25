package com.origamimc.commands;

import com.origamimc.main.VelocityOrigamiMain;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;

import java.util.List;
@SuppressWarnings("unused")
public class VelocityOrigamiCommands implements OrigamiCommands, SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        OrigamiPermissionHolder permissionHolder;
        if (invocation.source() instanceof Player player) {
            permissionHolder = new VelocityOrigamiPermissionHolder(player);
        } else permissionHolder = OrigamiPermissionHolder.consolePermissions;
        if (runCommand(invocation.arguments(), VelocityOrigamiMain.getOrigamiInstances(), permissionHolder)) {
            invocation.source().sendMessage(Component.text("Command usage: /origami <instance> <input>"));
        }
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        return getSuggestions(invocation.arguments(), VelocityOrigamiMain.getOrigamiInstances());
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        return invocation.source().hasPermission("origami.command");
    }
}
