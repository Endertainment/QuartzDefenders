package ua.endertainment.quartzdefenders.commands.team;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class Lock extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission(PermissionsList.TEAM_LOCK)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(args.length == 0) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Check command usage: &b/team help"));
			return;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cOnly players can use this command"));
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
		
		team.setAllowJoin(!team.isJoinAllow());
		
		sender.sendMessage(LoggerUtil.gameMessage("Game", "Team " + team.getName() + "&7 is &a" + (team.isJoinAllow() ? "unlocked" : "locked") + "&7 now"));
		
	}

	@Override
	public String getUsage() {
		return new ColorFormat("&8» &b/team lock <team> &8- &bDisable to join in team").format();
	}

}
