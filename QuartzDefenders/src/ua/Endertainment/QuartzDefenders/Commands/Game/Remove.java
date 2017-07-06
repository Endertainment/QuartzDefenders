package ua.Endertainment.QuartzDefenders.Commands.Game;

import org.bukkit.command.CommandSender;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;

public class Remove extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.game.removeGame")) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(args.length == 0) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "Check command usage: &b/game help"));
			return;
		}
		
		String gameID = args[0];
		
		Game game = QuartzDefenders.getInstance().getGame(gameID, false);
		
		if(game == null) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cGame with id &a" + gameID + "&7 is not exist"));
			return;
		}
		
		QuartzDefenders.getInstance().deleteGame(game);
	}

}
