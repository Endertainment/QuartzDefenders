package ua.endertainment.quartzdefenders.configuration;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.bukkit.entity.Player;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class Database {

    private QuartzDefenders plugin;
    private Connection connection;
    private boolean mysql;

    private final String playersSuffix = "players", gamesSuffix = "games", statsSuffix = "stats";

    public static String prefix;

    private String hostname, database, username, password;
    private int port;

    public Database(QuartzDefenders plugin) {
        this.plugin = plugin;
        mysql = plugin.getConfig().getBoolean("database.use_mysql", false);
        if (mysql) {
            this.hostname = plugin.getConfig().getString("database.host", "localhost");
            this.port = plugin.getConfig().getInt("database.port", 3306);
            this.database = plugin.getConfig().getString("database.database");
            this.username = plugin.getConfig().getString("database.username", "");
            this.password = plugin.getConfig().getString("database.password", "");
        }
        this.prefix = plugin.getConfig().getString("database.table_prefix", "quartzdefenders").replace("'", "").replace("\"", "");

        open();

        if (!isTable(gamesSuffix)) {
            createTable(gamesSuffix);
        }
        if (!isTable(playersSuffix)) {
            createTable(playersSuffix);
        }
        if (!isTable(statsSuffix)) {
            createTable(statsSuffix);
        }
    }

    private boolean init() {
        try {
            Class<?> useless = mysql ? Class.forName("com.mysql.jdbc.Driver") : Class.forName("org.sqlite.JDBC");
            return true;
        } catch (ClassNotFoundException e) {
            LoggerUtil.error("Class not found in initialize(): " + e + ". What server core are you using?");
            return false;
        }
    }

    public final boolean open() {
        return mysql ? openMySQL() : openSQLLite();
    }

    private boolean createTable(String suffix) {
        if (connection == null) {
            LoggerUtil.error("Can't connect to database!");
            return false;
        }
        String query;
        try {
            switch (suffix) {
                case gamesSuffix:
                    String inc = mysql ? "AUTO_INCREMENT" : "";//sql injection? How to fix?
                    query = "CREATE TABLE IF NOT EXISTS ?_?(id MEDIUMINT NOT NULL " + inc + ", game_id VARCHAR(255), PRIMARY KEY(id))";
                    break;
                case playersSuffix:
                    query = "CREATE TABLE IF NOT EXISTS ?_?(UUID varchar(36) NOT NULL, name VARCHAR(16), coins INTEGER DEFAULT 0, points INTEGER DEFAULT 0, PRIMARY KEY(UUID))";
                    break;
                case statsSuffix:
                    query = "CREATE TABLE IF NOT EXISTS ?_?(UUID varchar(36) NOT NULL, games INTEGER DEFAULT 0, wins INTEGER DEFAULT 0, kills INTEGER DEFAULT 0, deaths INTEGER DEFAULT 0, PRIMARY KEY(UUID))";
                    break;
                default:
                    return false;
            }
            PreparedStatement stat = connection.prepareStatement(query.replace("?_?", prefix + "_" + suffix));
            if (update(stat) > 0) {
                LoggerUtil.info("Created table: " + suffix);
                stat.close();
                return true;
            }
            stat.close();
        } catch (SQLException ex) {
            LoggerUtil.error("Can't create table: " + prefix + "_" + suffix + ". Error: " + ex.getMessage());
            return false;
        }
        return false;
    }

    private boolean isTable(String suffix) {
        try {
            PreparedStatement stat = connection.prepareStatement("SELECT 1 FROM ?_? LIMIT 1");
            stat.setString(1, prefix);
            stat.setString(2, suffix);
            stat.executeQuery(); //async?
            stat.close();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean openSQLLite() {
        if (init()) {
            try {
                File file = new File(plugin.getDataFolder(), "database.db");
                connection = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
                if (connection != null) {
                    return true;
                }
            } catch (Exception e) {
                LoggerUtil.error("Could not establish an SQLite connection, SQLException: " + e.getMessage());
            }
        }
        return false;
    }

    private boolean openMySQL() {
        if (init()) {
            try {
                String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=utf-8&connectTimeout=10000";
                if (init()) {
                    this.connection = DriverManager.getConnection(url, username, password);
                    try (Statement statement = connection.createStatement()) {
                        statement.executeUpdate("SET NAMES 'utf8'");
                        return true;
                    }
                }
            } catch (Exception e) {
                LoggerUtil.error("Could not establish a MySQL connection, SQLException: " + e.getMessage());
            }
        }
        return false;
    }

    public int update(final PreparedStatement statement) {
        try {
            if (statement.getConnection().isClosed()) {
                return 0;
            }
        } catch (SQLException ex) {
            LoggerUtil.error("Connection not valid!");
        }
        try {
            ExecutorService exe = Executors.newCachedThreadPool();

            Future<Integer> future = exe.submit(() -> {
                try {
                    return statement.executeUpdate();
                } catch (SQLException e) {
                    error(e);
                }
                return null;
            });

            if (future.get() != null) {
                statement.clearParameters();
                return future.get();
            }
        } catch (Exception e) {
            LoggerUtil.error("Error in query: " + e.getMessage());
        }
        return 0;
    }

    public ResultSet query(final PreparedStatement statement) {
        ResultSet resultSet = null;
        try {
            if (statement.getConnection().isClosed()) {
                return null;
            }
        } catch (SQLException ex) {
            LoggerUtil.error("Connection not valid!");
        }
        try {
            ExecutorService exe = Executors.newCachedThreadPool();

            Future<ResultSet> future = exe.submit(() -> {
                try {
                    ResultSet query = statement.executeQuery();
                    statement.closeOnCompletion();
                    return query;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return null;
            });

            if (future.get() != null) {
                resultSet = future.get();
            }
        } catch (Exception e) {
            LoggerUtil.error("Error in query: " + e.getMessage());
        }

        return resultSet;
    }

    public boolean insertPlayer(Player player) {
        if (isOpen()) {
            try {
                PreparedStatement preparedStmt = mysql?
                        connection.prepareStatement("INSERT INTO " + prefix + "_players (UUID, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE UUID=UUID;"):
                        connection.prepareStatement("INSERT OR REPLACE INTO " + prefix + "_players (UUID, name) VALUES (?, ?)");
                preparedStmt.setString(1, player.getUniqueId().toString());
                preparedStmt.setString(2, player.getName());
                return update(preparedStmt) > 0;
            } catch (SQLException ex) {
                LoggerUtil.error("Can't add player " + player.getName() + " to database! " + ex.getMessage());
                return false;
            }
        }
        LoggerUtil.error("Can't connect to database!");
        return false;
    }

    public String getUserName(String uuid) {
        if (isOpen()) {
            String user = "";
            try {
                PreparedStatement st = getConnection().prepareStatement("SELECT UUID, name FROM " + prefix + "_players WHERE UUID='" + uuid + "' LIMIT 1");
                st.setString(1, uuid);
                ResultSet rs = query(st);
                while (rs.next()) {
                    user = rs.getString("user");
                }
            } catch (SQLException ex) {
                error(ex);
            }
            return user;
        }
        LoggerUtil.error("Can't connect to database!");
        return "";
    }

    public void error(Exception ex) {
        LoggerUtil.error("Database error! " + ex.getMessage());
    }

    public final boolean isOpen() {
        return isOpen(1);
    }

    public final boolean isOpen(int timeout) {
        if (connection != null) {
            try {
                if (connection.isValid(timeout)) {
                    return true;
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }

    public final Connection getConnection() {
        return connection;
    }

    public final boolean close() {
        if (connection != null) {
            try {
                connection.close();
                return true;
            } catch (SQLException e) {
                LoggerUtil.error("Could not close connection, SQLException: " + e.getMessage());
                return false;
            }
        } else {
            LoggerUtil.error("Could not close connection, it is null.");
            return false;
        }
    }

}
