package me.padej.spleefutil.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LowGravity {

    public static boolean lowGravity = false;

    public static void applyLowGravityEffects() {
        Location center = new Location(Bukkit.getWorld("world"), 867, 88, -652); // Указываем мир и координаты

        for (Player player : Bukkit.getOnlinePlayers()) {
            Location playerLocation = player.getLocation(); // Получаем местоположение игрока

            if (playerLocation.distance(center) <= 70) { // Проверяем, находится ли игрок в радиусе 200 блоков
                PotionEffect invisible = new PotionEffect(PotionEffectType.SLOW_FALLING, 900 * 20, 0, false, false);
                PotionEffect glowing = new PotionEffect(PotionEffectType.JUMP_BOOST, 900 * 20, 2, false, false);

                player.addPotionEffect(invisible);
                player.addPotionEffect(glowing);

                Anonim.anonymousEffects.put(player, invisible);
            }
        }
    }

    public static void removeLowGravityEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (Anonim.anonymousEffects.containsKey(player)) {
                player.removePotionEffect(PotionEffectType.SLOW_FALLING);
                player.removePotionEffect(PotionEffectType.JUMP_BOOST);

                Anonim.anonymousEffects.remove(player);
            }
        }
    }

}
