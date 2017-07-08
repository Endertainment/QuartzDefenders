package ua.Endertainment.QuartzDefenders.Commands.Team;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.GamePlayer;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Stats.StatsManager;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;

public class Info extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Check command usage: &b/team help"));
			return;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}
		
		if(!sender.hasPermission("QuartzDefenders.team.info")) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return; 
		}
		
		Player p = (Player) sender;
		
		Game game = QuartzDefenders.getInstance().getGame(p);
		
		if(game == null) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou can not use this command when you is not in game"));
			return;
		}
		
		GameTeam team = game.getTeam(args[0]);
		
		if(team == null) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Team " + args[0] + "&7 is not exist"));
			return;
		}
		GamePlayer gp = QuartzDefenders.getInstance().getGamePlayer(p);
		
		gp.sendMessage("&8-------------------- &3Team &8--------------------");
		gp.sendMessage("&8» &fName: &b" + team.getName());
		gp.sendMessage("&8» &fPlayers: &b" + team.getPlayersSize() + "&f/&b" + team.intPlayersInTeam());
		gp.sendMessage("&8» &fTeam KD: &b" + StatsManager.getTeamKDR(team));
		gp.sendMessage("&8» &fSpawn location: &b" + team.getSpawnLocation().getBlockX() + " " 
												  + team.getSpawnLocation().getBlockY() + " " 
												  + team.getSpawnLocation().getBlockZ());
		gp.sendMessage("&8» &fCan respawn: &b" + team.canRespawn());
	}

}
