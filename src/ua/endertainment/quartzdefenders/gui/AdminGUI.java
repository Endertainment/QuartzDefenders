package ua.endertainment.quartzdefenders.gui;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.utils.ItemUtil;
import ua.endertainment.quartzdefenders.utils.Language;

public class AdminGUI {
    
    Game game;
    String title;
    Inventory inventory;

    public AdminGUI(Game game) {
        this.game = game;
        
        this.title = Language.getString("GUI.admin.name");
	this.inventory = Bukkit.createInventory(new QuartzInventoryHolder(), 9*1, title);
        generateInventory();
    }
    
    public void openInventory(Player player) {
        if(player.hasPermission("QuartzDefenders.game.admin")) {
            player.openInventory(inventory);
        }
    }
    
    private void generateInventory() {
        ItemStack start = ItemUtil.newItem(Language.getString("admin.start_game"), Material.DIAMOND_SWORD, 1, 0);
        ItemStack info = ItemUtil.newItem(Language.getString("admin.info.name"), Material.OAK_SIGN, 1, 0);
        ItemUtil.setLore(info, getGameInfo());
        ItemStack stop = ItemUtil.newItem(Language.getString("admin.stop_game"), Material.BARRIER, 1, 0);
        inventory.setItem(0, start);
        inventory.setItem(4, info);
        inventory.setItem(8, stop);
    }
    
    private List<String> getGameInfo() {
        List<String> list = new ArrayList<>();
        list.add(Language.getString("admin.info.gameid")+" " + game.getGameId());
        list.add(Language.getString("admin.info.players")+" " + game.getPlayers().size());
        list.add(Language.getString("admin.info.state")+" " + game.getGameState().toString());
        list.add(Language.getString("admin.info.quartz")+" " + fancyQuartz());
        if(game.isGameState(Game.GameState.ACTIVE) || game.isGameState(Game.GameState.ENDING)) {
        list.add(Language.getString("admin.info.started")+" " + DateFormat.getTimeInstance().format(game.getStartTime().getTime()));
        }
        return list;
    }
    
    private String fancyQuartz() {
        String hp="";
        for(GameTeam team : game.getTeams().values()) {
            int health = game.getQuartz(team).getQuartzHealth();
            hp += ""+team.getColor() + health + " ";
        }
        return hp;
    }
    
}
