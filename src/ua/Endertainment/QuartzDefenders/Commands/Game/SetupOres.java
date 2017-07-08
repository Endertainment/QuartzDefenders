package ua.Endertainment.QuartzDefenders.Commands.Game;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Items.SetupItems;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;

public class SetupOres extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.setup.regenerativeBlocks")) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}	
		
		Player p = 	(Player) sender;
		
		p.getInventory().clear();
		
		p.getInventory().setItem(0, SetupItems.itemSetupOres());
	}

	
	
}
