package com.origamimc.commands;

import com.velocitypowered.api.proxy.Player;

public class VelocityOrigamiPermissionHolder extends OrigamiPermissionHolder {
    private final Player player;

    public VelocityOrigamiPermissionHolder(Player player) {
        this.player = player;
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }
}
