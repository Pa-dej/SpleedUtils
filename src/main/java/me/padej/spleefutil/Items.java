package me.padej.spleefutil;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Items {

    public static ItemStack createPowderSnowTrapEgg() {
        ItemStack egg = new ItemStack(Material.EGG, 1);
        ItemMeta eggMeta = egg.getItemMeta();
        eggMeta.setDisplayName(ChatColor.YELLOW + "Яйцо-ловушка");
        egg.setItemMeta(eggMeta);
        return egg;
    }

    public static ItemStack createFeatherDash() {
        ItemStack feather = new ItemStack(Material.FEATHER, 1);
        ItemMeta featherMeta = feather.getItemMeta();
        featherMeta.setDisplayName(ChatColor.YELLOW + "Рывок");
        featherMeta.setCustomModelData(3000);
        feather.setItemMeta(featherMeta);
        return feather;
    }
}
