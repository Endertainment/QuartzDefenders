package ua.Endertainment.QuartzDefenders.Commands.Stats;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.GUI.StatsGUI;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;

public class Info extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}
		if(args.length == 0) {
			Player p = (Player) sender;
			new StatsGUI(p).openInventory();
			return;
		}
		
		if(!sender.hasPermission("QuartzDefenders.stats.seeInfo")) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(args.length >= 1) {
			Player p = Bukkit.getPlayer(args[0]);
			if(p == null) {
				sender.sendMessage(LoggerUtil.gameMessage("Chat", "Player " + args[0] + " &7is not online"));
				return;
			}
			new StatsGUI(p).openInventory((Player) sender);
			return;
		}
		
	}

	
	
}
