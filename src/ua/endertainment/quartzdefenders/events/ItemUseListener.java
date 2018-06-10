package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.gui.AchievementsGUI;
import ua.endertainment.quartzdefenders.gui.GamesGUI;
import ua.endertainment.quartzdefenders.gui.KitsGUI;
import ua.endertainment.quartzdefenders.gui.LobbyShopGUI;
import ua.endertainment.quartzdefenders.gui.StatsGUI;
import ua.endertainment.quartzdefenders.gui.TeamGUI;
import ua.endertainment.quartzdefenders.items.QItems;
import ua.endertainment.quartzdefenders.items.SetupItems;

public class ItemUseListener implements Listener {

    private QuartzDefenders plugin;

    public ItemUseListener(QuartzDefenders plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void itemUse(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        ItemStack i = e.getItem();

        if (i == null || i.getType().equals(Material.AIR)) {
            return;
        }

        if (!i.hasItemMeta()) {
            return;
        }

        if (!i.getItemMeta().hasDisplayName()) {
            return;
        }

        if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            String compass = QItems.itemGamesChoose().getItemMeta().getDisplayName();
            String stats = QItems.itemStats().getItemMeta().getDisplayName();
            String kits = QItems.itemKitsChoose().getItemMeta().getDisplayName();
            String quit = QItems.itemQuit().getItemMeta().getDisplayName();
            String show = QItems.itemHidePlayers(true).getItemMeta().getDisplayName();
            String hide = QItems.itemHidePlayers(false).getItemMeta().getDisplayName();
            String lShop = QItems.itemLobbyShop().getItemMeta().getDisplayName();

            String setupOres = SetupItems.itemSetupOres().getItemMeta().getDisplayName();
            String setupSignsK = SetupItems.itemSetupSignsK().getItemMeta().getDisplayName();
            String setupSignsW = SetupItems.itemSetupSignsW().getItemMeta().getDisplayName();
            String achievements = QItems.itemAchievements().getItemMeta().getDisplayName();

            if (i.getItemMeta().getDisplayName().equalsIgnoreCase(compass)) {
                e.setCancelled(true);
                new GamesGUI(plugin).openInventory(p);
                return;
            }

            if (i.getItemMeta().getDisplayName().equalsIgnoreCase(stats)) {
                e.setCancelled(true);
                new StatsGUI(p).openInventory();
                return;
            }

            if (i.getItemMeta().getDisplayName().equalsIgnoreCase(achievements)) {
                e.setCancelled(true);
                new AchievementsGUI(p).openInventory();
                return;
            }

            if (i.getItemMeta().getDisplayName().equalsIgnoreCase(hide)) {
                e.setCancelled(true);
                plugin.getLobby().hidePlayers(p);
                p.getInventory().setItemInMainHand(QItems.itemHidePlayers(true));
                return;
            }

            if (i.getItemMeta().getDisplayName().equalsIgnoreCase(show)) {
                e.setCancelled(true);
                plugin.getLobby().showPlayers(p);
                p.getInventory().setItemInMainHand(QItems.itemHidePlayers(false));
                return;
            }

            if (i.getItemMeta().getDisplayName().equalsIgnoreCase(kits)) {
                e.setCancelled(true);
                new KitsGUI(p).openInventory();
                return;
            }

            if (i.getItemMeta().getDisplayName().equalsIgnoreCase(lShop)) {
                e.setCancelled(true);
                new LobbyShopGUI(p).openInventory();
                return;
            }

            if (i.getItemMeta().getDisplayName().equalsIgnoreCase(setupSignsK)) {
                e.setCancelled(true);
                Block b = e.getClickedBlock();
                if (!(b.getState() instanceof Sign)) {
                    return;
                }

                plugin.getLobby().addSignK(b.getLocation(), p);
                return;
            }

            if (i.getItemMeta().getDisplayName().equalsIgnoreCase(setupSignsW)) {
                e.setCancelled(true);
                Block b = e.getClickedBlock();
                if (!(b.getState() instanceof Sign)) {
                    return;
                }

                plugin.getLobby().addSignW(b.getLocation(), p);
                return;
            }

            if (plugin.getGame(p) != null) {
                String teams = QItems.itemTeamChoose().getItemMeta().getDisplayName();

                Game game = plugin.getGame(p);

                if (i.getItemMeta().getDisplayName().equalsIgnoreCase(teams)) {
                    e.setCancelled(true);
                    new TeamGUI(game, p).openInventory();
                    return;
                }

                if (i.getItemMeta().getDisplayName().equalsIgnoreCase(setupOres)) {
                    e.setCancelled(true);
                    Block b = e.getClickedBlock();
                    game.addRegenerativeBlock(b, p);
                    return;
                }

            }

            if (i.getItemMeta().getDisplayName().equalsIgnoreCase(quit)) {
                e.setCancelled(true);
                plugin.getGame(p).quitGame(plugin.getGamePlayer(p));
                return;
            }

        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e) {
        ItemStack i = e.getItemDrop().getItemStack();
        Player p = e.getPlayer();
        if (!i.hasItemMeta()) {
            return;
        }

        if (i.getItemMeta().getDisplayName() != null
                && (i.getItemMeta().getDisplayName().equals(QItems.itemGamesChoose().getItemMeta().getDisplayName())
                || i.getItemMeta().getDisplayName().equals(QItems.itemStats().getItemMeta().getDisplayName())
                || i.getItemMeta().getDisplayName().equals(QItems.itemKitsChoose().getItemMeta().getDisplayName())
                || i.getItemMeta().getDisplayName().equals(QItems.itemQuit().getItemMeta().getDisplayName())
                || i.getItemMeta().getDisplayName().equals(QItems.itemAchievements().getItemMeta().getDisplayName())
                || i.getItemMeta().getDisplayName().equals(QItems.itemHidePlayers(true).getItemMeta().getDisplayName())
                || i.getItemMeta().getDisplayName().equals(QItems.itemHidePlayers(false).getItemMeta().getDisplayName())
                || i.getItemMeta().getDisplayName().equals(QItems.itemLobbyShop().getItemMeta().getDisplayName()))) {
            e.setCancelled(true);
            return;
        }

        if (plugin.getGame(p) != null) {
            if (i.getItemMeta().getDisplayName()!= null 
                    && i.getItemMeta().getDisplayName().equals(QItems.itemTeamChoose().getItemMeta().getDisplayName())) {
                e.setCancelled(true);
            }
        }

    }
}
