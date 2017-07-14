package ua.Endertainment.QuartzDefenders;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;

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
	
	public boolean breakQuartz(GamePlayer player) {
		if(team.contains(player)) {
			replace();
			player.sendMessage(LoggerUtil.gameMessage("Game", "&cYou can not break quartz your own team"));
			return false;
		}
		
		for(GamePlayer p : team.getPlayers()) {
			p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
		}
		for(GamePlayer p : game.getPlayers()) {
			if(!team.contains(p)) p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
		}
				
		setQuartzHealth(getQuartzHealth() - 1);
		this.replace();
		
		player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 4*20, 0), true);
		
		if(getQuartzHealth() == 0) {
			for(GamePlayer p : game.getPlayers()) {
				p.sendMessage(LoggerUtil.gameMessage("Game", "Player " + player.getDisplayName() + 
						"&7 has destroyed the quartz of the " + team.getName() + "&7 team"));
				p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
				
				StatsPlayer sp = new StatsPlayer(player.getPlayer());
				sp.addBrokenQuartz();
				
			}
			for(GamePlayer p : team.getPlayers()) {
				p.sendMessage(LoggerUtil.gameMessage("Game", "&cYour quartz is broken"));
				p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0), true);
				p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 0), true);
				p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0), true);
			}
		}	
		return true;
	}
	public void breakQuartz() {
		setQuartzHealth(getQuartzHealth() - 1);
		replace();
	}
	public void destroyQuartz() {
		setQuartzHealth(0);
		replace();
	}

	public Location getLocation() {
		return location;
	}

	public int getQuartzHealth() {
		return quartzHealth;
	}

	public void setQuartzHealth(int quartzHealth) {
		if(this.quartzHealth == 0) return;
		this.quartzHealth = quartzHealth;
	}
	
	public void checkQuartz() {
		Block b = game.getGameWorld().getBlockAt(getLocation());
		if(b.getType() != Material.QUARTZ_ORE || b.getType() != Material.BEDROCK) {
			replace();
		}
	}
	
	public void replace() {
		Block b = game.getGameWorld().getBlockAt(getLocation());
		if(getQuartzHealth() > 0)  {
			Bukkit.getScheduler().scheduleSyncDelayedTask(QuartzDefenders.getInstance(), new Runnable() {
				@Override
				public void run() {
					b.setType(Material.QUARTZ_ORE);					
				}
			});
		}
		else {
			Bukkit.getScheduler().scheduleSyncDelayedTask(QuartzDefenders.getInstance(), new Runnable() {
				@Override
				public void run() {
					b.setType(Material.BEDROCK);					
				}
			});
		}
	}
	
	public GameTeam getTeam() {
		return team;
	}

	public Game getGame() {
		return game;
	}
	
}
