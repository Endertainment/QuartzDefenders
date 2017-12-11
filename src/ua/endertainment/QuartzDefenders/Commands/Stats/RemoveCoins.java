package ua.endertainment.quartzdefenders.commands.stats;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.stats.StatsPlayer;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.ScoreboardLobby;

public class RemoveCoins extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.stats.removeCoins")) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(args.length == 0) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Check command usage: &b/stats help"));
			return;
		}
		
		int coins = 0;
		
		try {
			coins = Integer.parseInt(args[0]);		
		} catch(NumberFormatException e) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", args[0] + "&7 is not a valid number"));
			return;
		}
		
		Player p = null;
		
		if(args.length >= 2) {
			p = Bukkit.getPlayer(args[1]);
			if(p == null) {
				sender.sendMessage(LoggerUtil.gameMessage("Chat", "Player " + args[0] + "&7 is not online"));
				return;
			}
			
		} 
		
		if(args.length == 1) {
			if(sender instanceof Player) {
				p = (Player) sender;
			}
		}
		
		StatsPlayer sp = new StatsPlayer(p);
		sp.addCoins(-coins);
		sender.sendMessage(LoggerUtil.gameMessage("Stats", "&6" + coins + "&7 coins removed from player &6" + sp.getPlayer().getName()));
		
		if(p.getWorld() == QuartzDefenders.getInstance().getLobby().getWorld()) {
			ScoreboardLobby sb = new ScoreboardLobby(QuartzDefenders.getInstance(), p);
			sb.setScoreboard();
		}
	}
	
}
