package ua.Endertainment.QuartzDefenders.Utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class FilesUtil {

    private QuartzDefenders plugin;

    private File langFile;
    private FileConfiguration lang;

    private File gamesFile;
    private File statsFile;
    private File kitsFile;
    private File shopFile;

    private FileConfiguration games;
    private FileConfiguration stats;
    private FileConfiguration kits;
    private FileConfiguration shop;

    public FilesUtil(QuartzDefenders plugin) {
        this.plugin = plugin;

        setupGames();
        setupStats();
        setupKits();
        setupShop();

        setupLang();
    }

    /*
	 *  LANGUAGE
     */
    private void setupLang() {
        File langFolder = new File(plugin.getDataFolder() + File.separator + "language" + File.separator);
        langFile = new File(langFolder, "messages_" + plugin.getConfig().getString("language", "en") + ".yml");
        if (!langFile.exists()) {
            langFile = new File(langFolder, "messages_en.yml");
            if (!langFile.exists()) {
                plugin.saveResource("language" + File.separator + "messages_en.yml", false);
            }
        }
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    public FileConfiguration getLang() {
        return lang;
    }

    public void reloadLang() {
        lang = YamlConfiguration.loadConfiguration(langFile);
    }

    /*
	 * GAMES
     */
    private void setupGames() {
        gamesFile = new File(plugin.getDataFolder() + File.separator, "games.yml");
        if (!gamesFile.exists()) {
            try {
                gamesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        games = YamlConfiguration.loadConfiguration(gamesFile);
        if (!games.isConfigurationSection("Games")) {
            games.createSection("Games");
            saveGameInfo();
        }
    }

    public FileConfiguration getGameInfo() {
        return games;
    }

    public void saveGameInfo() {
        try {
            games.save(gamesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        games = YamlConfiguration.loadConfiguration(gamesFile);
    }

    /*
	 * STATS
     */
    private void setupStats() {
        statsFile = new File(plugin.getDataFolder() + File.separator, "stats.yml");
        if (!statsFile.exists()) {
            try {
                statsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stats = YamlConfiguration.loadConfiguration(statsFile);
    }

    public FileConfiguration getStatsInfo() {
        return stats;
    }

    public void saveStatsInfo() {
        try {
            stats.save(statsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        stats = YamlConfiguration.loadConfiguration(statsFile);
    }

    /*
	 * KITS
     */
    private void setupKits() {
        kitsFile = new File(plugin.getDataFolder() + File.separator, "kits.yml");
        if (!kitsFile.exists()) {
            try {
                kitsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        kits = YamlConfiguration.loadConfiguration(kitsFile);
    }

    public FileConfiguration getKitsInfo() {
        return kits;
    }

    public void saveKitsInfo() {
        try {
            kits.save(kitsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        kits = YamlConfiguration.loadConfiguration(kitsFile);
    }

    private void setupShop() {
        shopFile = new File(plugin.getDataFolder() + File.separator, "shop.yml");
        if (!shopFile.exists()) {
            try {
                shopFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        shop = YamlConfiguration.loadConfiguration(shopFile);
    }

    public FileConfiguration getShopInfo() {
        return shop;
    }

    public void saveShopInfo() {
        try {
            shop.save(shopFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        shop = YamlConfiguration.loadConfiguration(shopFile);
    }

}
