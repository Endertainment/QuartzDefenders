package ua.endertainment.quartzdefenders.commands.game;

import org.bukkit.command.CommandSender;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.Symbols;

public class GamesList extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission(PermissionsList.GAME_LIST)) {
			sender.sendMessage(LoggerUtil.gameMessage(Language.getString("commands.chat"), Language.getString("commands.no_permissions")));
			return;
		}
		int index = 1;
		for(Game game : QuartzDefenders.getInstance().getGames()) {
			sender.sendMessage(new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &fGame &b#" + index).format());
			sender.sendMessage(new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &fGame ID: &b" + game.getGameId()).format());
			sender.sendMessage(new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &fGame name: &b" + game.getGameName()).format());
			sender.sendMessage(new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &fMap: &b" + game.getColorWorldName()).format());
			sender.sendMessage(new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &fWorld: &b" + game.getTechWorldName()).format());
			sender.sendMessage(new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &fState: &b" + game.getGameState().toString()).format());
			index++;
		}
		
	}

	@Override
	public String getUsage() {
		return new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &b/game gameslist  &8- &bShow all games").format();
	}

}
