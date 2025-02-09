package me.padej.spleefutil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static me.padej.spleefutil.SpleefUtil.disableAllFeatures;
import static me.padej.spleefutil.SpleefUtil.enableAllFeatures;
import static me.padej.spleefutil.modules.Anonim.*;
import static me.padej.spleefutil.modules.DestroySnowballsOnHit.destroySnowOnHitEnabled;
import static me.padej.spleefutil.modules.DropSnowballs.dropSnowballsEnabled;
import static me.padej.spleefutil.modules.Pearl.enderPearlEnabled;
import static me.padej.spleefutil.modules.FeatherDash.dashEnabled;
import static me.padej.spleefutil.modules.LowGravity.*;
import static me.padej.spleefutil.modules.PowderSnowTrap.powderSnowTrapEnabled;
import static me.padej.spleefutil.modules.RandomEffect.randomEffectEnabled;
import static me.padej.spleefutil.modules.RegenBlocks.blocksRegen;

public class onCommand implements CommandExecutor, TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.add("drop");
            completions.add("destroy_snowballs");
            completions.add("anonim");
            completions.add("random_effect");
            completions.add("ender_pearl");
            completions.add("powder_snow_trap");
            completions.add("low_gravity");
            completions.add("dash");
            completions.add("blocks_regen");
            completions.add("disable");
            completions.add("enable");
            completions.add("list");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("drop") || args[0].equalsIgnoreCase("destroy_snowballs") ||
                    args[0].equalsIgnoreCase("anonim") ||
                    args[0].equalsIgnoreCase("low_gravity") ||
                    args[0].equalsIgnoreCase("powder_snow_trap") ||
                    args[0].equalsIgnoreCase("random_effect") ||
                    args[0].equalsIgnoreCase("ender_pearl") ||
                    args[0].equalsIgnoreCase("dash") ||
                    args[0].equalsIgnoreCase("blocks_regen")
            ) {
                completions.add("enable");
                completions.add("disable");
            }
        }

        List<String> filteredCompletions = new ArrayList<>();
        String currentArg = args[args.length - 1];

        for (String completion : completions) {
            if (completion.startsWith(currentArg.toLowerCase())) {
                filteredCompletions.add(completion);
            }
        }

        return filteredCompletions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Эту команду может использовать только игрок.");
            return true;
        }

        Player player = (Player) sender;

        // Добавляем проверку на разрешение "permission" здесь
        if (!player.hasPermission("spleefutils.event")) {
            player.sendMessage(ChatColor.RED + "У вас нет разрешения для выполнения этой команды.");
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            // Отправляем информацию всем игрокам
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage("");
                onlinePlayer.sendMessage(ChatColor.DARK_AQUA + "[========[Spleef Utils]========]");
                onlinePlayer.sendMessage("  Снежки выпадают: " + ChatColor.GRAY + ChatColor.BOLD + (dropSnowballsEnabled));
                onlinePlayer.sendMessage("  Снежки ломают блоки: " + ChatColor.GRAY + ChatColor.BOLD + (destroySnowOnHitEnabled));
                onlinePlayer.sendMessage("  Игроки анонимны: " + ChatColor.GRAY + ChatColor.BOLD + (anonymousEnabled));
                onlinePlayer.sendMessage("  Случайные эффекты: " + ChatColor.GRAY + ChatColor.BOLD + (randomEffectEnabled));
                onlinePlayer.sendMessage("  Эндер перлы выпадают: " + ChatColor.GRAY + ChatColor.BOLD + (enderPearlEnabled));
                onlinePlayer.sendMessage("  Яйца-ловушки выпадают: " + ChatColor.GRAY + ChatColor.BOLD + (powderSnowTrapEnabled));
                onlinePlayer.sendMessage("  Слабая гравитация: " + ChatColor.GRAY + ChatColor.BOLD + (lowGravity));
                onlinePlayer.sendMessage("  Рывок: " + ChatColor.GRAY + ChatColor.BOLD + (dashEnabled));
                onlinePlayer.sendMessage("  Регенерация блоков: " + ChatColor.GRAY + ChatColor.BOLD + (blocksRegen));
                onlinePlayer.sendMessage(ChatColor.DARK_AQUA + "[============[❄]============]");
                onlinePlayer.sendMessage("");

                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ITEM_PICKUP, 1F, 1F);
            }
            return true;
        }

        if (args.length == 2) {
            // Snowballs
            if (args[0].equalsIgnoreCase("drop")) {
                if (args[1].equalsIgnoreCase("enable")) {
                    dropSnowballsEnabled = true;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Выдача снежков при разрушении блока снега включена.");
                } else if (args[1].equalsIgnoreCase("disable")) {
                    dropSnowballsEnabled = false;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Выдача снежков при разрушении блока снега выключена.");
                }
            }
            // Destroy snowballs
            if (args[0].equalsIgnoreCase("destroy_snowballs")) {
                if (args[1].equalsIgnoreCase("enable")) {
                    destroySnowOnHitEnabled = true;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Уничтожение снежков при попадании в блок снега включено.");
                } else if (args[1].equalsIgnoreCase("disable")) {
                    destroySnowOnHitEnabled = false;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Уничтожение снежков при попадании в блок снега выключено.");
                }
            }
            // Anonim
            if (args[0].equalsIgnoreCase("anonim")) {
                if (args[1].equalsIgnoreCase("enable")) {
                    anonymousEnabled = true;
                    applyAnonymousEffects();
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Анонимный режим включен.");
                } else if (args[1].equalsIgnoreCase("disable")) {
                    anonymousEnabled = false;
                    removeAnonymousEffects();
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Анонимный режим выключен.");
                }
            }
            // Low gravity
            if (args[0].equalsIgnoreCase("low_gravity")) {
                if (args[1].equalsIgnoreCase("enable")) {
                    lowGravity = true;
                    applyLowGravityEffects();
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Вы на луне.");
                } else if (args[1].equalsIgnoreCase("disable")) {
                    lowGravity = false;
                    removeLowGravityEffects();
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Вы не на луне.");
                }
            }
            // FUN: Random effect
            if (args[0].equalsIgnoreCase("random_effect")) {
                if (args[1].equalsIgnoreCase("enable")) {
                    randomEffectEnabled = true;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Случайные эффекты при разрушении блока снега включены.");
                } else if (args[1].equalsIgnoreCase("disable")) {
                    randomEffectEnabled = false;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Случайные эффекты при разрушении блока снега выключены.");
                }
            }
            // Powder snow
            if (args[0].equalsIgnoreCase("powder_snow_trap")) {
                if (args[1].equalsIgnoreCase("enable")) {
                    powderSnowTrapEnabled = true;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Яйца-ловушки включены.");
                } else if (args[1].equalsIgnoreCase("disable")) {
                    powderSnowTrapEnabled = false;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Яйца-ловушки выключены.");
                }
            }
            // Pearls
            if (args[0].equalsIgnoreCase("ender_pearl")) {
                if (args[1].equalsIgnoreCase("enable")) {
                    enderPearlEnabled = true;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Получение жемчуга Эндера при разрушении блока включено.");
                } else if (args[1].equalsIgnoreCase("disable")) {
                    enderPearlEnabled = false;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Получение жемчуга Эндера при разрушении блока выключено.");
                }
            }
            // Feather(dash)
            if (args[0].equalsIgnoreCase("dash")) {
                if (args[1].equalsIgnoreCase("enable")) {
                    dashEnabled = true;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Получение пера при разрушении блока включено.");
                } else if (args[1].equalsIgnoreCase("disable")) {
                    dashEnabled = false;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Получение пера при разрушении блока выключено.");
                }
            }
            // Blocks regen
            if (args[0].equalsIgnoreCase("blocks_regen")) {
                if (args[1].equalsIgnoreCase("enable")) {
                    blocksRegen = true;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Блоки регенерируют.");
                } else if (args[1].equalsIgnoreCase("disable")) {
                    blocksRegen = false;
                    player.sendMessage("[" + ChatColor.DARK_AQUA + "❄" + ChatColor.RESET + "] Блоки перестали регенерировать.");
                }
            }
        } else if (args.length == 1 && args[0].equalsIgnoreCase("disable")) {
            disableAllFeatures();
            player.sendMessage("[" + ChatColor.DARK_AQUA +  "❄" + ChatColor.RESET + "]" + ChatColor.RED + " Все функции выключены.");
        } else if (args.length == 1 && args[0].equalsIgnoreCase("enable")) {
            enableAllFeatures();
            player.sendMessage("[" + ChatColor.DARK_AQUA +  "❄" + ChatColor.RESET + "]" + ChatColor.GREEN + " Все функции включены.");
        }

        return true;
    }
}
