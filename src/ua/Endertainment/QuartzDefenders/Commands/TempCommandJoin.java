package ua.Endertainment.QuartzDefenders.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class TempCommandJoin implements CommandExecutor {

	private QuartzDefenders plugin;
	
	public TempCommandJoin(QuartzDefenders quartzDefenders) {
		plugin = quartzDefenders;
		plugin.getCommand("join").setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
	
		return true;
		
	}

}
