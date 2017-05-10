package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;

public class ChatFormatEvent implements Listener{

	private QuartzDefenders plugin;
	
	public ChatFormatEvent(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void chatForm(AsyncPlayerChatEvent e) {
		StatsPlayer p = new StatsPlayer(e.getPlayer());
		String format = e.getFormat();
		//format = "{mode}{lvl} %1$s §8»§r %2$s";
		format = "{lvl} %1$s §8»§r %2$s";
		format = format.replace("{lvl}", new ColorFormat("&b" + p.getLevel() + "&8│&r").format()).replace("%1$s", e.getPlayer().getDisplayName());
		
		if(e.getPlayer().hasPermission("QuartzDefenders.ColorChat")) e.setMessage(new ColorFormat(e.getMessage()).format());
		e.setFormat(format);		
	}
	
}
