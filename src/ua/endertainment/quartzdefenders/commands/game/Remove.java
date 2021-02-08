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

public class Remove extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission(PermissionsList.GAME_REMOVE)) {
			sender.sendMessage(LoggerUtil.gameMessage(Language.getString("commands.chat"), Language.getString("commands.no_permissions")));
			return;
		}
		
		if(args.length == 0) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Check command usage: &b/game help"));
			return;
		}
		
		String gameID = args[0];
		
		Game game = QuartzDefenders.getInstance().getGame(gameID, false);
		
		if(game == null) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cGame with id &a" + gameID + "&7 is not exist"));
			return;
		}
		
		QuartzDefenders.getInstance().deleteGame(game);
	}

	@Override
	public String getUsage() {
		return new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &b/game remove <gameID> &8- &bRemove game from Active games").format();
	}

}
