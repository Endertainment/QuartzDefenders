package ua.endertainment.quartzdefenders.configuration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import ua.endertainment.quartzdefenders.QuartzDefenders;

public class Config {

    private QuartzDefenders plugin;

    private File langFile;
    private FileConfiguration lang;

    private File gamesFile;
    private File statsFile;
    private File kitsFile;
    private File shopFile;
    private File mobsFile;

    private FileConfiguration games;
    private FileConfiguration stats;
    private FileConfiguration kits;
    private FileConfiguration shop;
    private FileConfiguration mobs;

    public Config(QuartzDefenders plugin) {
        this.plugin = plugin;

        setupGames();
        setupStats();
        setupKits();
        setupShop();
        setupMobs();

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
                InputStream input = plugin.getResource("messages_en.yml");
                if (input == null) {
                    plugin.getLogger().warning("Failed to load language file!!!");
                }
                try {
                    FileUtils.copyInputStreamToFile(input, langFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                lang = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("messages_en.yml")));
                return;
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
            plugin.saveResource("shop.yml", false);
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

    /*
     * ACHIEVEMENTS
     */
    private void setupMobs() {
        mobsFile = new File(plugin.getDataFolder() + File.separator, "mobs.yml");
        if (!mobsFile.exists()) {
            plugin.saveResource("mobs.yml", false);
        }
        mobs = YamlConfiguration.loadConfiguration(mobsFile);
    }

    public FileConfiguration getMobsInfo() {
        return mobs;
    }

    public void saveMobsInfo() {
        try {
            mobs.save(mobsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mobs = YamlConfiguration.loadConfiguration(mobsFile);
    }

}
