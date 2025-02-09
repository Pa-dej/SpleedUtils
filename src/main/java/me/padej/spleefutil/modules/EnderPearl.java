package me.padej.spleefutil.modules;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import static me.padej.spleefutil.SpleefUtil.random;

public class EnderPearl implements Listener {

    public static boolean enderPearlEnabled = false;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block brokenBlock = event.getBlock();


        if (enderPearlEnabled && brokenBlock.getType() == Material.SNOW_BLOCK) {
            if (random.nextDouble() < 0.007) {
                player.getInventory().addItem(new ItemStack(Material.ENDER_PEARL, 1));
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Проверяем, есть ли ENDER_PEARL в инвентаре игрока
        if (player.getInventory().contains(Material.ENDER_PEARL) && enderPearlEnabled) {
            Location particleLocation = player.getLocation().add(0, 2.5, 0);
            Particle.DustOptions dustOptions = new Particle.DustOptions(Color.fromRGB(0, 179, 74), 1.0f);
            player.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 10, 0, 0, 0, 1, dustOptions);
        }
    }
}
