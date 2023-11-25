package com.origamimc.commands;

public abstract class OrigamiPermissionHolder {
    public static OrigamiPermissionHolder consolePermissions = new OrigamiPermissionHolder() {
        @Override
        public boolean hasPermission(String permission) {
            return true;
        }
    };
    public abstract boolean hasPermission(String permission);
}
