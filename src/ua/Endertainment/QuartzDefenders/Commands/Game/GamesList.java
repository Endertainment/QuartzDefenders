package ua.Endertainment.QuartzDefenders.Commands.Game;

import org.bukkit.command.CommandSender;

import ua.Endertainment.QuartzDefenders.Game.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;

public class GamesList extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.game.gamesList")) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		int index = 1;
		for(Game game : QuartzDefenders.getInstance().getGames()) {
			sender.sendMessage(new ColorFormat("&8» &fGame &b#" + index).format());
			sender.sendMessage(new ColorFormat("&8» &fGame ID: &b" + game.getGameId()).format());
			sender.sendMessage(new ColorFormat("&8» &fGame name: &b" + game.getGameName()).format());
			sender.sendMessage(new ColorFormat("&8» &fMap: &b" + game.getColorWorldName()).format());
			sender.sendMessage(new ColorFormat("&8» &fWorld: &b" + game.getTechWorldName()).format());
			sender.sendMessage(new ColorFormat("&8» &fState: &b" + game.getGameState().toString()).format());
			index++;
		}
		
	}

}
