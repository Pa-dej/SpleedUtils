package me.padej.spleefutil.modules;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static me.padej.spleefutil.SpleefUtil.random;

public class DropSnowballs implements Listener {

    public static boolean dropSnowballsEnabled = false;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block brokenBlock = event.getBlock();

        if (dropSnowballsEnabled && brokenBlock.getType() == Material.SNOW_BLOCK) {
            if (random.nextDouble() < 0.15) {
                player.getInventory().addItem(new ItemStack(Material.SNOWBALL, 1));
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
            }
        }
    }

}
