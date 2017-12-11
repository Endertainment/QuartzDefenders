package ua.endertainment.quartzdefenders.commands.game;

import org.bukkit.command.CommandSender;

import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class Add extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.game.addGame")) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(args.length == 0) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Check command usage: &b/game help"));
			return;
		}
		
		String gameID = args[0];
		
		QuartzDefenders.getInstance().addGame(gameID);		
	}

}
