package ua.endertainment.quartzdefenders.commands.game;

import java.util.LinkedHashMap;

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
			if(page > 3) page = 1; 
		} catch(NumberFormatException ex) {
			page = 1;
		}
		sendHelp(page, sender);
	}
	
	private final int inPageLines = 6;
	
	private void sendHelp(int page, CommandSender sender) {		
		
		LinkedHashMap<String, SubCommand> map = CommandGameManager.getCommands();
		int pages = (int)Math.ceil(map.size() / (double)inPageLines);
		int p = page-1;
		int i_b = p*inPageLines;
		int i_e = p*inPageLines+inPageLines > map.size() ? map.size() : p*inPageLines+inPageLines;

		sender.sendMessage(new ColorFormat("&8-------------------- &3Help &8--------------------").format());		
		for (int i = i_b; i < i_e; i++) {
			sender.sendMessage(map.get(map.keySet().toArray()[i]).getUsage());
		}
		/*
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
				break;
			}
			case 2: {
				sender.sendMessage(new ColorFormat("&8» &b/game setupOres &8- &bAdd new regenerative ores").format());
				sender.sendMessage(new ColorFormat("&8» &b/game setupQuartz <team> [x] [y] [z] &8- &bSet quartz").format());
				sender.sendMessage(new ColorFormat("&8» &b/game setupSpawn <team> [x] [y] [z] [yaw] [pitch] &8- &bSet spawn").format());
				break;
			}
			default {
				println("Wrong menu cpntent, try enter again");
				break;
			}
		} 
		*/
		
	}

	@Override
	public String getUsage() {
		return new ColorFormat("&8» &b/game help &8- &bShow help").format();
	}
	
	
}
