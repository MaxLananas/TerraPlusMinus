package de.btegermany.terraplusminus.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public enum Permission {
    DISTORTION_CMD(Properties.NonConfigurable.PERMISSION_PREFIX + "distortion"),
    OFFSET_CMD(Properties.NonConfigurable.PERMISSION_PREFIX + "offset"),
    WHERE_CMD(Properties.NonConfigurable.PERMISSION_PREFIX + "where");

    private final String node;

    Permission(String node) {
        this.node = node;
    }

    public boolean isGrantedTo(@NonNull CommandSender sender) {
        return sender.hasPermission(node);
    }

    public boolean isGrantedTo(@NonNull Player player) {
        return player.hasPermission(node);
    }
}
