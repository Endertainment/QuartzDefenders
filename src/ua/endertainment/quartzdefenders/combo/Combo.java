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

public class Combo {

	private String title;
	private BarColor bc;
	private ChatColor cc;
	
	private GamePlayer player;
	private Kills count;
	private long lastTime;
	private BossBar bossBar;
	private Game game;
	
	public Combo(GamePlayer player, Game game) {
		this.player = player;
		this.count = Kills.ZERO;
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
	}
	
	public void updTime() {
		lastTime = System.currentTimeMillis();
	}
	
	public void up() {
		switch(count) {
			case ZERO:
				count = Kills.FIRST;
			case FIRST:
				count = Kills.DOUBLE;
				show();
			case DOUBLE:
				count = Kills.TRIPLE;
				show();
			case TRIPLE:
				count = Kills.ULTRA;
				show();
			case ULTRA:
				count = Kills.RAMPAGE;
				show();
			case RAMPAGE:
				count = Kills.RAMPAGE;
				show();
			default:
				count = Kills.ZERO;		
		}
	}
	
	public boolean compareTime() {
		if((lastTime + 15*1000) >= System.currentTimeMillis()) {
			return true;
		}
		return false;
	}
		
	private void show() {
		Random rand = new Random();
		int i = rand.nextInt(7) + 1;
		switch (i) {
			case 1:
				this.bc = BarColor.PINK;
				this.cc = ChatColor.LIGHT_PURPLE;
			case 2:
				this.bc = BarColor.BLUE;
				this.cc = ChatColor.BLUE;
			case 3:
				this.bc = BarColor.RED;
				this.cc = ChatColor.RED;
			case 4:
				this.bc = BarColor.GREEN;
				this.cc = ChatColor.GREEN;
			case 5:
				this.bc = BarColor.YELLOW;
				this.cc = ChatColor.YELLOW;
			case 6:
				this.bc = BarColor.PURPLE;
				this.cc = ChatColor.DARK_PURPLE;
			case 7:
				this.bc = BarColor.WHITE;
				this.cc = ChatColor.WHITE;
		}
		
		this.title = new ColorFormat(cc + "" + count + (count == Kills.RAMPAGE ? "" : " kill") + " by " + player.getDisplayName()).format();
		
		bossBar.setColor(bc);
		bossBar.setTitle(title);
		
		game.showBossBar(bossBar);
		
	}
}
