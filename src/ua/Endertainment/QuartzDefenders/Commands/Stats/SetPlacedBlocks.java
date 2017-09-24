package ua.Endertainment.QuartzDefenders.Commands.Stats;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;
import ua.Endertainment.QuartzDefenders.Utils.ScoreboardLobby;

public class SetPlacedBlocks extends SubCommand {


	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.stats.setPlacedBlocks")) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(args.length == 0) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Check command usage: &b/stats help"));
			return;
		}
		
		int placedBlocks = 0;
		
		try {
			placedBlocks = Integer.parseInt(args[0]);		
		} catch(NumberFormatException e) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", args[0] + "&7 is not a valid number"));
			return;
		}
		
		placedBlocks = Math.max(0, placedBlocks);
		
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
		sp.setPlacedBlocks(placedBlocks);
		sender.sendMessage(LoggerUtil.gameMessage("Stats", "&6" + placedBlocks + "&7 placed blocks set for player &6" + sp.getPlayer().getName()));
		
		if(p.getWorld() == QuartzDefenders.getInstance().getLobby().getWorld()) {
			ScoreboardLobby sb = new ScoreboardLobby(QuartzDefenders.getInstance(), p);
			sb.setScoreboard();
		}
	}	


}
