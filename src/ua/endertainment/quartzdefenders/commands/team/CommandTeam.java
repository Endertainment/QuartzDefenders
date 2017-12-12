package ua.endertainment.quartzdefenders.Commands.Team;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.gui.TeamGUI;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

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
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Check command usage: &b/team help"));			
			
			if(!(sender instanceof Player)) {
				return true;
			}
			Player p = (Player) sender;
			Game game = QuartzDefenders.getInstance().getGame(p);
			
			if(game == null) {
				sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou can not join to team when you is not in game"));
				return true;
			}		
			
			new TeamGUI(game, p).openInventory();
			
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
