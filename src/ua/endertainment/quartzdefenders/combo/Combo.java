package ua.endertainment.quartzdefenders.combo;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.combo.ComboManager.Kills;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class Combo {

	private String title;
	private BarColor bc;
	
	private GamePlayer player;
	private Kills count;
	private long lastTime;
	private BossBar bossBar;
	private Game game;
	
	public Combo(GamePlayer player, Game game) {
		this.player = player;
		this.count = Kills.FIRST;
		this.game = game;
		this.lastTime = System.currentTimeMillis();
		
		this.bossBar = Bukkit.createBossBar(" ", BarColor.WHITE, BarStyle.SOLID);
	}
	
	
	public GamePlayer getPlayer() {
		return player;
	}
	
	public Kills getKillsCount() {
		return count;
	}
	
	public long getLastTime() {
		return lastTime;
	}
	
	public BossBar getBossBar() {
		return bossBar;
	}
	
	public void setCount(Kills count) {
		this.count = count;
		
	}
	
	public void reset() {
		bossBar.setTitle(" ");
		
		this.count = Kills.ZERO;
		this.lastTime = 0;
		updateTime();
	}
	
	public void updateTime() {
		lastTime = System.currentTimeMillis();
	}
	
	public void up() {
            LoggerUtil.info(count.toString());
		switch(count) {
			case ZERO:
				count = Kills.FIRST;
                                break;
			case FIRST:
				count = Kills.DOUBLE;
				show();
                                break;
			case DOUBLE:
				count = Kills.TRIPLE;
				show();
                                break;
			case TRIPLE:
				count = Kills.ULTRA;
				show();
                                break;
			case ULTRA:
				count = Kills.RAMPAGE;
				show();
                                break;
			case RAMPAGE:
				count = Kills.RAMPAGE;
				show();
                                break;
			default:
				count = Kills.ZERO;		
		}
	}
	
	public boolean compareTime() {
		return (lastTime + 15*1000) >= System.currentTimeMillis();
	}
		
	private void show() {
		Random random = new Random();
		bc = BarColor.values()[random.nextInt(BarColor.values().length)];
		
		this.title = new ColorFormat(count.getName()+ChatColor.GRAY + (count == Kills.RAMPAGE ? "" : " kill") + " by " + player.getDisplayName()).format();
		
		bossBar.setColor(bc);
		bossBar.setTitle(title);
		
		game.showBossBar(bossBar);
		
	}
}
