package ua.endertainment.quartzdefenders.stats;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimaps;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class TopManager {

    private final QuartzDefenders plugin;

    private final List<OfflinePlayer> topKills = new ArrayList<>();
    private final List<OfflinePlayer> topWins = new ArrayList<>();

    public TopManager(QuartzDefenders plugin) {
        this.plugin = plugin;

        calcKills();
        calcWins();
        setupSigns();
    }

    /*
	 * KILLS
     */
    private void calcKills() {
        ListMultimap<Integer, OfflinePlayer> killsMap = Multimaps.newListMultimap(new TreeMap<>(Collections.reverseOrder()), Lists::newArrayList);

        for (String s : plugin.getConfigs().getStatsInfo().getKeys(false)) {
            killsMap.put(plugin.getConfigs().getStatsInfo().getInt(s + ".kills"), Bukkit.getOfflinePlayer(UUID.fromString(s)));
        }
        topKills.addAll(killsMap.values());
    }

    public OfflinePlayer getPlayerByKillPosition(int position) {
        if (position < topKills.size()) {
            return topKills.get(position);
        }
        return null;
    }

    public int getPlayerKillsPosition(Player p) {
        for (OfflinePlayer entry : topKills) {
            if (entry.getUniqueId().equals(p.getUniqueId())) {
                return topKills.indexOf(entry);
            }
        }
        return topKills.size() /*+ 1*/;
    }

    public void refreshKillsTop() {
        calcKills();
    }
    
    public void refreshWinsTop() {
        calcWins();
    }

    /*
	 * WINS
     */
    private void calcWins() {
        ListMultimap<Integer, OfflinePlayer> winsMap = Multimaps.newListMultimap(new TreeMap<>(Collections.reverseOrder()), Lists::newArrayList);
        
        for (String s : plugin.getConfigs().getStatsInfo().getKeys(false)) {
            winsMap.put(plugin.getConfigs().getStatsInfo().getInt(s + ".wins"), Bukkit.getOfflinePlayer(UUID.fromString(s)));
        }
        topWins.addAll(winsMap.values());
    }

    public OfflinePlayer getPlayerByWinPosition(int position) {
        if (position < topWins.size()) {
            return topWins.get(position);
        }
        return null;
    }

    public int getPlayerWinPosition(Player p) {
        for (OfflinePlayer entry : topWins) {
            if (entry.getUniqueId().equals(p.getUniqueId())) {
                return topWins.indexOf(entry);
            }
        }
        return topWins.size() /*+ 1*/;
    }

    public void refresh() {
        calcWins();
        calcKills();
    }

    /*
	 * SIGNS MANAGER
     */
    public void setupSigns() {
        if (plugin.getConfig().isList("Signs.top_kills")) {
            setKillsText(refreshSigns("Signs.top_kills"));
        }
        if (plugin.getConfig().isList("Signs.top_wins")) {
            setWinsText(refreshSigns("Signs.top_wins"));
        }
    }

    @SuppressWarnings({ "unchecked", "deprecation" })
	public static List<List<Integer>> getIntegerListOfLists(List<?> from) {
        List<List<Integer>> list = new ArrayList<>();
        if (from == null) {
            return list;
        }
        for (Object obj : from) {
            if (obj instanceof List) {
                try {
                    List<Integer> lst = (List<Integer>) obj;
                    list.add(lst);
                } catch (ClassCastException ex) {
                    LoggerUtil.logError("Can't read integer list from config: " + obj);
                }
            }
        }
        return list;
    }

    private List<Sign> refreshSigns(String section) {
        List<Sign> signs = new ArrayList<>();
        for (List<Integer> list : getIntegerListOfLists(plugin.getConfig().getList(section))) {
            int x, y, z;
            x = list.get(0);
            y = list.get(1);
            z = list.get(2);
            Location l = new Location(plugin.getLobby().getWorld(), x, y, z);
            if (!(plugin.getLobby().getWorld().getBlockAt(l).getState() instanceof Sign)) {
                continue;
            }
            Sign sign = (Sign) plugin.getLobby().getWorld().getBlockAt(l).getState();
            clearSign(sign);
            signs.add(sign);
        }
        return signs;
    }

    private void clearSign(Sign sign) {
        for (int i = 0; i < 4; i++) {
            sign.setLine(i, "");
        }
    }

    private void setKillsText(List<Sign> signs) {
        int index = 0;
        for (Sign s : signs) {
            int x = index + 1;
            if (getPlayerByKillPosition(index) == null) {
                clearSign(s);
                return;
            }
            String kills = plugin.getConfigs().getStatsInfo().getString(getPlayerByKillPosition(index).getUniqueId().toString() + ".kills");
            if (kills == null) {
                clearSign(s);
                return;
            }
            s.setLine(0, ChatColor.BLUE + "Top " + x);
            s.setLine(1, ChatColor.GRAY + getPlayerByKillPosition(index).getName());
            s.setLine(2, ChatColor.GOLD + "Kills: " + kills);
            s.update();
            index++;
        }
    }

    private void setWinsText(List<Sign> signs) {
        int index = 0;
        for (Sign s : signs) {
            int x = index + 1;
            if (getPlayerByWinPosition(index) == null) {
                clearSign(s);
                return;
            }
            s.setLine(0, ChatColor.BLUE + "Top " + x);
            s.setLine(1, ChatColor.GRAY + getPlayerByWinPosition(index).getName());
            s.setLine(2, ChatColor.GOLD + "Wins: " + plugin.getConfigs().getStatsInfo().getString(getPlayerByWinPosition(index).getUniqueId().toString() + ".wins"));
            s.update();
            index++;
        }
    }
}
