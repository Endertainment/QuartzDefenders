package ua.endertainment.quartzdefenders.commands.team;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class Quit extends SubCommand{

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}
		
		Player p = (Player) sender;
		GamePlayer gp = QuartzDefenders.getInstance().getGamePlayer(p);
		
		Game game = QuartzDefenders.getInstance().getGame(p);
		
		if(game == null) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou can not quit from team when you is not in game"));
			return;
		}
		
		if(args.length == 0) {
			if(game.isInTeam(gp)) {
				GameTeam team = game.getTeam(p);
				team.quitTeam(gp);
			}
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
			
			if(!sender.hasPermission(PermissionsList.TEAM_REMOVE_PLAYER)) return; 
			
			Player pp = Bukkit.getPlayer(args[1]);
			if(pp != null && pp.isOnline()) {
				GamePlayer gpp = QuartzDefenders.getInstance().getGamePlayer(pp);
				team.quitTeam(gpp);
			} else {
				sender.sendMessage(LoggerUtil.gameMessage("Chat", "Player " + args[1] + " is not online"));
			}
			return;
		}
		
		team.quitTeam(gp);
		
	}

	@Override
	public String getUsage() {
		return new ColorFormat("&8» &b/team quit|leave|kick [team] [player] &8- &bLeave from team").format();
	}

	
}
