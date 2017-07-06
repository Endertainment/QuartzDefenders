package ua.Endertainment.QuartzDefenders.Commands.Kit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class CommandKit implements CommandExecutor {

	private QuartzDefenders plugin;
	
	public CommandKit(QuartzDefenders plugin) {
		this.plugin = plugin;
		PluginCommand cmd = plugin.getCommand("kit");
		cmd.setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		
		return true;
	}
	
}
