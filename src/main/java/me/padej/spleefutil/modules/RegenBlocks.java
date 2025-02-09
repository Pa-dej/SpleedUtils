package me.padej.spleefutil.modules;

import me.padej.spleefutil.SpleefUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.*;

import static net.md_5.bungee.api.ChatColor.*;

public class RegenBlocks implements Listener {

    public static boolean blocksRegen = false;

    // Хранилище ломаемых блоков для каждого игрока
    public static final Map<UUID, Deque<Block>> brokenBlocks = new HashMap<>();
    // Хранилище задач восстановления блоков для каждого игрока
    public static final Map<UUID, BukkitRunnable> regenTasks = new HashMap<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getType() == Material.SNOW_BLOCK && blocksRegen) {
            UUID playerId = event.getPlayer().getUniqueId();
            brokenBlocks.computeIfAbsent(playerId, k -> new ArrayDeque<>()).addLast(block);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if ((event.getTo().getY() < 85 && event.getFrom().getY() >= 85) && blocksRegen) {
            UUID playerId = event.getPlayer().getUniqueId();
            if (brokenBlocks.containsKey(playerId) && !regenTasks.containsKey(playerId)) {
                startRegeneration(playerId);
            }
        }
    }

    public void startRegeneration(UUID playerId) {
        Deque<Block> blocksToRegen = brokenBlocks.get(playerId);

        // Создание новой задачи восстановления блоков
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!blocksToRegen.isEmpty()) {
                    Block block = blocksToRegen.pollFirst();
                    if (block != null) {
                        // Создаем падающий блок с текстурой снега
                        FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation().add(0.5, -1, 0.5), Material.SNOW_BLOCK.createBlockData());
                        fallingBlock.setDropItem(false); // Отключаем дроп при падении
                        fallingBlock.setTicksLived(100);
                        fallingBlock.setVelocity(new BlockVector(0, 0.37, 0));

                        // Планировщик для удаления падающего блока и установки настоящего блока снега
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                block.setType(Material.BARRIER); // Устанавливаем настоящий блок снега
                            }
                        }.runTaskLater(JavaPlugin.getPlugin(SpleefUtil.class), 1); // Задержка в 20 тиков (1 секунда)
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                fallingBlock.remove(); // Удаляем падающий блок
                                block.setType(Material.SNOW_BLOCK); // Устанавливаем настоящий блок снега
                            }
                        }.runTaskLater(JavaPlugin.getPlugin(SpleefUtil.class), 5); // Задержка в 20 тиков (1 секунда)
                    }
                } else {
                    // Если массив пуст, очищаем его и завершаем задачу
                    brokenBlocks.remove(playerId);
                    regenTasks.remove(playerId);
                    cancel(); // Останавливаем таск
                }
            }
        };


        // Запуск таска, который будет выполняться каждый тик
        task.runTaskTimer(JavaPlugin.getPlugin(SpleefUtil.class), 0L, 1); // Изменено на 1L для визуализации каждые 1 тик

        // Сохранение задачи в списке задач для этого игрока
        regenTasks.put(playerId, task);
    }
}
