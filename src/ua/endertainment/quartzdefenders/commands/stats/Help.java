package ua.endertainment.quartzdefenders.commands.stats;

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
		} catch(NumberFormatException ex) {
			page = 1;
		}
		sendHelp(page, sender);
	}

	private final int inPageLines = 6;
	
	private void sendHelp(int page, CommandSender sender) {		
		
		LinkedHashMap<String, SubCommand> map = CommandStatsManager.getCommands();
		int pages = (int)Math.ceil(map.size() / (double)inPageLines);
		page = page >= pages ? pages : page;
		int p = page-1;
		int i_b = p*inPageLines;
		int i_e = p*inPageLines+inPageLines >= map.size() ? map.size() : p*inPageLines+inPageLines;
		sender.sendMessage(new ColorFormat("&8----------------- &3Help &8(&3" + page + "&8/&3" + pages + "&8) -----------------").format());		
		for (int i = i_b; i < i_e; i++) {
			sender.sendMessage(map.get(map.keySet().toArray()[i]).getUsage());
		}
		/*
		switch(page) {
			case 1: {
				sender.sendMessage(new ColorFormat("&8-------------------- &3Help &8--------------------").format());
				sender.sendMessage(new ColorFormat("&8� &b/stats help &8- &bShow help").format());
				sender.sendMessage(new ColorFormat("&8� &b/stats info [player] &8- &bShow player's stats").format());
				sender.sendMessage(new ColorFormat("&8� &b/stats addCoins <coins> [player] &8- &bAdd coins").format());
				sender.sendMessage(new ColorFormat("&8� &b/stats removeCoins <coins> [player] &8- &bAdd coins").format());
				sender.sendMessage(new ColorFormat("&8� &b/stats addPoints <points> [player] &8- &bAdd points").format());
				sender.sendMessage(new ColorFormat("&8� &b/stats removePoints <points> [player] &8- &bAdd points").format());
				// TODO
				break;
			}
			case 2: {
				
			}
		} 
		*/
	}

	@Override
	public String getUsage() {
		return new ColorFormat("&8� &b/stats help &8- &bShow help").format();
	}
}
