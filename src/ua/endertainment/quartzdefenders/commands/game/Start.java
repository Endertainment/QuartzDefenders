package ua.endertainment.quartzdefenders.commands.game;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class Start extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission(PermissionsList.GAME_START)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}
		
		Player p = (Player) sender;
		
		Game game = null;
		
		if(args.length == 0) {
			game = QuartzDefenders.getInstance().getGame(p);
		} else {
			game = QuartzDefenders.getInstance().getGame(args[0], false);
		}
		
		if(game == null) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cGame is not exist"));
			return;
		}
		
		if(!game.startCountdown()) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cGame can not be started"));
		}
		
	}

	@Override
	public String getUsage() {		
		return new ColorFormat("&8� &b/game start [gameName]&8- &bStart game").format();
	}

}
