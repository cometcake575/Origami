package com.origamimc.commands;

import net.md_5.bungee.api.connection.ConnectedPlayer;

public class WaterfallOrigamiPermissionHolder extends OrigamiPermissionHolder {
    private final ConnectedPlayer player;

    public WaterfallOrigamiPermissionHolder(ConnectedPlayer player) {
        this.player = player;
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }
}
