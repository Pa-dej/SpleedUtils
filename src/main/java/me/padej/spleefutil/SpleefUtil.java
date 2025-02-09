package me.padej.spleefutil;

import me.padej.spleefutil.modules.*;
import me.padej.spleefutil.modules.EnderPearl;
import org.bukkit.*;
import org.bukkit.block.Block;
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
        getCommand("spleefutil").setExecutor(new onCommand());
        getServer().getPluginManager().registerEvents(new DestroySnowballsOnHit(), this);
        getServer().getPluginManager().registerEvents(new DropSnowballs(), this);
        getServer().getPluginManager().registerEvents(new EnderPearl(), this);
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
    }

    public static void disableAllFeatures() {
        Anonim.anonymousEnabled = false;
        DestroySnowballsOnHit.destroySnowOnHitEnabled = false;
        DropSnowballs.dropSnowballsEnabled = false;
        EnderPearl.enderPearlEnabled = false;
        FeatherDash.dashEnabled = false;
        LowGravity.lowGravity = false;
        PowderSnowTrap.powderSnowTrapEnabled = false;
        RandomEffect.randomEffectEnabled = false;
    }

    public static void enableAllFeatures() {
        DropSnowballs.dropSnowballsEnabled = true;
        DestroySnowballsOnHit.destroySnowOnHitEnabled = true;
        RandomEffect.randomEffectEnabled = true;
        EnderPearl.enderPearlEnabled = true;
        PowderSnowTrap.powderSnowTrapEnabled = true;
        FeatherDash.dashEnabled = true;
    }
}