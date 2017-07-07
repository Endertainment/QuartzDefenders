package ua.Endertainment.QuartzDefenders.Commands.Stats;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;
import ua.Endertainment.QuartzDefenders.Utils.ScoreboardLobby;

public class Reset extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.stats.reset")) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(args.length == 0) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "Check command usage: &b/stats help"));
			return;
		}
		
		Player p = null;
		
		if(args.length >= 1) {
			p = Bukkit.getPlayer(args[0]);
			if(p == null) {
				sender.sendMessage(GameMsg.gameMessage("Chat", "Player " + args[0] + "&7 is not online"));
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
		sender.sendMessage(GameMsg.gameMessage("Chat", p.getName() + "'s stats has beed reset"));
		new ScoreboardLobby(QuartzDefenders.getInstance(), p).setScoreboard();
	}

	
	
}
