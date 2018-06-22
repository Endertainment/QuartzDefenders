package ua.endertainment.quartzdefenders.events;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class CommandListener implements Listener {
    
    private List<String> blockedCmds;
    
    public CommandListener(QuartzDefenders plugin) {
        blockedCmds = plugin.getConfig().getStringList("blocked_commands");
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage().split(" ")[0].replace("/", "").toLowerCase();
        if(blockedCmds.contains(command) && !e.getPlayer().hasPermission("quartzdefenders.command.bypass_blocked")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("commands.command_blocked")));
        }
    }

}
