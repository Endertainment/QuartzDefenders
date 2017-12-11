package ua.Endertainment.QuartzDefenders.Commands.Game;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.GUI.AdminGUI;
import ua.Endertainment.QuartzDefenders.Game.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;

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
