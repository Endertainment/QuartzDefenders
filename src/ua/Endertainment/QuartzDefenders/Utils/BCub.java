package ua.Endertainment.QuartzDefenders.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import ua.Endertainment.QuartzDefenders.Game;

public class BCub {

	private final World world;
	private final int x1;
	private final int y1;
	private final int z1;
	private final int x2;
	private final int y2;
	private final int z2;
	
	public BCub(Location center, Game game) {
		
		Bukkit.broadcastMessage(center.toString());
		
		Bukkit.broadcastMessage(game.getBuildRadius() + "");
		Bukkit.broadcastMessage(game.getBuildHeight() + "");
		
		Location loc1 = center.clone().add(game.getBuildRadius(), 0, game.getBuildRadius());
		Location loc2 = center.clone().subtract(game.getBuildRadius(), 0, game.getBuildRadius());
		loc1.setY(0);
		loc2.setY(game.getBuildHeight());
		
		Bukkit.broadcastMessage(loc1.toString());
		Bukkit.broadcastMessage(loc2.toString());
		
		this.world = loc1.getWorld();
	    this.x1 = Math.min(loc1.getBlockX(), loc2.getBlockX());
	    this.y1 = Math.min(loc1.getBlockY(), loc2.getBlockY());
	    this.z1 = Math.min(loc1.getBlockZ(), loc2.getBlockZ());
	    this.x2 = Math.max(loc1.getBlockX(), loc2.getBlockX());
	    this.y2 = Math.max(loc1.getBlockY(), loc2.getBlockY());
	    this.z2 = Math.max(loc1.getBlockZ(), loc2.getBlockZ());
		
	}
	
	public boolean contains(int x, int y, int z) {
		return (x >= this.x1) && (x <= this.x2) && (y >= this.y1) && (y <= this.y2) && (z >= this.z1) && (z <= this.z2);
	}
	public boolean contains(Location loc) {
		if(loc.getWorld() != world) return false;
		return contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	
}
