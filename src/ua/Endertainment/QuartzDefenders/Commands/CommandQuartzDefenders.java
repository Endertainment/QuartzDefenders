package ua.Endertainment.QuartzDefenders.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;

public class CommandQuartzDefenders implements CommandExecutor {

	private QuartzDefenders plugin;
	
	public CommandQuartzDefenders(QuartzDefenders plugin) {
		this.plugin = plugin;
		PluginCommand cmd = this.plugin.getCommand("quartzdefenders");
		cmd.setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {

		if(args.length == 0) {
			sender.sendMessage(new ColorFormat("&8» &dQuartz&5Defenders &fplugin developed by &bEndertainment & Cool_boy &ffor &bPlayCraft.COM.UA").format());
			return true;
		}
		
		if(args.length >= 1 || args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(new ColorFormat("&8» &b/game help").format());
			sender.sendMessage(new ColorFormat("&8» &b/stats help").format());
			sender.sendMessage(new ColorFormat("&8» &b/team help").format());
			sender.sendMessage(new ColorFormat("&8» &b/gamebroadcast").format());
			
		}
		
		return true;
	}
	
}
