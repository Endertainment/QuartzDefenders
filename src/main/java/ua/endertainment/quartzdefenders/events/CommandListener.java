package ua.endertainment.quartzdefenders.events;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class CommandListener implements Listener {
    
    private Map<String, String> blockedCmds = new HashMap<>();
    
    public CommandListener(QuartzDefenders plugin) {
    	for(String s : plugin.getConfig().getStringList("blocked_commands")) {    		
    		blockedCmds.put(s.contains(",") ? s.split(",")[0] : s, s.contains(",") ? s.split(",")[1] : "minecraft");
    	}
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String commandInput = e.getMessage().split(" ")[0].replace("/", "").toLowerCase();
        for(String cmd : blockedCmds.keySet()) {
        	if(cmd.equalsIgnoreCase(commandInput) || commandInput.equalsIgnoreCase(blockedCmds.get(cmd) + ":" + cmd)) {
        		e.setCancelled(true);
        		e.getPlayer().sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("commands.command_blocked")));
        	}
        }
    }

}
