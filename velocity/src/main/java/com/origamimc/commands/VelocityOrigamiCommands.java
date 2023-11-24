package com.origamimc.commands;

import com.origamimc.main.VelocityOrigamiMain;
import com.velocitypowered.api.command.SimpleCommand;

import java.util.List;
@SuppressWarnings("unused")
public class VelocityOrigamiCommands extends OrigamiCommands implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        runCommand(invocation.arguments(), VelocityOrigamiMain.getOrigamiInstances());
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
