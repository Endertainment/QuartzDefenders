package ua.endertainment.quartzdefenders.commands.game;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.Symbols;

public class Lock extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission(PermissionsList.GAME_LOCK)) {
			sender.sendMessage(LoggerUtil.gameMessage(Language.getString("commands.chat"), Language.getString("commands.no_permissions")));
			return;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(LoggerUtil.gameMessage(Language.getString("commands.chat"), Language.getString("commands.only_players_can_use")));
			return;
		}
		
		Player p = (Player) sender;
		
		Game game;
		
		if(args.length == 0) {
			game = QuartzDefenders.getInstance().getGame(p);
		} else {
			game = QuartzDefenders.getInstance().getGame(args[0], false);
		}
		
		if(game == null) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cGame is not exist"));
			return;
		}
		
		game.setLockGame(!game.isGameLocked());		
		
		sender.sendMessage(LoggerUtil.gameMessage("Game", "Game " + game.getGameName() + "&7 is &a" + (game.isGameLocked() ? "locked" : "unlocked") + "&7 now"));
		
	}

	@Override
	public String getUsage() {
		return new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &b/game lock [gameID] &8- &bLock game").format();
	}

	
	
}
