package ua.endertainment.quartzdefenders.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class Database {

    private QuartzDefenders plugin;
    private Connection connection;
    private boolean mysql;

    private String hostname, database, username, password, prefix;
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
            this.prefix = plugin.getConfig().getString("database.table_prefix");
        }
        if (!isTable("games")) {
            createTable("games");
        }
        if (!isTable("players")) {
            createTable("players");
        }
        if (!isTable("stats")) {
            createTable("stats");
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

    public boolean open() {
        return mysql ? openMySQL() : openSQLLite();
    }

    private boolean createTable(String suffix) {
        String query;
        try {
            switch(suffix) {
                case "games":
                    query = "CREATE TABLE IF NOT EXISTS ?_?(id MEDIUMINT NOT NULL AUTO_INCREMENT, game_id VARCHAR(255) NOT NULL, PRIMARY KEY(id))";
                    break;
                case "players":
                    query = "CREATE TABLE IF NOT EXISTS ?_?(UUID varchar(36) NOT NULL, name VARCHAR(16) NOT NULL, PRIMARY KEY(UUID))";
                    break;
                case "stats":
                    query = "CREATE TABLE IF NOT EXISTS ?_?(UUID varchar(36) NOT NULL, PRIMARY KEY(UUID))";
                    break;
                default:
                    return false;
            }
            PreparedStatement stat = connection.prepareStatement(query);
            stat.setString(1, prefix);
            stat.setString(2, suffix);
            query(stat);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean isTable(String suffix) {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            LoggerUtil.error("Could not create a statement in checkTable(), SQLException: " + e.getMessage());
            return false;
        }
        try {
            statement.executeQuery("SELECT 1 FROM " + prefix + suffix); //async?
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private boolean openSQLLite() {
        if (init()) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:plugins/QuartzDefenders/database.db");
                return true;
            } catch (SQLException e) {
                LoggerUtil.error("Could not establish an SQLite connection, SQLException: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    private boolean openMySQL() {
        if (init()) {
            try {
                String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database;
                if (init()) {
                    this.connection = DriverManager.getConnection(url, username, password);
                    return true;
                }
                return false;
            } catch (SQLException e) {
                LoggerUtil.error("Could not establish a MySQL connection, SQLException: " + e.getMessage());
                return false;
            }
        }
        return false;
    }
    
     public static ResultSet query(final PreparedStatement statement) {
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
                    return statement.executeQuery();
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
