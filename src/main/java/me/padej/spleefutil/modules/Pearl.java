package me.padej.spleefutil.modules;

import me.padej.spleefutil.SpleefUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
        Location start = player.getEyeLocation();
        Vector direction = start.getDirection().normalize().multiply(0.25);

        Location current = start.clone();
        Block targetBlock = null;

        // 1. Мгновенный рэйкаст (до 360 шагов)
        for (int i = 0; i < 360; i++) {
            current.add(direction);
            Block block = current.getBlock();

            if (block.getType() == Material.SNOW_BLOCK) {
                targetBlock = block;
                break;
            }
        }

        // 2. Если найден снег — делаем визуальную проходку
        if (targetBlock != null) {
            pearl.remove();

            Location visualStart = start.clone();
            Location finalTarget = targetBlock.getLocation();

            new BukkitRunnable() {
                final Location visualCurrent = visualStart.clone();
                int steps = 0;

                @Override
                public void run() {
                    if (steps >= 360) {
                        cancel();
                        return;
                    }

                    for (int i = 0; i < 8 && steps < 360; i++, steps++) {
                        visualCurrent.add(direction);
                        player.getWorld().spawnParticle(Particle.SCULK_CHARGE_POP, visualCurrent, 1, 0, 0, 0, 0);

                        if (visualCurrent.getBlock().getLocation().equals(finalTarget)) {
                            cancel();

                            Location teleportLocation = finalTarget.add(0.5, 1.5, 0.5);
                            teleportLocation.setYaw(player.getLocation().getYaw());
                            teleportLocation.setPitch(player.getLocation().getPitch());

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    player.teleport(teleportLocation);
                                    player.getWorld().spawnParticle(Particle.SONIC_BOOM, teleportLocation, 1, 0, 0, 0, 0);
                                    player.playSound(teleportLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                                }
                            }.runTaskLater(SpleefUtil.getInstance(), 1);
                            break;
                        }
                    }
                }
            }.runTaskTimer(SpleefUtil.getInstance(), 0L, 1L);
        }
    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!enderPearlEnabled) return;
        Player player = event.getPlayer();

        if (player.getInventory().contains(Material.ENDER_PEARL)) {
            Location particleLocation = player.getLocation().add(0, 2.5, 0);
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 179, 74), 1.0f);
            player.getWorld().spawnParticle(Particle.DUST, particleLocation, 3, 0, 0, 0, 1, dustOptions);
        }
    }
}

