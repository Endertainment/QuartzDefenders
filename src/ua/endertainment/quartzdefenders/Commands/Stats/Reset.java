package ua.endertainment.quartzdefenders.Commands.Stats;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.stats.StatsPlayer;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.ScoreboardLobby;

public class Reset extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.stats.reset")) {
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
			}
		}
		
		StatsPlayer sp = new StatsPlayer(p);
		sp.reset();
		sender.sendMessage(LoggerUtil.gameMessage("Chat", p.getName() + "'s stats has beed reset"));
		new ScoreboardLobby(QuartzDefenders.getInstance(), p).setScoreboard();
	}

	
	
}
