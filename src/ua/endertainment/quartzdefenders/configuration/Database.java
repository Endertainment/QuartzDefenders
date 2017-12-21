package ua.endertainment.quartzdefenders.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class Database {

    QuartzDefenders plugin;
    Connection connection;
    boolean mysql;

    private String hostname, database, username, password, table;
    int port;

    public Database(QuartzDefenders plugin) {
        this.plugin = plugin;
        mysql = plugin.getConfig().getBoolean("database.use_mysql", false);
        if (mysql) {
            this.hostname = plugin.getConfig().getString("database.host");
            this.port = plugin.getConfig().getInt("database.port");
            this.database = plugin.getConfig().getString("database.database");
            this.username = plugin.getConfig().getString("database.username");
            this.password = plugin.getConfig().getString("database.password");
            this.table = plugin.getConfig().getString("database.table");
        }
    }

    private boolean init() {
        try {
            Class<?> useless = mysql ? Class.forName("com.mysql.jdbc.Driver") : Class.forName("org.sqlite.JDBC");
            return true;
        } catch (ClassNotFoundException e) {
            LoggerUtil.logError("Class not found in initialize(): " + e + ". What server core are you using?");
            return false;
        }
    }

    public boolean open() {
        return mysql ? openMySQL() : openSQLLite();
    }

    private boolean openSQLLite() {
        if (init()) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:plugins/QuartzDefenders/database.db");
                return true;
            } catch (SQLException e) {
                LoggerUtil.logError("Could not establish an SQLite connection, SQLException: " + e.getMessage());
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
                LoggerUtil.logError("Could not establish a MySQL connection, SQLException: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

}
