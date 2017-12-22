package ua.endertainment.quartzdefenders.configuration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bukkit.entity.Player;
import ua.endertainment.quartzdefenders.QuartzDefenders;

public class Info {

    private static Database database;
    private static PreparedStatement statement;

    private static String selectPlayers = "SELECT * FROM " + Database.prefix + "_players WHERE UUID = '?' LIMIT 1";
    private static String selectGames = "SELECT * FROM " + Database.prefix + "_games WHERE id = '?' LIMIT 1";
    private static String selectStats = "SELECT * FROM " + Database.prefix + "_stats WHERE UUID = '?' LIMIT 1";

    public Info(QuartzDefenders plugin) {
        database = plugin.getDatabase();
    }

    public static int getPlayedGames(Player player) {
        int games = 0;
        try (ResultSet query = getPlayerInfo(player)) {
            while (query.next()) {
                games = query.getInt("games");
            }
        } catch (SQLException ex) {
            database.error(ex);
        }
        return games;
    }

    public static int getWonGames(Player player) {
        int won = 0;
        try (ResultSet query = getPlayerInfo(player)) {
            while (query.next()) {
                won = query.getInt("wins");
            }
        } catch (SQLException ex) {
            database.error(ex);
        }
        return won;
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

    private static ResultSet getPlayerInfo(Player player) {
        try {
            statement = database.getConnection().prepareStatement(selectStats);
            statement.setString(1, player.getUniqueId().toString());
            ResultSet query = database.query(statement);
            return query;
        } catch (SQLException ex) {
            database.error(ex);
        }
        return null;
    }
}
