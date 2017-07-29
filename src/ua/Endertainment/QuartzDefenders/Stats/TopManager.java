package ua.Endertainment.QuartzDefenders.Stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class TopManager {

	private QuartzDefenders plugin;
	
	private HashMap<Integer, OfflinePlayer> topKills = new HashMap<>();
	private HashMap<Integer, OfflinePlayer> topWins = new HashMap<>();
	
	public TopManager(QuartzDefenders plugin) {
		this.plugin = plugin;
		
		calcKills();
		calcWins();
	}
	
	/*
	 * KILLS
	 */	
	private void calcKills() {
		HashMap<Integer, OfflinePlayer> killsMap = new HashMap<>();
		
		for(String s : plugin.getConfigs().getStatsInfo().getKeys(false)) {
			killsMap.put(plugin.getConfigs().getStatsInfo().getInt(s + ".kills"), Bukkit.getOfflinePlayer(UUID.fromString(s)));
		}
	
		ArrayList<Integer> kills = new ArrayList<>();
		
		for(int i : killsMap.keySet()) {
			kills.add(i);
		}
		
		Collections.sort(kills, Collections.reverseOrder());
		
		for(int i = 0; i < kills.size(); i++) {
			topKills.put(i+1, killsMap.get(kills.get(i)));
		}		
	}	
	
	public OfflinePlayer getPlayerByPositionK(int position) {
		return topKills.get(position);
	}
	public int getPlayerPositionK(Player p) {
		for(Entry<Integer, OfflinePlayer> entry : topKills.entrySet()) {
			if(entry.getValue().getUniqueId().equals(p.getUniqueId())) {
				return entry.getKey();
			}
		}
		return topKills.keySet().size()+1;
	}
	public void refreshKillsTop() {
		calcKills();
	}
		
	/*
	 * WINS
	 */
	private void calcWins() {
		HashMap<Integer, OfflinePlayer> winsMap = new HashMap<>();
		
		for(String s : plugin.getConfigs().getStatsInfo().getKeys(false)) {
			winsMap.put(plugin.getConfigs().getStatsInfo().getInt(s + ".wins"), Bukkit.getOfflinePlayer(UUID.fromString(s)));
		}
	
		ArrayList<Integer> wins = new ArrayList<>();
		
		for(int i : winsMap.keySet()) {
			wins.add(i);
		}
		
		Collections.sort(wins, Collections.reverseOrder());
		
		for(int i = 0; i < wins.size(); i++) {
			topWins.put(i+1, winsMap.get(wins.get(i)));
		}		
	}	
	
	public OfflinePlayer getPlayerByPositionW(int position) {
		return topWins.get(position);
	}
	public int getPlayerPositionW(Player p) {
		for(Entry<Integer, OfflinePlayer> entry : topWins.entrySet()) {
			if(entry.getValue().getUniqueId().equals(p.getUniqueId())) {
				return entry.getKey();
			}
		}
		return topWins.keySet().size()+1;
	}
	public void refreshWinsTop() {
		calcWins();
	}
	
	/*
	 * SIGNS MANAGER
	 */
	public void setupSigns() {	
		if(plugin.getConfig().isList("Signs.top_kills")) {
			
			Set<Sign> signs = new HashSet<>();
			
			for(String loc : plugin.getConfig().getStringList("Signs.top_kills")) {
				int x, y, z;
                String[] array = loc.split(",");
                x = Integer.parseInt(array[0]);
                y = Integer.parseInt(array[1]);
                z = Integer.parseInt(array[2]);
                Location l = new Location(plugin.getLobby().getWorld(), x, y, z);
                if(!(plugin.getLobby().getWorld().getBlockAt(l).getState() instanceof Sign)) {
                	continue;
                }
                Sign sign = (Sign) plugin.getLobby().getWorld().getBlockAt(l).getState();
                signs.add(sign);
			}
			setTextK(signs);			
		}
		if(plugin.getConfig().isList("Signs.top_wins")) {
			
			Set<Sign> signs = new HashSet<>();
			
			for(String loc : plugin.getConfig().getStringList("Signs.top_wins")) {
				int x, y, z;
                String[] array = loc.split(",");
                x = Integer.parseInt(array[0]);
                y = Integer.parseInt(array[1]);
                z = Integer.parseInt(array[2]);
                Location l = new Location(plugin.getLobby().getWorld(), x, y, z);
                if(!(plugin.getLobby().getWorld().getBlockAt(l).getState() instanceof Sign)) {
                	continue;
                }
                Sign sign = (Sign) plugin.getLobby().getWorld().getBlockAt(l).getState();
                signs.add(sign);
			}
			setTextW(signs);			
		}
	}
	
	
	private void setTextK(Set<Sign> signs) {
		
		int index = 0;
		
		for(Sign s : signs) {
			int x = index + 1;
			s.setLine(0, "Top " + x);
			s.setLine(1, getPlayerByPositionK(x).getName());
			s.setLine(2, "Kills: " + plugin.getConfigs().getStatsInfo().getString(getPlayerByPositionK(x).getUniqueId().toString() + ".kills"));
			index++;
		}
		
	}
	private void setTextW(Set<Sign> signs) {
		
		int index = 0;
		
		for(Sign s : signs) {
			int x = index + 1;
			s.setLine(0, "Top " + x);
			s.setLine(1, getPlayerByPositionW(x).getName());
			s.setLine(2, "Wins: " + plugin.getConfigs().getStatsInfo().getString(getPlayerByPositionW(x).getUniqueId().toString() + ".wins"));
			index++;
		}
		
	}
}
