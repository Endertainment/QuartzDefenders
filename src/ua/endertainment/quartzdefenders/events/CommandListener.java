package ua.endertainment.quartzdefenders.events;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class CommandListener implements Listener {
    
    private List<String> blockedCmds;
    
    public CommandListener(QuartzDefenders plugin) {
        blockedCmds = new ArrayList<>();
        blockedCmds.add("me");
        blockedCmds.add("tell");
        blockedCmds.add("w");
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    public void onCommand(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage().split(" ")[0].replace("/", "");
        if(blockedCmds.contains(command) && !e.getPlayer().hasPermission("quartzdefenders.command.bypass_blocked")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(LoggerUtil.getPrefix()+ChatColor.RED+"This command is blocked!");
        }
    }

}
