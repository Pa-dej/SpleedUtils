package me.padej.spleefutil.modules;

import me.padej.spleefutil.SpleefUtil;
import me.padej.spleefutil.utils.ArenaUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class Pearl implements Listener {

    public static boolean enderPearlEnabled = false;
    private final Random random = new Random();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!enderPearlEnabled) return;
        Player player = event.getPlayer();
        Block brokenBlock = event.getBlock();

        if (brokenBlock.getType() == Material.SNOW_BLOCK) {
            if (random.nextDouble() < 0.007) {
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!enderPearlEnabled) return;
        if (!(event.getEntity() instanceof EnderPearl)) return;

        EnderPearl pearl = (EnderPearl) event.getEntity();
        if (!(pearl.getShooter() instanceof Player)) return;

        Player player = (Player) pearl.getShooter();
        Location launchLocation = player.getLocation();

        // Проверяем, бросил ли игрок перл в арене
        if (ArenaUtils.isInArena(launchLocation)) {
            Vector velocity = pearl.getVelocity();
            pearl.setVelocity(velocity.multiply(1.5));
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!enderPearlEnabled) return;
        if (!(event.getEntity() instanceof EnderPearl)) return;

        EnderPearl pearl = (EnderPearl) event.getEntity();
        if (!(pearl.getShooter() instanceof Player)) return;

        Player player = (Player) pearl.getShooter();
        Block hitBlock = event.getHitBlock();
        if (hitBlock == null || hitBlock.getType() != Material.SNOW_BLOCK) return;

        Location hitLocation = hitBlock.getLocation();
        if (!ArenaUtils.isInArena(hitLocation)) return;

        // Запускаем телепортацию через 1 тик
        new BukkitRunnable() {
            @Override
            public void run() {
                Location teleportLocation = hitLocation.add(0, 1.5, 0); // Центр блока и поднимаем на 1.5 блока вверх
                teleportLocation.setYaw(player.getLocation().getYaw());   // Сохраняем поворот игрока
                teleportLocation.setPitch(player.getLocation().getPitch());

                player.teleport(teleportLocation);
                player.getWorld().spawnParticle(Particle.SONIC_BOOM, teleportLocation, 1, 0, 0, 0, 0);
            }
        }.runTaskLater(SpleefUtil.getInstance(), 1); // Задержка в 1 тик
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!enderPearlEnabled) return;
        Player player = event.getPlayer();

        if (player.getInventory().contains(Material.ENDER_PEARL) && enderPearlEnabled) {
            Location particleLocation = player.getLocation().add(0, 2.5, 0);
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 179, 74), 1.0f);
            player.getWorld().spawnParticle(Particle.DUST, particleLocation, 3, 0, 0, 0, 1, dustOptions);
        }
    }
}
