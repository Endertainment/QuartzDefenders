package ua.endertainment.quartzdefenders.commands.stats;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.stats.StatsPlayer;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.LobbySidebar;
import ua.endertainment.quartzdefenders.utils.Symbols;

public class SetKills extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission(PermissionsList.STATS_SET_KILLS)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(args.length == 0) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "Check command usage: &b/stats help"));
			return;
		}
		
		int kills = 0;
		
		try {
			kills = Integer.parseInt(args[0]);		
		} catch(NumberFormatException e) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", args[0] + "&7 is not a valid number"));
			return;
		}
		
		kills = Math.max(0, kills);
		
		Player p = null;
		
		if(args.length >= 2) {
			p = Bukkit.getPlayer(args[1]);
			if(p == null) {
				sender.sendMessage(LoggerUtil.gameMessage("Chat", "Player " + args[0] + "&7 is not online"));
				return;
			}
		} 
		
		if(args.length == 1) {
			if(sender instanceof Player) {
				p = (Player) sender;
			} else {
				sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou are not a player"));
				return;
			}
		}
		
		StatsPlayer sp = new StatsPlayer(p);
		sp.setKills(kills);
		sender.sendMessage(LoggerUtil.gameMessage("Stats", "&6" + kills + "&7 kills set for player &6" + sp.getPlayer().getName()));
		
		if(p.getWorld() == QuartzDefenders.getInstance().getLobby().getWorld()) {
			LobbySidebar sb = new LobbySidebar(QuartzDefenders.getInstance(), p);
			sb.setScoreboard();
		}
	}

	@Override
	public String getUsage() {
		return new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &b/stats setKills <kills> [player] &8- &bSet amount of kills").format();
	}	

}
