package ua.endertainment.quartzdefenders.commands.stats;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.gui.StatsGUI;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class Info extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player p = null;
		
		if(sender instanceof ConsoleCommandSender) {
			// TODO
			return;
		} else if(sender instanceof Player) {
			p = (Player) sender;
		}
		
		if(args.length == 0) {
			new StatsGUI(p).openInventory();
			return;
		}
		
		if(!sender.hasPermission(PermissionsList.STATS_SEE_INFO)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(args.length >= 1) {
			Player target = Bukkit.getPlayer(args[0]);
			if(target == null) {
				sender.sendMessage(LoggerUtil.gameMessage("Chat", "Player " + args[0] + " &7is not online"));
				return;
			}
			new StatsGUI(p).openInventory((Player) sender);
			return;
		}
		
	}

	@Override
	public String getUsage() {
		return new ColorFormat("&8» &b/stats info [player] &8- &bShow player's stats").format();
	}

	
	
}
