package ua.endertainment.quartzdefenders.Commands.Game;

import org.bukkit.command.CommandSender;

import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.ColorFormat;

public class Help extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		int page = 1;
		if(args.length == 0) {
			sendHelp(page, sender);
			return;
		}
		try {
			page = Integer.parseInt(args[0]);
			if(page >= 3) page = 1; 
		} catch(NumberFormatException ex) {
			page = 1;
		}
		sendHelp(page, sender);
	}
	
	private void sendHelp(int page, CommandSender sender) {		
		
		switch(page) {
			case 1: {
				sender.sendMessage(new ColorFormat("&8-------------------- &3Help &8--------------------").format());
				sender.sendMessage(new ColorFormat("&8» &b/game help &8- &bShow help").format());
				sender.sendMessage(new ColorFormat("&8» &b/game add <gameID> &8- &bAdd game to Active games").format());
				sender.sendMessage(new ColorFormat("&8» &b/game remove <gameID> &8- &bRemove game from Active games").format());
				sender.sendMessage(new ColorFormat("&8» &b/game lock [gameID] &8- &bLock game").format());
				sender.sendMessage(new ColorFormat("&8» &b/game end [gameID] &8- &bEnd game").format());
				sender.sendMessage(new ColorFormat("&8» &b/game gameslist  &8- &bShow all games").format());
				// TODO
			}
			case 2: {
				sender.sendMessage(new ColorFormat("&8» &b/game setupOres &8- &bAdd new regenerative ores").format());
				sender.sendMessage(new ColorFormat("&8» &b/game setupQuartz <team> [x] [y] [z] &8- &bSet quartz").format());
				sender.sendMessage(new ColorFormat("&8» &b/game setupSpawn <team> [x] [y] [z] [yaw] [pitch] &8- &bSet quartz").format());
			}
		} 
		
	}
	
	
}
