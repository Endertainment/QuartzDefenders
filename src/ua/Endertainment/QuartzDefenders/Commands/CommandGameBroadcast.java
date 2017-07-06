package ua.Endertainment.QuartzDefenders.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;

public class CommandGameBroadcast implements CommandExecutor {

	private QuartzDefenders plugin;
	
	public CommandGameBroadcast(QuartzDefenders plugin) {
		this.plugin = plugin;
		PluginCommand cmd = this.plugin.getCommand("gamebroadcast");
		cmd.setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.game.broadcast")) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cYou do not have permissions"));
			return true;
		}
		
		if(args.length == 0) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "Command usage: &b/" + label + " <message>"));
			return true;
		}
		
		String message = "";
		
		for(String s : args) {
			message += s + " ";
		} 
		
		Bukkit.broadcastMessage(new ColorFormat(GameMsg.getPrefix() + message).format());
		
		return true;
	}
	
	
	
}
