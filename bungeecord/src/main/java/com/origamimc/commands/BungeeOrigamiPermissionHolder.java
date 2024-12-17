package com.origamimc.commands;

import net.md_5.bungee.api.connection.ConnectedPlayer;

public class BungeeOrigamiPermissionHolder extends OrigamiPermissionHolder {
    private final ConnectedPlayer player;

    public BungeeOrigamiPermissionHolder(ConnectedPlayer player) {
        this.player = player;
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }
}
