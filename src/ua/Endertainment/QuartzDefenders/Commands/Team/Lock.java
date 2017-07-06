package ua.Endertainment.QuartzDefenders.Commands.Team;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;

public class Lock extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "Check command usage: &b/team help"));
			return;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}
		
		if(!sender.hasPermission("QuartzDefenders.team.lockTeam")) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		Player p = (Player) sender;
		
		Game game = QuartzDefenders.getInstance().getGame(p);
		
		if(game == null) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cYou can not use this command when you is not in game"));
			return;
		}
		
		GameTeam team = game.getTeam(args[0]);
		
		if(team == null) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "Team " + args[0] + "&7 is not exist"));
			return;
		}
		
		team.setAllowJoin(!team.isJoinAllow());
		
		sender.sendMessage(GameMsg.gameMessage("Game", "Team " + team.getName() + "&7 is &a" + (team.isJoinAllow() ? "unlocked" : "locked") + "&7 now"));
		
	}

}
