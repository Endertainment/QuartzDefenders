package ua.endertainment.quartzdefenders.commands;

import org.bukkit.command.CommandSender;

public abstract class SubCommand {

	public abstract void execute(CommandSender sender, String[] args);
        
	public abstract String getUsage();
	
}
