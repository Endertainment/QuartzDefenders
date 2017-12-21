package ua.endertainment.quartzdefenders.configuration;

import org.bukkit.entity.Player;
import ua.endertainment.quartzdefenders.QuartzDefenders;

public class Info {

    Database database;

    public Info(QuartzDefenders plugin) {
        database = plugin.getDatabase();
    }

    public static int getPlayedGames(Player player) {
        //some logic
        return 0;
    }

    public static int getWonGames(Player player) {
        return 0;
    }

    public static int getPoints(Player player) {
        return 0;
    }

    public static int getCoins(Player player) {
        return 0;
    }

    public static int getKills(Player player) {
        return 0;
    }

    public static int getDeath(Player player) {
        return 0;
    }
}
