package ua.endertainment.quartzdefenders.Commands.Game;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.gui.AdminGUI;
import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class Admin extends SubCommand {
    
    @Override
    public void execute(CommandSender sender, String[] args) {
        
        if (!(sender instanceof Player)) {
            LoggerUtil.logInfo("This command only for players");
        }
        
        if (!sender.hasPermission("QuartzDefenders.game.admin")) {
            sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
            return;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            Game game = QuartzDefenders.getInstance().getGame(player);
            if(game != null) {
                new AdminGUI(game).openInventory(player);
            }
            return;
        }
        
        Game game = QuartzDefenders.getInstance().getGame(args[0]);
        if(game != null) {
            new AdminGUI(game).openInventory(player);
        }
    }
    
}
