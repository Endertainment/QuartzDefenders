package ua.endertainment.quartzdefenders.configuration;

import java.sql.Date;
import ua.endertainment.quartzdefenders.configuration.database.Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import org.bukkit.entity.Player;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class Info {

    private static Database database;
    private static PreparedStatement statement;

    private static String selectPlayers = "SELECT * FROM " + Database.PREFIX + "_players WHERE UUID = '?' LIMIT 1";
    private static String selectGames = "SELECT * FROM " + Database.PREFIX + "_games WHERE id = '?' LIMIT 1";
    private static String selectStats = "SELECT * FROM " + Database.PREFIX + "_stats WHERE UUID = '?' LIMIT 1";

    public Info(QuartzDefenders plugin) {
        database = plugin.getDatabase();
    }

    public static int getPlayedGames(Player player) {
        int games = 0;
        try (ResultSet query = getInfo(player, Type.STATS)) {
            while (query.next()) {
                games = query.getInt("games");
                query.close();
            }
        } catch (SQLException ex) {
            database.error(ex);
        }
        return games;
    }

    public static boolean addGame(Player player) {
        return addGames(player, 1);
    }

    public static boolean addGames(Player player, int count) {
        return add(player, count, Type.PLAYER, StatsColumns.games.getName());
    }
    
    public static boolean setGames(Player player, int amount) {
        return set(player, amount, Type.STATS, StatsColumns.games.getName());
    }

    public static int getWonGames(Player player) {
        int won = 0;
        try (ResultSet query = getInfo(player, Type.STATS)) {
            while (query.next()) {
                won = query.getInt("wins");
                query.close();
            }
        } catch (SQLException ex) {
            database.error(ex);
        }
        return won;
    }

    public static boolean addWonGame(Player player) {
        return addKills(player, 1);
    }

    public static boolean addWonGames(Player player, int count) {
        return add(player, count, Type.STATS, StatsColumns.wins.getName());
    }
    public static boolean setWonGames(Player player, int amount) {
        return set(player, amount, Type.STATS, StatsColumns.wins.getName());
    }

    public static int getPoints(Player player) {
        int points = 0;
        try (ResultSet query = getInfo(player, Type.PLAYER)) {
            while (query.next()) {
                points = query.getInt("points");
                query.close();
            }
        } catch (SQLException ex) {
            database.error(ex);
        }
        return points;
    }

    public static boolean addPoints(Player player, int count) {
        return add(player, count, Type.PLAYER, PlayersColumns.points.getName());
    }
    
    public static boolean setPoints(Player player, int amount) {
        return set(player, amount, Type.PLAYER, PlayersColumns.points.getName());
    }
    
    public static int getCoins(Player player) {
        int coins = 0;
        try (ResultSet query = getInfo(player, Type.PLAYER)) {
            while (query.next()) {
                coins = query.getInt("coins");
                query.close();
            }
        } catch (SQLException ex) {
            database.error(ex);
        }
        return coins;
    }

    public static boolean addCoins(Player player, int count) {
        return add(player, count, Type.PLAYER, PlayersColumns.coins.getName());
    }
    
    public static boolean setCoins(Player player, int amount) {
        return set(player, amount, Type.PLAYER, PlayersColumns.coins.getName());
    }

    public static int getKills(Player player) {
        int kills = 0;
        try (ResultSet query = getInfo(player, Type.STATS)) {
            while (query.next()) {
                kills = query.getInt("kills");
                query.close();
            }
        } catch (SQLException ex) {
            database.error(ex);
        }
        return kills;
    }

    public static boolean addKill(Player player) {
        return addKills(player, 1);
    }

    public static boolean addKills(Player player, int count) {
        return add(player, count, Type.STATS, StatsColumns.kills.getName());
    }
    
    public static boolean setKills(Player player, int amount) {
        return set(player, amount, Type.STATS, StatsColumns.kills.getName());
    }

    public static int getDeath(Player player) {
        int deaths = 0;
        try (ResultSet query = getInfo(player, Type.STATS)) {
            while (query.next()) {
                deaths = query.getInt("deaths");
                query.close();
            }
        } catch (SQLException ex) {
            database.error(ex);
        }
        return deaths;
    }

    public static boolean addDeath(Player player) {
        return addKills(player, 1);
    }

    public static boolean addDeaths(Player player, int count) {
        return add(player, count, Type.STATS, StatsColumns.deaths.getName());
    }
    
    public static boolean setDeaths(Player player, int amount) {
        return set(player, amount, Type.STATS, StatsColumns.deaths.getName());
    }
    
    public static boolean addGame(Game game) {
        if(game.getStartTime()==null) return false;
        int i = 0;
        try {
            PreparedStatement stat = database.getConnection().prepareStatement("INSERT INTO " + Database.PREFIX + "_" + Type.GAME.getSuffix() + " (game_id, start) VALUES(?,?)");
            stat.setString(1, game.getGameId());
            stat.setString(2, new Date(game.getStartTime().getTimeInMillis()).toString());
            i = database.update(stat);
            stat.close();
        } catch (SQLException ex) {
            LoggerUtil.error("Can't add game: " + game.getGameId() + ". Error:\n" + ex.getMessage());
        }
        return i>0;
    }
    
    public static boolean updateGame(Game game, Calendar end) {
        if(game.getStartTime()==null || end==null) return false;
        int i = 0;
        try {
            PreparedStatement stat = database.getConnection().prepareStatement("UPDATE " + Database.PREFIX + "_" + Type.GAME.getSuffix() + " SET end = ? WHERE game_id = ? AND start = ?");
            stat.setString(1, new Date(end.getTimeInMillis()).toString());
            stat.setString(2, game.getGameId());
            stat.setString(3, new Date(game.getStartTime().getTimeInMillis()).toString());
            i = database.update(stat);
            stat.close();
        } catch (SQLException ex) {
            LoggerUtil.error("Can't update game: " + game.getGameId() + ". Error:\n" + ex.getMessage());
        }
        return i>0;
    }

    public static boolean clearAll(Player player) {
        boolean r = true;
        for (Type type : Type.values()) {
            if(type.equals(Type.GAME)) continue;
            if (!clear(player, type)) { //if, at least, one not edited - return false
                r = false;
            }
        }
        return r;
    }
    
    public static boolean deleteGame(Game game) {
        int i = 0;
        try {
            PreparedStatement stat = database.getConnection().prepareStatement(Type.GAME.getClearQuery());
            stat.setString(1, game.getGameId());
            stat.setString(2, new Date(game.getStartTime().getTimeInMillis()).toString());
            i = database.update(stat);
            stat.close();
        } catch (SQLException ex) {
            LoggerUtil.error("Can't delete game: " + game.getGameId() + ". Error:\n" + ex.getMessage());
        }
        return i>0;
    }

    public static boolean clear(Player player, Type type) {
        int i = 0;
        try {
            PreparedStatement stat = database.getConnection().prepareStatement(type.getClearQuery());
            stat.setString(1, player.getUniqueId().toString());
            i = database.update(stat);
            stat.close();
        } catch (SQLException ex) {
            LoggerUtil.error("Can't remove player info: " + player.getName() + ". Error:\n" + ex.getMessage());
        }
        return i > 0;
    }
    
    private static boolean set(Player player, int count, Type type, String column) {
        if(count < 0) return false; //don't allow negative values
        int i = 0;
        try {
            PreparedStatement stat = database.getConnection().prepareStatement("UPDATE " + Database.PREFIX + "_" + type.getSuffix() + " SET " + column + " = ? WHERE UUID=?");
            stat.setInt(1, count);
            stat.setString(2, player.getUniqueId().toString());
            i = database.update(stat);
            stat.close();
        } catch (SQLException ex) {
            LoggerUtil.error("Can't set " + column + " to : " + player.getName() + ". Error:\n" + ex.getMessage());
        }
        return i > 0;
    }
    
    private static boolean add(Player player, int count, Type type, String column) {
        if (count == 0) {
            return false;
        }
        if (count < 0) {
            return remove(player, -count, type, column);
        }
        int i = 0;
        try {
            PreparedStatement stat = database.getConnection().prepareStatement("UPDATE " + Database.PREFIX + "_" + type.getSuffix() + " SET " + column + " = " + column + "+ ? WHERE UUID=?");
            stat.setInt(1, count);
            stat.setString(2, player.getUniqueId().toString());
            i = database.update(stat);
            stat.close();
        } catch (SQLException ex) {
            LoggerUtil.error("Can't add" + column + " to : " + player.getName() + ". Error:\n" + ex.getMessage());
        }
        return i > 0;
    }

    private static boolean remove(Player player, int count, Type type, String column) {
        int i = 0;
        try {
            String max = database.isMySQL() ? "GREATEST" : "MAX";
            PreparedStatement stat = database.getConnection().prepareStatement("UPDATE " + Database.PREFIX + "_" + type.getSuffix() + " SET " + column + " = " + max + "(" + column + "- ?,0) WHERE UUID=?");//prevent negative arguments
            stat.setInt(1, count);
            stat.setString(2, player.getUniqueId().toString());
            i = database.update(stat);
            stat.close();
        } catch (SQLException ex) {
            LoggerUtil.error("Can't remove" + column + "to : " + player.getName() + ". Error:\n" + ex.getMessage());
        }
        return i > 0;
    }

    public static ResultSet getInfo(Player player, Type type) {
        try {
            statement = database.getConnection().prepareStatement(type.getSelectQuery());
            statement.setString(1, player.getUniqueId().toString());
            ResultSet query = database.query(statement);
            statement.closeOnCompletion();
            return query;
        } catch (SQLException ex) {
            database.error(ex);
        }
        return null;
    }

    public enum StatsColumns {
        UUID, games, wins, kills, deaths;

        public String getName() {
            return this.toString();
        }
    }

    public enum GamesColumns {
        id, game_id, start, end;

        public String getName() {
            return this.toString();
        }
    }

    public enum PlayersColumns {
        UUID, name, coins, points;

        public String getName() {
            return this.toString();
        }
    }

    public enum Type {
        PLAYER, GAME, STATS;

        public String getSuffix() {
            switch (this) {
                case GAME:
                    return database.gamesSuffix;
                case PLAYER:
                    return database.playersSuffix;
                case STATS:
                    return database.statsSuffix;
                default:
                    return "";
            }
        }

        protected String getSelectQuery() {
            switch (this) {
                case GAME:
                    return selectGames;
                case PLAYER:
                    return selectPlayers;
                case STATS:
                    return selectStats;
                default:
                    return "";
            }
        }
        
        protected String getClearQuery() {
            switch (this) {
                case GAME:
                    return "DELETE FROM " + Database.PREFIX + "_" + this.getSuffix() + " WHERE game_id=? AND start=?";
                case PLAYER:
                    return "UPDATE "+Database.PREFIX +"_"+this.getSuffix() + " SET coins=0, points=0 WHERE UUID=?";
                case STATS:
                    return "UPDATE "+Database.PREFIX +"_"+this.getSuffix() + " SET games=0, wins=0, kills=0, deaths=0 WHERE UUID=?";
                default:
                    return "";
            }
        }
    }
}
