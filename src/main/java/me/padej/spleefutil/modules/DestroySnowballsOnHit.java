package me.padej.spleefutil.modules;

import me.padej.spleefutil.SpleefUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

import static me.padej.spleefutil.modules.RegenBlocks.brokenBlocks;
import static org.bukkit.entity.EntityType.PLAYER;

public class DestroySnowballsOnHit implements Listener {

    public static boolean destroySnowOnHitEnabled = true;

    @EventHandler
    public void DestroySnowball(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        World world = event.getEntity().getWorld();

        if (!destroySnowOnHitEnabled) {
            return;
        }

        if (projectile.getType() == EntityType.SNOWBALL) {
            Block hitBlock = event.getHitBlock();
            if (hitBlock != null && (hitBlock.getType() == Material.SNOW_BLOCK)) {
                hitBlock.setType(Material.AIR);
                brokenBlocks.computeIfAbsent(projectile.getOwnerUniqueId(), k -> new ArrayDeque<>()).addLast(hitBlock);
                projectile.remove();
                world.spawnParticle(Particle.SNOWFLAKE, hitBlock.getLocation().add(0.5, 1.0, 0.5), 30, 0.5, 0.0, 0.5, 0);
            }
        }
    }

}
