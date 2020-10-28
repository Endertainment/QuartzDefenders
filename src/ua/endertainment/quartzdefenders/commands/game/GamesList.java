package ua.endertainment.quartzdefenders.commands.game;

import org.bukkit.command.CommandSender;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class GamesList extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission(PermissionsList.GAME_LIST)) {
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

	@Override
	public String getUsage() {
		return new ColorFormat("&8» &b/game gameslist  &8- &bShow all games").format();
	}

}
