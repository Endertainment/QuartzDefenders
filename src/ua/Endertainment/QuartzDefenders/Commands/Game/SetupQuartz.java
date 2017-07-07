package ua.Endertainment.QuartzDefenders.Commands.Game;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;

public class SetupQuartz extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.setup.quartz")) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}	
		
		if(args.length == 0) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "Check command usage: &b/game help"));
			return;
		}
		
		
		Player p = (Player) sender;
		
		Game game = QuartzDefenders.getInstance().getGame(p);
		
		if(game == null) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cYou can not join to team when you is not in game"));
			return;
		}
		
		Block b = null;
		
		if(args.length == 1) {
			b = p.getTargetBlock((Set<Material>) null, 200);			
		}
		
		if(args.length >= 4) {
			b = game.getGameWorld().getBlockAt(new Location(game.getGameWorld(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3])));
		}
		
		game.setQuartz(b, args[0], p);
		
	}

	
	
}
