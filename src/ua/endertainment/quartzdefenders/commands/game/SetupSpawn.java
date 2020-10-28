package ua.endertainment.quartzdefenders.commands.game;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class SetupSpawn extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission(PermissionsList.GAME_SETUP_SPAWN)) {
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
		
		Location loc = null;
		
		if(args.length == 1) {
			loc = p.getLocation();
		}
		
		if(args.length >= 4) {
			loc = new Location(game.getGameWorld(), Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
			if(args.length > 4) loc.setYaw(Float.parseFloat(args[4]));
			if(args.length > 5) loc.setPitch(Float.parseFloat(args[5]));
		}
		
		game.setSpawn(loc, args[0], p);
		
	}

	@Override
	public String getUsage() {
		return new ColorFormat("&8» &b/game setupSpawn <team> [x] [y] [z] [yaw] [pitch] &8- &bSet spawn").format();
	}
	
}
