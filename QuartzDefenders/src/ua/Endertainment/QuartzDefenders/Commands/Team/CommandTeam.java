package ua.Endertainment.QuartzDefenders.Commands.Team;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;

public class CommandTeam implements CommandExecutor {

	private QuartzDefenders plugin;
	
	public CommandTeam(QuartzDefenders plugin) {
		this.plugin = plugin;
		PluginCommand cmd = this.plugin.getCommand("team");
		cmd.setExecutor(this);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length == 0) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "Check command usage: &b/team help"));
			return true;
		}
		
		SubCommand subCommand = CommandTeamManager.getInstance().find(args[0]);
		if(subCommand == null) {
			subCommand = CommandTeamManager.getInstance().find("help");
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
