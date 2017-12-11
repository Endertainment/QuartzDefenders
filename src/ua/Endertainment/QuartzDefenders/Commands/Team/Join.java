package ua.Endertainment.QuartzDefenders.Commands.Team;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.Game.Balance;
import ua.Endertainment.QuartzDefenders.Game.Game;
import ua.Endertainment.QuartzDefenders.Game.GamePlayer;
import ua.Endertainment.QuartzDefenders.Game.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;

public class Join extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}
		
		Player p = (Player) sender;
		GamePlayer gp = QuartzDefenders.getInstance().getGamePlayer(p);
		
		if(args.length == 0) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Check command usage: &b/team help"));
			return;
		}
		
		Game game = QuartzDefenders.getInstance().getGame(p);
		
		if(game == null) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou can not join to team when you is not in game"));
			return;
		}
		
		GameTeam team = game.getTeam(args[0]);
		
		if(team == null) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Team " + args[0] + "&7 is not exist"));
			String teams = "";
			for(GameTeam teamX : game.getTeams().values()) {
				teams += teamX.getName() + "&7, ";
			}
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Active teams: " + teams));
			return;
		}
		
		if(args.length >= 2) {
			
			if(!sender.hasPermission("QuartzDefenders.team.addPlayer")) return; 
			
			Player pp = Bukkit.getPlayer(args[1]);
			if(pp != null && pp.isOnline()) {
				if(QuartzDefenders.getInstance().getGame(pp) == null) {
					sender.sendMessage(LoggerUtil.gameMessage("Chat", "Player " + pp.getDisplayName() + "&7 is not in game"));
					return;
				}
				GamePlayer gpp = QuartzDefenders.getInstance().getGamePlayer(pp);
				
				team.joinTeam(gpp, true);
			} else {
				sender.sendMessage(LoggerUtil.gameMessage("Chat", "Player " + args[1] + " is not online"));
			}
			return;
		}
		if(!team.canJoin()) {
			sender.sendMessage(LoggerUtil.gameMessage("Game", "You can not join to team while game is running"));
			return;
		}
		if(new Balance(game, game.getBalanceType(), gp, game.getTeams().values(), team).isTeamsBalanced()) {
			team.joinTeam(gp, false);
		}
	}

}
