package ua.endertainment.quartzdefenders.Commands.Kit;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.kits.Kit;
import ua.endertainment.quartzdefenders.kits.KitsManager;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class Give extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.kit.give")) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}
		
		if(args.length == 0) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Check command usage: &b/kit help"));
			return;
		}
		
		Kit kit = null;
		Player p = null;
		
		kit = KitsManager.getInstance().getKit(args[0]);
		
		if(kit == null) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Kit " + args[0] + "&7 is not exist"));
			return;
		}
		
		if(args.length >= 2) {
			p = Bukkit.getPlayer(args[1]);
			if(p == null) {
				sender.sendMessage(LoggerUtil.gameMessage("Chat", "Player " + args[1] + "&7 is not online"));
				return;
			}
		} else {
			p = (Player) sender;
		}
		
		KitsManager.getInstance().giveKit(kit, p);
		sender.sendMessage(LoggerUtil.gameMessage("Kits", "You give kit " + kit.getDisplayName() + "&7 to player " + p.getDisplayName()));
		return;
	}

}
