package ua.endertainment.quartzdefenders.commands.stats;

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
			if(page >= 2) page = 1; 
		} catch(NumberFormatException ex) {
			page = 1;
		}
		sendHelp(page, sender);
	}

	private void sendHelp(int page, CommandSender sender) {		
		
		switch(page) {
			case 1: {
				sender.sendMessage(new ColorFormat("&8-------------------- &3Help &8--------------------").format());
				sender.sendMessage(new ColorFormat("&8» &b/stats help &8- &bShow help").format());
				sender.sendMessage(new ColorFormat("&8» &b/stats info [player] &8- &bShow player's stats").format());
				sender.sendMessage(new ColorFormat("&8» &b/stats addCoins <coins> [player] &8- &bAdd coins").format());
				sender.sendMessage(new ColorFormat("&8» &b/stats removeCoins <coins> [player] &8- &bAdd coins").format());
				sender.sendMessage(new ColorFormat("&8» &b/stats addPoints <points> [player] &8- &bAdd points").format());
				sender.sendMessage(new ColorFormat("&8» &b/stats removePoints <points> [player] &8- &bAdd points").format());
				// TODO
			}
			case 2: {
				
			}
		} 
		
	}
}
