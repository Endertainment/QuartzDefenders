package ua.endertainment.quartzdefenders.commands.kit;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class CommandKit implements CommandExecutor {

	private QuartzDefenders plugin;
	
	public CommandKit(QuartzDefenders plugin) {
		this.plugin = plugin;
		PluginCommand cmd = this.plugin.getCommand("kit");
		cmd.setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(args.length == 0) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Check command usage: &b/kit help"));
			return true;
		}
		
		SubCommand subCommand = CommandKitManager.getInstance().find(args[0]);
		if(subCommand == null) {
			subCommand = CommandKitManager.getInstance().find("help");
		}
		
		ArrayList<String> newArgs = new ArrayList<>();
		
		for(int i = 0; i < args.length; i++) {
			if(i == 0) {
				continue;
			}
			newArgs.add(args[i]);
		}
		
		subCommand.execute(sender, newArgs.toArray(new String[0]));
		
		return true;
	}
	
}
