package com.origamimc.commands;

import org.bukkit.entity.Player;

public class PaperOrigamiPermissionHolder extends OrigamiPermissionHolder {
    private final Player player;

    public PaperOrigamiPermissionHolder(Player player) {
        this.player = player;
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }
}
