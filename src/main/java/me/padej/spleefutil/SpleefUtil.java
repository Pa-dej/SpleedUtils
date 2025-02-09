package me.padej.spleefutil;

import me.padej.displayAPI.render.shapes.Highlight;
import me.padej.spleefutil.modules.*;
import me.padej.spleefutil.modules.Pearl;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

import static me.padej.spleefutil.modules.RegenBlocks.brokenBlocks;
import static me.padej.spleefutil.modules.RegenBlocks.regenTasks;

public final class SpleefUtil extends JavaPlugin implements Listener {
    public static final Random random = new Random();

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("spleefutil")).setExecutor(new onCommand());
        getServer().getPluginManager().registerEvents(new DestroySnowballsOnHit(), this);
        getServer().getPluginManager().registerEvents(new DropSnowballs(), this);
        getServer().getPluginManager().registerEvents(new Pearl(), this);
        getServer().getPluginManager().registerEvents(new FeatherDash(), this);
        getServer().getPluginManager().registerEvents(new PowderSnowTrap(), this);
        getServer().getPluginManager().registerEvents(new RandomEffect(), this);
        getServer().getPluginManager().registerEvents(new BlocksRegen(), this);
        getServer().getPluginManager().registerEvents(new RegenBlocks(), this);
    }

    @Override
    public void onDisable() {
        // Очистка всех данных при выключении плагина
        brokenBlocks.clear();
        regenTasks.values().forEach(BukkitRunnable::cancel);
        regenTasks.clear();
        Highlight.removeAllSelections();
    }

    public static JavaPlugin getInstance() {
        return JavaPlugin.getPlugin(SpleefUtil.class);
    }

    public static void disableAllFeatures() {
        Anonim.anonymousEnabled = false;
        DestroySnowballsOnHit.destroySnowOnHitEnabled = false;
        DropSnowballs.dropSnowballsEnabled = false;
        Pearl.enderPearlEnabled = false;
        FeatherDash.dashEnabled = false;
        LowGravity.lowGravity = false;
        PowderSnowTrap.powderSnowTrapEnabled = false;
        RandomEffect.randomEffectEnabled = false;
    }

    public static void enableAllFeatures() {
        DropSnowballs.dropSnowballsEnabled = true;
        DestroySnowballsOnHit.destroySnowOnHitEnabled = true;
        RandomEffect.randomEffectEnabled = true;
        Pearl.enderPearlEnabled = true;
        PowderSnowTrap.powderSnowTrapEnabled = true;
        FeatherDash.dashEnabled = true;
    }
}