package ua.Endertainment.QuartzDefenders.Stats;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.entity.Player;

public class StatsPlayer {

	
	private Player player;
	
	private int points;
	private int coins;
	
	private int kills;
	private int deaths;
	private float kdr;
	
	private int wins;
	private int games;
	private float winRate;
	
	private int level;
	private int nextLevel;
	private int nextLevelPoints;
	
	public StatsPlayer(Player player) {
		this.player = player;
		
		this.points = StatsManager.getInfo(player, "points");
		this.coins = StatsManager.getInfo(player, "coins");
		
		this.kills = StatsManager.getInfo(player, "kills");
		this.deaths = StatsManager.getInfo(player, "deaths");
		if(deaths == 0) this.kdr = kills; 			
		else this.kdr = ((float)this.kills)/((float)this.deaths);
		
		this.wins = StatsManager.getInfo(player, "wins");
		this.games = StatsManager.getInfo(player, "games");
		if(games == 0) this.winRate = 0;
		else this.winRate = ((float)wins / (float)games) * 100;
		
		Level l = new Level(points);
		
		this.level = l.getLevel();
		this.nextLevel = l.getNextLevel();
		this.nextLevelPoints = l.getNextLevelPoints();
	}
	

	public Player getPlayer() {
		return player;
	}
	
	/*
	 * POINTS 
	 */
	public int getPoints() {
		return points;
	}

	public void addPoints(int points) {
		this.points += points;
		StatsManager.saveInfo(player, "points", this.points);
	}
	
	public void setPoints(int points) {
		this.points = points;
		StatsManager.saveInfo(player, "points", this.points);
	}

	/*
	 * KILLS 
	 */	
	public int getKills() {
		return kills;
	}

	public void addKill() {
		this.kills += 1;
		StatsManager.saveInfo(player, "kills", this.kills);
	}
	
	public void setKills(int kills) {
		this.kills = kills;
		StatsManager.saveInfo(player, "kills", this.kills);
	}

	
	/*
	 * DEATHS 
	 */
	public int getDeaths() {
		return deaths;
	}
	
	public void addDeath() {
		this.deaths += 1;
		StatsManager.saveInfo(player, "deaths", this.deaths);
	}
	
	public void setDeaths(int deaths) {
		this.deaths = deaths;
		StatsManager.saveInfo(player, "deaths", this.deaths);
	}

	
	/*
	 * WINS 
	 */
	public int getWins() {
		return wins;
	}
	public void addWin() {
		this.wins += 1;
		StatsManager.saveInfo(player, "wins", this.wins);
	}
	public void setWins(int wins) {
		this.wins = wins;
		StatsManager.saveInfo(player, "wins", this.wins);
	}
	
	/*
	 * GAMES 
	 */
	public int getPlayedGames() {
		return games;
	}
	public void addPlayedGame() {
		this.games += 1;
		StatsManager.saveInfo(player, "games", this.games);
	}
	public void setPlayedGames(int games) {
		this.games = games;
		StatsManager.saveInfo(player, "games", this.games);
	}
	public float getWinRate() {
		return winRate;
	}
	
	/*
	 * COINS 
	 */
	public int getCoins() {
		return coins;
	}
	public void addCoins(int coins) {
		this.coins += coins;
		StatsManager.saveInfo(player, "coins", this.coins);
	}	
	public void setCoins(int coins) {
		this.coins = coins;
		StatsManager.saveInfo(player, "coins", this.coins);
	}

	
	/*
	 * LEVEL 
	 */
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}

	
	/*
	 * NEXT LEVEL 
	 */
	public int getNextLevel() {
		return nextLevel;
	}	
	public int getNextLevelPoints() {
		return nextLevelPoints;
	}
	
	/*
	 * KDR 
	 */
	public float getKdr() {
		return kdr;
	}

	
	public class Level {
		
		private int level;
		private int nextLevelPoints;
		
		private ArrayList<Integer> exp = new ArrayList<>(Arrays.asList(0,7,17,31,48,69,93,121,152,187,225,
				267,312,361,413,469,528,657,727,
				800,877,957,1041,1128,1219,1313,
				1411,1512,1617,1725,1837,1952,2071,
				2193,2319,2448,2581,2717,1857,3000,
				3147,3297,3451,3608,3769,3933,4101,
				4272,4447,4500));

		public Level(int points) {			
			try {
				for(int i = 0; i < exp.size(); i++) {
					if(points >= exp.get(i) && points < exp.get(i+1)) {
						this.level = i;
						this.nextLevelPoints = exp.get(i+1);
					}
				}
			} catch(Exception e) {
				this.level = 50;
				this.nextLevelPoints = 4500;
			}			
		}
		
		public int getLevel() {
			return level;
		}
		public int getNextLevel() {
			return level + 1;
		}
		public int getNextLevelPoints() {
			return nextLevelPoints;
		}
		
	}

}
