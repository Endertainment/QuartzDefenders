package ua.Endertainment.QuartzDefenders.Commands.Kit;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Kits.Kit;
import ua.Endertainment.QuartzDefenders.Kits.KitsManager;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;

public class Give extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.kit.give")) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}
		
		if(args.length == 0) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "Check command usage: &b/kit help"));
			return;
		}
		
		Kit kit = null;
		Player p = null;
		
		kit = KitsManager.getInstance().getKit(args[0]);
		
		if(kit == null) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "Kit " + args[0] + "&7 is not exist"));
			return;
		}
		
		if(args.length >= 2) {
			p = Bukkit.getPlayer(args[1]);
			if(p == null) {
				sender.sendMessage(GameMsg.gameMessage("Chat", "Player " + args[1] + "&7 is not online"));
				return;
			}
		} else {
			p = (Player) sender;
		}
		
		KitsManager.getInstance().giveKit(kit, p);
		sender.sendMessage(GameMsg.gameMessage("Kits", "You give kit " + kit.getName() + "&7 to player " + p.getDisplayName()));
		return;
	}

}
