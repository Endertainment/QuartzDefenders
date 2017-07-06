package ua.Endertainment.QuartzDefenders.Commands.Game;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;

public class Lock extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.game.lockGame")) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cOnly players can use this command"));
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
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cGame is not exist"));
			return;
		}
		
		game.setLockGame(!game.isGameLocked());		
		
		sender.sendMessage(GameMsg.gameMessage("Game", "Game " + game.getGameName() + "&7 is &a" + (game.isGameLocked() ? "locked" : "unlocked") + "&7 now"));
		
	}

	
	
}
