package ua.Endertainment.QuartzDefenders.Commands.Stats;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;
import ua.Endertainment.QuartzDefenders.Utils.ScoreboardLobby;

public class AddCoins extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.stats.addCoins")) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(args.length == 0) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "Check command usage: &b/stats help"));
			return;
		}
		
		int coins = 0;
		
		try {
			coins = Integer.parseInt(args[0]);		
		} catch(NumberFormatException e) {
			sender.sendMessage(GameMsg.gameMessage("Chat", args[0] + "&7 is not a valid number"));
			return;
		}
		
		Player p = null;
		
		if(args.length >= 2) {
			p = Bukkit.getPlayer(args[1]);
			if(p == null) {
				sender.sendMessage(GameMsg.gameMessage("Chat", "Player " + args[0] + "&7 is not online"));
				return;
			}
		} 
		
		if(args.length == 1) {
			if(sender instanceof Player) {
				p = (Player) sender;
			}
		}
		

		StatsPlayer sp = new StatsPlayer(p);
		sp.addCoins(coins);		
		sender.sendMessage(GameMsg.gameMessage("Stats", "&6" + coins + "&7 coins added for player &6" + sp.getPlayer().getName()));
		
		if(p.getWorld() == QuartzDefenders.getInstance().getLobby().getLocation().getWorld()) {
			ScoreboardLobby sb = new ScoreboardLobby(QuartzDefenders.getInstance(), p);
			sb.setScoreboard();
		}
	}	
	
}
