package me.padej.spleefutil.modules;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import static me.padej.spleefutil.Items.createFeatherDash;
import static me.padej.spleefutil.SpleefUtil.random;

public class FeatherDash implements Listener {

    public static boolean dashEnabled = false;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block brokenBlock = event.getBlock();

        if (dashEnabled && brokenBlock.getType() == Material.SNOW_BLOCK) {
            if (random.nextDouble() < 0.025 / 2) {
                player.getInventory().addItem(createFeatherDash());
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // Проверка, является ли действие кликом ПКМ
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
            ItemStack itemInOffHand = player.getInventory().getItemInOffHand();

            // Проверка, является ли предмет пером с CustomModelData 3000 в главной руке
            if (isFeatherWithCustomModelData(itemInMainHand) && player.getCooldown(Material.FEATHER) <= 0) {
                if (itemInMainHand.getAmount() > 0) { // Проверяем, что количество больше нуля
                    // Ускорение игрока
                    boostPlayer(player);

                    // Установка кулдауна на 10 тиков
                    player.setCooldown(Material.FEATHER, 10);

                    // Анимация взмаха рукой
                    player.swingMainHand();

                    // Удаление одного пера из главной руки
                    itemInMainHand.setAmount(itemInMainHand.getAmount() - 1);
                }
            } else if (isFeatherWithCustomModelData(itemInOffHand) && player.getCooldown(Material.FEATHER) <= 0) {
                if (itemInOffHand.getAmount() > 0) { // Проверяем, что количество больше нуля
                    // Ускорение игрока
                    boostPlayer(player);

                    // Установка кулдауна на 10 тиков
                    player.setCooldown(Material.FEATHER, 10);

                    // Анимация взмаха рукой
                    player.swingOffHand();

                    // Удаление одного пера из второй руки
                    itemInOffHand.setAmount(itemInOffHand.getAmount() - 1);
                }
            }
        }
    }

    private boolean isFeatherWithCustomModelData(ItemStack item) {
        if (item.getType() == Material.FEATHER && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            return meta != null && meta.hasCustomModelData() && meta.getCustomModelData() == 3000;
        }
        return false;
    }

    private void boostPlayer(Player player) {
        // Ускорение игрока
        Vector direction = player.getLocation().getDirection();
        direction.setY(0).normalize().multiply(1.35); // Горизонтальное ускорение
        direction.setY(1.15); // Вертикальное ускорение
        player.setVelocity(direction);

        // Воспроизведение звукового эффекта
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SHULKER_BULLET_HURT, 1.0f, 1.0f);

        // Показ частиц
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 5, 0.3, 0.3, 0.3, 0.01);
    }
}
