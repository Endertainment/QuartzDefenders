package ua.endertainment.quartzdefenders.combo;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.combo.ComboManager.Kills;
import ua.endertainment.quartzdefenders.utils.ColorFormat;

public class Combo {

	private String title;
	private BarColor bc;
	private ChatColor cc;
	
	private GamePlayer player;
	private Kills count;
	private long time;
	private BossBar bossBar;
	
	public Combo(GamePlayer player) {
		this.player = player;
		this.count = Kills.ZERO;
		this.time = time;
		
		this.bossBar = Bukkit.createBossBar(" ", BarColor.WHITE, BarStyle.SEGMENTED_20);
	}
	
	
	public GamePlayer getPlayer() {
		return player;
	}
	
	public Kills getKillsCount() {
		return count;
	}
	
	public long getTime() {
		return time;
	}
	
	public BossBar getBossBar() {
		return bossBar;
	}
	
	public void setCount(Kills count) {
		this.count = count;
		
	}
	
	public void reset() {
		this.bossBar = Bukkit.createBossBar(" ", BarColor.WHITE, BarStyle.SEGMENTED_20);
		this.count = Kills.ZERO;
		this.time = 0;
	}
	
	
	
	
	
	private void gen() {
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
		
		this.title = new ColorFormat(cc + "" + count + " kill by " + player.getDisplayName()).format();
	}
}
