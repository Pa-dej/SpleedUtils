package me.padej.spleefutil.modules;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

import java.util.ArrayDeque;
import java.util.Random;

import static me.padej.spleefutil.Items.createPowderSnowTrapEgg;
import static me.padej.spleefutil.SpleefUtil.random;
import static me.padej.spleefutil.modules.RegenBlocks.brokenBlocks;

public class PowderSnowTrap implements Listener {

    public static boolean powderSnowTrapEnabled = false;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block brokenBlock = event.getBlock();

        if (powderSnowTrapEnabled && brokenBlock.getType() == Material.SNOW_BLOCK) {
            if (random.nextDouble() < 0.015) {
                player.getInventory().addItem(createPowderSnowTrapEgg());
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler
    public void SnowTrap(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        World world = event.getEntity().getWorld();

        if (!powderSnowTrapEnabled) {
            return;
        }

        if (projectile.getType() == EntityType.EGG) {
            Block hitBlock = event.getHitBlock();
            if (hitBlock != null && hitBlock.getType() == Material.SNOW_BLOCK) {
                int centerX = hitBlock.getX();
                int centerY = hitBlock.getY();
                int centerZ = hitBlock.getZ();

                for (int x = centerX - 1; x <= centerX + 1; x++) {
                    for (int y = centerY - 1; y <= centerY + 1; y++) {
                        for (int z = centerZ - 1; z <= centerZ + 1; z++) {
                            Block targetBlock = world.getBlockAt(x, y, z);
                            Material targetBlockType = targetBlock.getType();
                            if (targetBlockType == Material.SNOW_BLOCK || targetBlockType == Material.POWDER_SNOW) {
                                targetBlock.setType(Material.POWDER_SNOW);
                                brokenBlocks.computeIfAbsent(projectile.getOwnerUniqueId(), k -> new ArrayDeque<>()).addLast(targetBlock);
                                world.spawnParticle(Particle.SNOWFLAKE, targetBlock.getLocation().add(0.55, 1.7, 0.55), 25, 0.5, 0.5, 0.5, 0.1);
                            }
                        }
                    }
                }
            }
        }
    }
}
