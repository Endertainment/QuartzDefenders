package ua.endertainment.quartzdefenders.kits;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.stats.StatsPlayer;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.Replacer;

public class KitsManager {

    QuartzDefenders plugin;
    FileConfiguration config;
    String kitsName = Language.getString("kits.kits");

    private static HashMap<Kit, JavaPlugin> kits = new HashMap<>();

    public KitsManager(QuartzDefenders plugin) {
        this.plugin = plugin;
        config = plugin.getConfigs().getKitsInfo();
    }

    public void registerKit(Kit kit, JavaPlugin plugin) {
        kits.put(kit, plugin);
    }

    public void unregisterKit(Kit kit) {
        kits.remove(kit);
    }

    public void unregisterAll(JavaPlugin plugin) {
        kits.entrySet().removeIf(e -> e.getValue().equals(plugin));
    }

    public HashMap<Kit, JavaPlugin> getKitsRegistry() {
        return kits;
    }

    public Kit getKit(String string) {
        for (Kit kit : kits.keySet()) {
            if (kit.getName().equals(string)) {
                return kit;
            }
        }
        return null;
    }

    public boolean isBought(Kit kit, Player player) {
        String kitName = (getPlugin(kit).getName() + ":" + kit.getName()).toLowerCase();
        List<String> list = config.getStringList(player.getUniqueId().toString());
        return list.contains(kitName);
    }

    public Set<Kit> getPlayerKits(Player player) {
        Set<Kit> kits = new HashSet<>();
        List<String> list = config.getStringList(player.getUniqueId().toString());
        for (String kit : list) {
            String n = kit.split(":")[1];
            Kit k = getKit(n);
            if (k != null) {
                kits.add(k);
            }
        }
        return kits;
    }

    public boolean isObtainable(Kit kit, Player player) {
        if (getPlayerKits(player).contains(kit)) {
            return false;
        }
        StatsPlayer pl = new StatsPlayer(player);
        
        boolean b = true;
        
        cycle:
        for(KitUnlockType t : kit.getUnlockTypes()) {
        	switch (t) {
				case ACHIEVEMENT:
									if(!b) break cycle;
									b = false;
									break;
				case FREE:
									b = true;
									break cycle;
				case GIFT:
									b = false;
									break cycle;
				case LEVEL:
									if(!b) break cycle;
									b = pl.getLevel() >= kit.getLevel();
									break;
				case PERMISSION:
									if(!b) break cycle;	
									b = player.hasPermission(kit.getPermission());
									break;
				case PRICE:
									if(!b) break cycle;
									b = pl.getCoins() >= kit.getPrice();
									break;
				default:
									b = false;
									break;        	
	        }
        }
        
//        if (pl.getLevel() >= kit.getLevel()) {
//            if (pl.getCoins() >= kit.getPrice()) {
//                return true;
//            }
//        }
        return b;
    }

    public void removeKit(Kit kit, Player player) {
        String kitName = (getPlugin(kit).getName() + ":" + kit.getName()).toLowerCase();//plugin_name:kit_name
        List<String> list = config.getStringList(player.getUniqueId().toString());
        list.remove(kitName);
        config.set(player.getUniqueId().toString(), list);
        plugin.getConfigs().saveKitsInfo();
    }

    public void giveKit(Kit kit, Player player) {
        writeKit(player, kit);
    }

    public void buyKit(Kit kit, Player player) {
        StatsPlayer pl = new StatsPlayer(player);
        
        boolean b = true;
        String m = "";
        
        cycle:
        for(KitUnlockType t : kit.getUnlockTypes()) {
        	switch (t) {
				case ACHIEVEMENT:
									if(!b) break cycle;
									b = false;
									if(!b) m = Language.getString("kits.not_achievement", new Replacer("{0}", kit.getAchievement()));
									break;
				case FREE:
									b = true;
									break cycle;
				case GIFT:
									b = false;
									m = Language.getString("kits.gift");
									break cycle;
				case LEVEL:
									if(!b) break cycle;
									b = pl.getLevel() >= kit.getLevel();
									if(!b) m = Language.getString("kits.low_level", new Replacer("{0}", kit.getLevel()));
									break;
				case PERMISSION:
									if(!b) break cycle;	
									b = player.hasPermission(kit.getPermission());
									if(!b) m = Language.getString("kits.not_permission");
									break;
				case PRICE:
									if(!b) break cycle;
									b = pl.getCoins() >= kit.getPrice();
									if(b) pl.removeCoins(kit.getPrice());
									if(!b) m = Language.getString("kits.not_enough_coins", new Replacer("{0}", kit.getPrice()));
									break;
				default:
									b = false;
									break;        	
	        }
        }
        
        if (b) {
			writeKit(player, kit);
			player.sendMessage(LoggerUtil.gameMessage(kitsName, m));
		} else {
			player.sendMessage(LoggerUtil.gameMessage(kitsName, 
					Language.getString("kits.kit_buy_success", new Replacer("{0}", kit.getDisplayName()))));
		}
        
//        if (pl.getLevel() >= kit.getLevel()) {
//            if (pl.getCoins() >= kit.getPrice()) {
//                pl.removeCoins(kit.getPrice());
//                writeKit(player, kit);
//                LoggerUtil.gameMessage(kitsName, Language.getString("kits.kit_buy_success", new Replacer("{0}", kit.getDisplayName())));
//            }
//            LoggerUtil.gameMessage(kitsName, Language.getString("kits.not_enough_coins", new Replacer("{0}", kit.getPrice())));
//        }
//        LoggerUtil.gameMessage(kitsName, Language.getString("kits.low_level", new Replacer("{0}", kit.getLevel())));
    }

    private JavaPlugin getPlugin(Kit kit) {
        for (Entry<Kit, JavaPlugin> entry : kits.entrySet()) {
            if (entry.getKey().getName().equals(kit.getName())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void writeKit(Player player, Kit kit) {
        String kitName = (getPlugin(kit).getName() + ":" + kit.getName()).toLowerCase();//plugin_name:kit_name
        List<String> list = config.getStringList(player.getUniqueId().toString());
        if (!list.contains(kitName)) {
            list.add(kitName);
        }
        config.set(player.getUniqueId().toString(), list);
        plugin.getConfigs().saveKitsInfo();
    }

    public void chooseKit(Kit kit, Game game, GamePlayer gamePlayer) {
        if (isBought(kit, gamePlayer.getPlayer())) {
            game.setKit(gamePlayer, kit);
            gamePlayer.getPlayer().sendMessage(LoggerUtil.gameMessage(kitsName, 
            		Language.getString("kit_choose_success", new Replacer("{0}", kit.getDisplayName()))));
        } else {
            gamePlayer.getPlayer().sendMessage(LoggerUtil.gameMessage(kitsName, 
            		Language.getString("kit_choose_failed", new Replacer("{0}", kit.getDisplayName()))));
        }
    }

    public String getAvailability(Kit kit, Player player) {
        boolean b = isObtainable(kit, player);
        return b? Language.getString("kits.kit_available") : Language.getString("kits.kit_unavailable");
    }
    
    public String getAccess(Kit kit, Player player) {
        boolean b = isBought(kit, player);
        return b? Language.getString("kits.kit_available") : Language.getString("kits.kit_unavailable");
    }

}
