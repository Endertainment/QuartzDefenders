package ua.endertainment.quartzdefenders.Commands.Game;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.items.SetupItems;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class SetupOres extends SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("QuartzDefenders.setup.regenerativeBlocks")) {
            sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
            return;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cOnly players can use this command"));
            return;
        }

        Player p = (Player) sender;

        p.getInventory().clear();

        p.getInventory().setItem(0, SetupItems.itemSetupOres());
    }

}
