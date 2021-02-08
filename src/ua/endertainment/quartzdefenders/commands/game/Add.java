package ua.endertainment.quartzdefenders.commands.game;

import org.bukkit.command.CommandSender;

import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.Symbols;

public class Add extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission(PermissionsList.GAME_ADD)) {
			sender.sendMessage(LoggerUtil.gameMessage(Language.getString("commands.chat"), Language.getString("commands.no_permissions")));
			return;
		}
		
		if(args.length == 0) {
			sender.sendMessage(LoggerUtil.gameMessage(Language.getString("commands.chat"), Language.getString("commands.command_ussage", CommandGameManager.getInstance().find("add"))));
			return;
		}
		
		String gameID = args[0];
		
		QuartzDefenders.getInstance().addGame(gameID);		
	}

	@Override
	public String getUsage() {
		return new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &b/game add <gameID> &8- &bAdd game to Active games").format();
	}

}
