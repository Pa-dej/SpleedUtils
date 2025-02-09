package me.padej.spleefutil.utils;

import org.bukkit.Location;

public class ArenaUtils {

    public static boolean isInArena(Location loc) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        return (x >= 833 && x <= 902) && (y >= 3 && y <= 114) && (z >= -687 && z <= -618);
    }
}
