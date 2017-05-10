package ua.Endertainment.QuartzDefenders;

import org.bukkit.Location;

public class GameQuartz {

	private Location location;
	private int quartzHealth;
	private GameTeam team;
	private Game game;
	
	public GameQuartz(Game game, GameTeam team, Location location, int quartzHealth) {
		this.team = team;
		this.game = game;
		this.location = location;
		this.quartzHealth = quartzHealth;
	}
	
	public void breakQuartz(GameTeam teamE, GamePlayer player) {
		if(!team.contains(player))
		setQuartzHealth(getQuartzHealth() - 1);
	}
	public void breakQuartz() {
		setQuartzHealth(getQuartzHealth() - 1);
	}
	public void destroyQuartz() {
		setQuartzHealth(0);
	}

	public Location getLocation() {
		return location;
	}

	public int getQuartzHealth() {
		return quartzHealth;
	}

	public void setQuartzHealth(int quartzHealth) {
		this.quartzHealth = quartzHealth;
	}

	public GameTeam getTeam() {
		return team;
	}

	public Game getGame() {
		return game;
	}
	
}
