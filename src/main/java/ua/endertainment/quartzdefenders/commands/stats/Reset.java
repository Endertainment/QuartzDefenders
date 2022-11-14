package ua.endertainment.quartzdefenders.commands.stats;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.stats.StatsPlayer;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.LobbySidebar;
import ua.endertainment.quartzdefenders.utils.Symbols;

public class Reset extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission(PermissionsList.STATS_RESET)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		Player p = null;
		
		if(args.length >= 1) {
			p = Bukkit.getPlayer(args[0]);
			if(p == null) {
				sender.sendMessage(LoggerUtil.gameMessage("Chat", "Player " + args[0] + "&7 is not online"));
				return;
			}
			
		} 
		
		if(args.length == 0) {
			if(sender instanceof Player) {
				p = (Player) sender;
			} else {
				sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou are not a player"));
				return;
			}
		}
		
		StatsPlayer sp = new StatsPlayer(p);
		sp.reset();
		sender.sendMessage(LoggerUtil.gameMessage("Chat", p.getName() + "'s stats has beed reset"));
		new LobbySidebar(QuartzDefenders.getInstance(), p).setScoreboard();
	}

	@Override
	public String getUsage() {
		return new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &b/stats reset [player] &8- &bReset all player's stats").format();
	}

	
	
}
