package ua.endertainment.quartzdefenders.commands.game;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class SetupQuartz extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission(PermissionsList.GAME_SETUP_QUARTZ)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}	
		
		if(args.length == 0) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Check command usage: &b/game help"));
			return;
		}
		
		
		Player p = (Player) sender;
		
		Game game = QuartzDefenders.getInstance().getGame(p);
		
		if(game == null) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou can not use this command when you is not in game"));
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

	@Override
	public String getUsage() {
		return new ColorFormat("&8» &b/game setupQuartz <team> [x] [y] [z] &8- &bSet quartz for team at x,y,z").format();
	}

	
	
}
