package ua.endertainment.quartzdefenders.Utils;

import org.bukkit.Location;
import org.bukkit.World;

import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.game.GameTeam;

public class Cuboid {
	
	public enum CubType {
		SPAWN, QUARTZ
	}
	
	private final World world;
	private final int x1;
	private final int y1;
	private final int z1;
	private final int x2;
	private final int y2;
	private final int z2;
	private GameTeam team;
	private CubType type;
	
	public Cuboid(Location loc, int rad, GameTeam team, CubType type) {
		Location l1, l2;
		l1 = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).add(rad, rad, rad);
		l2 = new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).subtract(rad, rad, rad);
		this.world = l1.getWorld();
	    this.x1 = Math.min(l1.getBlockX(), l2.getBlockX());
	    this.y1 = Math.min(l1.getBlockY(), l2.getBlockY());
	    this.z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
	    this.x2 = Math.max(l1.getBlockX(), l2.getBlockX());
	    this.y2 = Math.max(l1.getBlockY(), l2.getBlockY());
	    this.z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
		this.team = team;
		this.type = type;
	}
	
	public boolean contains(int x, int y, int z) {
		return (x >= this.x1) && (x <= this.x2) && (y >= this.y1) && (y <= this.y2) && (z >= this.z1) && (z <= this.z2);
	}
	public boolean contains(Location loc) {
		if(loc.getWorld() != world) return false;
		return contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
	public GameTeam getTeam() {
		return team;
	}
	public CubType getCubType() {
		return type;
	}
	public boolean canBuild(GamePlayer p, Location loc) {
		if(!contains(loc)) return true; 
		if(type == CubType.QUARTZ) return false;
		if(type == CubType.SPAWN) 
			if(team.contains(p)) return true;
			else return false;		
		return false;
	}
	
	public boolean canBreak(GamePlayer p, Location loc) {
		if(!contains(loc)) return true;
		if(type == CubType.QUARTZ) { 
			if(team.contains(p)) {
				if(loc.equals(team.getTeamQuartz().getLocation())) return false;
				return true;
			}
			if(!team.contains(p)) {
				if(loc.equals(team.getTeamQuartz().getLocation())) return true;
				return false;
			}
		}
		if(type == CubType.SPAWN) 
			return true;
		return false;
	}
}
