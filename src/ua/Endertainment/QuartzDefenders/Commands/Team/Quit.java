package ua.Endertainment.QuartzDefenders.Commands.Team;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.GamePlayer;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;

public class Quit extends SubCommand{

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}
		
		Player p = (Player) sender;
		GamePlayer gp = QuartzDefenders.getInstance().getGamePlayer(p);
		
		Game game = QuartzDefenders.getInstance().getGame(p);
		
		if(game == null) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cYou can not quit from team when you is not in game"));
			return;
		}
		
		if(args.length == 0) {
			if(game.isPlayerInTeam(gp)) {
				GameTeam team = game.getTeam(p);
				team.quitTeam(gp);
			}
			return;
		}		
		
		GameTeam team = game.getTeam(args[0]);
		
		if(team == null) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "Team " + args[0] + "&7 is not exist"));
			String teams = "";
			for(GameTeam teamX : game.getTeams().values()) {
				teams += teamX.getName() + "&7, ";
			}
			sender.sendMessage(GameMsg.gameMessage("Chat", "Active teams: " + teams));
			return;
		}
		
		if(args.length >= 2) {
			
			if(!sender.hasPermission("QuartzDefenders.team.removePlayer")) return; 
			
			Player pp = Bukkit.getPlayer(args[1]);
			if(pp != null && pp.isOnline()) {
				GamePlayer gpp = QuartzDefenders.getInstance().getGamePlayer(pp);
				team.quitTeam(gpp);
			} else {
				sender.sendMessage(GameMsg.gameMessage("Chat", "Player " + args[1] + " is not online"));
			}
			return;
		}
		
		team.quitTeam(gp);
		
	}

	
}
