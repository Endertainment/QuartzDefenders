package ua.Endertainment.QuartzDefenders.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class DataAdapter {

    public static ItemStack getTerracotaByData(short data) {
        switch (data) {
            case 0:
                return new ItemStack(Material.WHITE_GLAZED_TERRACOTTA);
            case 1:
                return new ItemStack(Material.ORANGE_GLAZED_TERRACOTTA);
            case 2:
                return new ItemStack(Material.LIGHT_BLUE_GLAZED_TERRACOTTA);
        }
        return new ItemStack(Material.WHITE_GLAZED_TERRACOTTA);
    }

    public static Short getDamageByColor(ChatColor color) {
        switch (color) {
            case WHITE:
                return 0;
            case GOLD:
                return 1;
            case AQUA:
                return 3;
            case YELLOW:
                return 4;
            case GREEN:
                return 5;
            case LIGHT_PURPLE:
                return 6;
            case DARK_GRAY:
                return 7;
            case GRAY:
                return 8;
            case DARK_AQUA:
                return 9;
            case DARK_PURPLE:
                return 10;
            case BLUE:
                return 11;
            case DARK_GREEN:
                return 13;
            case RED:
                return 14;
            case BLACK:
                return 15;
            default:
                return 0;
        }
    }

}
