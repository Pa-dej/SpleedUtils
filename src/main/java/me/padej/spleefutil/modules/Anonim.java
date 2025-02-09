package me.padej.spleefutil.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class Anonim {

    public static boolean anonymousEnabled = false;
    public static final HashMap<Player, PotionEffect> anonymousEffects = new HashMap<>();

    public static void applyAnonymousEffects() {
        Location center = new Location(Bukkit.getWorld("world"), 867, 88, -652); // Указываем мир и координаты

        for (Player player : Bukkit.getOnlinePlayers()) {
            Location playerLocation = player.getLocation(); // Получаем местоположение игрока

            if (playerLocation.distance(center) <= 70) { // Проверяем, находится ли игрок в радиусе 200 блоков
                PotionEffect invisible = new PotionEffect(PotionEffectType.INVISIBILITY, 900 * 20, 0, false, false);
                PotionEffect glowing = new PotionEffect(PotionEffectType.GLOWING, 900 * 20, 0, false, false);

                player.addPotionEffect(invisible);
                player.addPotionEffect(glowing);

                anonymousEffects.put(player, invisible);
            }
        }
    }

    public static void removeAnonymousEffects() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (anonymousEffects.containsKey(player)) {
                player.removePotionEffect(PotionEffectType.INVISIBILITY);
                player.removePotionEffect(PotionEffectType.GLOWING);

                player.spawnParticle(Particle.FLAME, player.getLocation(), 1, 0, 0, 0, 0);


                anonymousEffects.remove(player);
            }
        }
    }
}
