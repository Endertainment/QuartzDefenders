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
        try (ResultSet query = getInfo(player, Type.STATS)) {
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
        try (ResultSet query = getInfo(player, Type.STATS)) {
            while (query.next()) {
                won = query.getInt("wins");
            }
        } catch (SQLException ex) {
            database.error(ex);
        }
        return won;
    }

    public static int getPoints(Player player) {
        int points = 0;
        try (ResultSet query = getInfo(player, Type.PLAYER)) {
            while (query.next()) {
                points = query.getInt("points");
            }
        } catch (SQLException ex) {
            database.error(ex);
        }
        return points;
    }

    public static int getCoins(Player player) {
        int coins = 0;
        try (ResultSet query = getInfo(player, Type.PLAYER)) {
            while (query.next()) {
                coins = query.getInt("coins");
            }
        } catch (SQLException ex) {
            database.error(ex);
        }
        return coins;
    }

    public static int getKills(Player player) {
        int kills = 0;
        try (ResultSet query = getInfo(player, Type.STATS)) {
            while (query.next()) {
                kills = query.getInt("kills");
            }
        } catch (SQLException ex) {
            database.error(ex);
        }
        return kills;
    }

    public static int getDeath(Player player) {
        int deaths = 0;
        try (ResultSet query = getInfo(player, Type.STATS)) {
            while (query.next()) {
                deaths = query.getInt("deaths");
            }
        } catch (SQLException ex) {
            database.error(ex);
        }
        return deaths;
    }

    private static ResultSet getInfo(Player player, Type type) {
        try {
            statement = database.getConnection().prepareStatement(type.getSelectQuery());
            statement.setString(1, player.getUniqueId().toString());
            ResultSet query = database.query(statement);
            return query;
        } catch (SQLException ex) {
            database.error(ex);
        }
        return null;
    }
    
    private static ResultSet setInfo(Player player, Type type, Object info) {
        try {
            statement = database.getConnection().prepareStatement("TODO");
            statement.setString(1, player.getUniqueId().toString());
            ResultSet query = database.query(statement);
            return query;
        } catch (SQLException ex) {
            database.error(ex);
        }
        return null;
    }

    private enum Type {
        PLAYER, GAME, STATS;

        public String getSelectQuery() {
            switch (this) {
                case GAME:
                    return selectGames;
                case PLAYER:
                    return selectPlayers;
                case STATS:
                    return selectStats;
            }
            return "";
        }
    }
}
