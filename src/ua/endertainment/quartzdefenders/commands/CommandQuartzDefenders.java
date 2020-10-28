package ua.endertainment.quartzdefenders.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.items.SetupItems;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.FireworkUtil;

public class CommandQuartzDefenders implements CommandExecutor {

	private QuartzDefenders plugin;
	
	public CommandQuartzDefenders(QuartzDefenders plugin) {
		this.plugin = plugin;
		PluginCommand cmd = this.plugin.getCommand("quartzdefenders");
		cmd.setExecutor(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {

		if(args.length == 0) {
			sender.sendMessage(new ColorFormat("&8» &bQuartz&3Defenders &fplugin developed by &bEndertainment & Cool_boy &ffor &bPlayCraft.COM.UA").format());
			return true;
		}
		
		if(args.length >= 1 && args[0].equalsIgnoreCase("help")) {
			sender.sendMessage(new ColorFormat("&8» &b/game help").format());
			sender.sendMessage(new ColorFormat("&8» &b/stats help").format());
			sender.sendMessage(new ColorFormat("&8» &b/team help").format());
			sender.sendMessage(new ColorFormat("&8» &b/gamebroadcast").format());
			return true;
		}
		
		if(!(sender instanceof Player)) {
			return true;
		}
		
		Player p = (Player) sender;
		/*
        if(args.length>=1 && args[0].equalsIgnoreCase("debug")) {
       		Info.setKills(p,900);
        }
                
		if(args.length >= 1 && args[0].equalsIgnoreCase("lobbyTools")) {
			
			plugin.getLobby().setLobbyTools(p);
			return true;
			
		}
		*/
		if(args.length >= 1 && args[0].equalsIgnoreCase("firework")) {
			if(!p.hasPermission(PermissionsList.FUN_FIREWORK)) {
				return true;
			}
			long detonate = 2;
			
			int amount = 1;
			
			if(args.length >= 2) detonate = Long.parseLong(args[1]); 
			
			if(args.length >= 3) amount = Math.min(Integer.parseInt(args[2]), 20);
			
			for(int i = 1; i <= amount; i++) {
				FireworkUtil.spawn(p.getLocation(), detonate);
			}
			
			return true;
		}
		if(args.length >= 1 && args[0].equalsIgnoreCase("setupSigns")) {
			if(!p.hasPermission(PermissionsList.LOBBY_SETUP_SIGNS)) {
				return true;
			}
			p.getInventory().setItem(1, SetupItems.itemSetupSignsK());
			p.getInventory().setItem(2, SetupItems.itemSetupSignsW());
		}
		if(args.length >= 2 && args[0].equalsIgnoreCase("setDisplayName")) {
			if(!p.hasPermission(PermissionsList.FUN_SET_NAME)) {
				return true;
			}
			
			Player targ = p;
			
			if(args.length >= 3) {
				targ = Bukkit.getPlayer(args[2]);
			}
			
			plugin.getGamePlayer(targ).setDisplayName(ChatColor.valueOf(args[1]));
			
			return true;
			
		}
                
        if(args.length >= 2 && args[0].equalsIgnoreCase("setTabName")) {
			if(!p.hasPermission(PermissionsList.FUN_SET_TAB_NAME)) {
				return true;
			}
			
			Player targ = p;
			
			if(args.length >= 3) {
				targ = Bukkit.getPlayer(args[2]);
			}
			if(targ == null) return false;
			targ.setPlayerListName(new ColorFormat(args[1]).format().replaceAll("_", " "));
			
			return true;
			
		}
		
		if(args.length >= 1 && args[0].equalsIgnoreCase("removeSigns")) {
			if(!p.hasPermission(PermissionsList.LOBBY_REMOVE_SIGNS)) {
				return true;
			}
			
			plugin.getLobby().removeSigns();
			
			return true;
		}
		
		return true;
	}
	
}
