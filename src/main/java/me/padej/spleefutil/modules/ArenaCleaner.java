package me.padej.spleefutil.modules;

import me.padej.displayAPI.render.HighlightStyle;
import me.padej.displayAPI.render.shapes.Highlight;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ArenaCleaner implements Listener {

    // Минимальные и максимальные координаты
    private static final Location MIN_CORNER = new Location(Bukkit.getWorld("world"), 833, 87, -687);
    private static final Location MAX_CORNER = new Location(Bukkit.getWorld("world"), 902, 87, -618);

    // Метод для подсветки периметра
    public static void highlightPerimeter() {
        // Подсвечиваем каждую сторону (периметр) используя мин. и макс. координаты
        highlightSide(MIN_CORNER, new Location(Bukkit.getWorld("world"), MAX_CORNER.getBlockX(), MIN_CORNER.getBlockY(), MIN_CORNER.getBlockZ())); // Верхняя сторона
        highlightSide(MIN_CORNER, new Location(Bukkit.getWorld("world"), MIN_CORNER.getBlockX(), MIN_CORNER.getBlockY(), MAX_CORNER.getBlockZ())); // Левая сторона
        highlightSide(new Location(Bukkit.getWorld("world"), MAX_CORNER.getBlockX(), MIN_CORNER.getBlockY(), MIN_CORNER.getBlockZ()), MAX_CORNER); // Правая сторона
        highlightSide(new Location(Bukkit.getWorld("world"), MIN_CORNER.getBlockX(), MIN_CORNER.getBlockY(), MAX_CORNER.getBlockZ()), new Location(Bukkit.getWorld("world"), MAX_CORNER.getBlockX(), MIN_CORNER.getBlockY(), MAX_CORNER.getBlockZ())); // Нижняя сторона
    }

    // Метод для подсветки одной стороны (линии) периметра
    private static void highlightSide(Location start, Location end) {
        Highlight.createSides(start, HighlightStyle.RUBY, (int) start.distance(end)); // Подсветка от старта до конца
    }

    @EventHandler
    public void interaction(PlayerInteractEvent event) {
        if (event.getAction().isRightClick()) {
            ArenaCleaner.highlightPerimeter(); // Подсвечиваем периметр при ПКМ
        }
    }
}
