package ua.endertainment.quartzdefenders.commands.game;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.items.SetupItems;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class SetupOres extends SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission(PermissionsList.GAME_SETUP_BLOCKS)) {
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

	@Override
	public String getUsage() {
		return new ColorFormat("&8» &b/game setupOres &8- &bAdd new regenerative ores").format();
	}

}
