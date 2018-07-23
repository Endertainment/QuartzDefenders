package ua.endertainment.quartzdefenders.utils;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;

public abstract class DataAdapter {

    public static Material getWoolByColor(DyeColor color) {
        return getMaterialByColor(color, "WOOL");
    }
    
    public static Material getGlassPaneByColor(DyeColor color) {
        return getMaterialByColor(color, "STAINED_GLASS_PANE");
    }
    
    public static Material getGlassByColor(DyeColor color) {
        return getMaterialByColor(color, "STAINED_GLASS");
    }

    public static Material getMaterialByColor(DyeColor color, String suffix) {
        return Material.getMaterial(color + "_" + suffix);
    }
    
    public static DyeColor getDyeColor(ChatColor color) {
        switch(color) {
            case AQUA: return DyeColor.CYAN;
            case BLACK: return DyeColor.BLACK;
            case BLUE: return DyeColor.LIGHT_BLUE;
            case DARK_AQUA: return DyeColor.CYAN;
            case DARK_BLUE: return DyeColor.BLUE;
            case DARK_GRAY: return DyeColor.GRAY;
            case DARK_GREEN: return DyeColor.GREEN;
            case DARK_PURPLE: return DyeColor.PURPLE;
            case DARK_RED: return DyeColor.RED;
            case GRAY: return DyeColor.LIGHT_GRAY;
            case GREEN: return DyeColor.LIME;
            case LIGHT_PURPLE: return DyeColor.PINK;
            case RED: return DyeColor.RED;
            case YELLOW: return DyeColor.YELLOW;
            default: return DyeColor.WHITE;
        }
    }
}
