package ua.Endertainment.QuartzDefenders.Commands.Team;

import org.bukkit.command.CommandSender;

import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;

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
				sender.sendMessage(new ColorFormat("&8» &b/team help &8- &bShow help").format());
				sender.sendMessage(new ColorFormat("&8» &b/team join <team> [player] &8- &bJoin to team").format());
				sender.sendMessage(new ColorFormat("&8» &b/team quit|leave|kick [team] [player] &8- &bLeave from team").format());
				sender.sendMessage(new ColorFormat("&8» &b/team info <team> &8- &bShow team's info").format());
				sender.sendMessage(new ColorFormat("&8» &b/team lock <team> &8- &bDisable to join in team").format());
			}
			case 2: {
				
			}
		} 
		
	}
	
}
