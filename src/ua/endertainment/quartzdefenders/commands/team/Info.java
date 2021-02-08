package ua.endertainment.quartzdefenders.commands.team;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.stats.StatsManager;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.Symbols;

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
		
		if(!sender.hasPermission(PermissionsList.TEAM_INFO)) {
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
		gp.sendMessage("&8"+Symbols.RIGHT_QUOTE+" &fName: &b" + team.getName());
		gp.sendMessage("&8"+Symbols.RIGHT_QUOTE+" &fPlayers: &b" + team.getPlayersSize() + "&f/&b" + team.intPlayersInTeam());
		gp.sendMessage("&8"+Symbols.RIGHT_QUOTE+" &fTeam KD: &b" + StatsManager.getTeamKDR(team));
		gp.sendMessage("&8"+Symbols.RIGHT_QUOTE+" &fSpawn location: &b" + team.getSpawnLocation().getBlockX() + " " 
												  + team.getSpawnLocation().getBlockY() + " " 
												  + team.getSpawnLocation().getBlockZ());
		gp.sendMessage("&8� &fCan respawn: &b" + team.canRespawn());
	}

	@Override
	public String getUsage() {
		return new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &b/team info <team> &8- &bShow team's info").format();
	}

}
