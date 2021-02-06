package ua.endertainment.quartzdefenders.utils;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Tag;

public abstract class DataAdapter {
    
    public static String getMaterialColorPrefix(ChatColor color) {
        return fromChatColor(color).name();
    }
    
    public static Material getForColor(ChatColor color, String base) {
        String id = getMaterialColorPrefix(color) + "_"+base;
        return Material.getMaterial(id.toUpperCase());
    }
    
    public static Material getForColor(ChatColor color, Tag<Material> group) {
        String prefix = getMaterialColorPrefix(color);
        for(Material mat : group.getValues()) {
            if(mat.name().startsWith(prefix)) return mat;
        }
        return null;
    }
    
    public static Material getForColor(ChatColor color, String base, Material defaultt) {
        String id = getMaterialColorPrefix(color) + "_"+base;
        Material mat = Material.getMaterial(id.toUpperCase());
        if(mat == null) mat = defaultt;
        return mat;
    }
    
    public static Material getForColor(ChatColor color, Tag<Material> group, Material defaultt) {
        String prefix = getMaterialColorPrefix(color);
        for(Material mat : group.getValues()) {
            if(mat.name().startsWith(prefix)) return mat;
        }
        return defaultt;
    }
    
    public static DyeColor fromChatColor(ChatColor color) {
        switch (color) {
            case WHITE:
                return DyeColor.WHITE;
            case BLACK:
                return DyeColor.BLACK;
            case AQUA:
                return DyeColor.LIGHT_BLUE;
            case BLUE:
            case DARK_BLUE:
                return DyeColor.BLUE;
            case DARK_AQUA:
                return DyeColor.CYAN;
            case DARK_GRAY:
                return DyeColor.GRAY;
            case DARK_GREEN:
                return DyeColor.GREEN;
            case DARK_PURPLE:
                return DyeColor.PURPLE;
            case RED:
            case DARK_RED:
                return DyeColor.RED;
            case GREEN:
                return DyeColor.LIME;
            case GRAY:
                return DyeColor.LIGHT_GRAY;
            case LIGHT_PURPLE:
                return DyeColor.PINK;
            case YELLOW:
                return DyeColor.YELLOW;
            case GOLD:
                return DyeColor.ORANGE;
            default:
                return DyeColor.BLACK;
        }
    }

}
