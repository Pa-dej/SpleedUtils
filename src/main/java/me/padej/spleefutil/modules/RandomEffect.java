package me.padej.spleefutil.modules;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static me.padej.spleefutil.SpleefUtil.random;

public class RandomEffect implements Listener {

    public static boolean randomEffectEnabled = false;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block brokenBlock = event.getBlock();

        if (randomEffectEnabled && brokenBlock.getType() == Material.SNOW_BLOCK) {
            if (random.nextDouble() < 0.05) {
                applyRandomEffect(player);
            }
        }
    }

    private void applyRandomEffect(Player player) {
        PotionEffectType[] effects = {
                PotionEffectType.SPEED,
                PotionEffectType.JUMP,
                PotionEffectType.SLOW_FALLING,
                PotionEffectType.SLOW
        };

        PotionEffectType randomEffect = effects[random.nextInt(effects.length)];
        int duration = random.nextInt(16) + 5; // 5 to 20 seconds
        int amplifier = random.nextInt(5) + 1; // 1 to 5

        PotionEffect effect = new PotionEffect(randomEffect, duration * 20, amplifier - 1, false, false);
        player.addPotionEffect(effect);
    }
}
