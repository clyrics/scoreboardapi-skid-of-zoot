package net.centilehcf.core;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;

import java.text.MessageFormat;

@AllArgsConstructor
public enum Locale {

    FAILED_TO_LOAD_PROFILE("COMMON_ERRORS.FAILED_TO_LOAD_PROFILE"),
    COULD_NOT_RESOLVE_PLAYER("COMMON_ERRORS.COULD_NOT_RESOLVE_PLAYER"),
    PLAYER_NOT_FOUND("COMMON_ERRORS.PLAYER_NOT_FOUND"),
    RANK_NOT_FOUND("COMMON_ERRORS.RANK_NOT_FOUND"),
    STAFF_CHAT("STAFF.CHAT"),
    STAFF_REQUEST("STAFF.REQUEST"),
    STAFF_BROADCAST_PREFIX("STAFF.BROADCAST_PREFIX"),
    STAFF_REQUEST_PREFIX("STAFF.REQUEST_PREFIX"),
    STAFF_JOIN_NETWORK("STAFF.JOIN_NETWORK"),
    STAFF_SWITCH_SERVER("STAFF.SWITCH_SERVER"),
    STAFF_LEAVE_NETWORK("STAFF.LEAVE_NETWORK"),
    SERVER_STATUS("SERVER.STATUS"),
    SERVER_STATUS_PREFIX("SERVER.PREFIX"),
    NETWORK_BROADCAST_PREFIX("NETWORK.BROADCAST_PREFIX"),
    NETWORK_RANK_REFRESHED("NETWORK.RANK_REFRESH"),
    NETWORK_RANK_DELETED("NETWORK.RANK_DELETE");

    private String path;

    public String format(Object... objects) {
        return new MessageFormat(ChatColor.translateAlternateColorCodes('&',
                Core.get().getMainConfig().getString(path))).format(objects);
    }
}
