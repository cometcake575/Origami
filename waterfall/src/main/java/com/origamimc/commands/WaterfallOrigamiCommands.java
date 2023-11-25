package com.origamimc.commands;

import com.origamimc.main.WaterfallOrigamiMain;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

@SuppressWarnings("unused")
public class WaterfallOrigamiCommands extends Command implements OrigamiCommands, TabExecutor {
    public WaterfallOrigamiCommands(String name, String permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (runCommand(args, WaterfallOrigamiMain.getOrigamiInstances())) {
            sender.sendMessage(TextComponent.fromLegacyText("Command usage: /origami <instance> <input>"));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return getSuggestions(args, WaterfallOrigamiMain.getOrigamiInstances());
    }
}
