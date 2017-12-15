package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ua.endertainment.quartzdefenders.game.Balance;
import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.gui.StatsGUI;
import ua.endertainment.quartzdefenders.items.QItems;
import ua.endertainment.quartzdefenders.kits.Kit;
import ua.endertainment.quartzdefenders.kits.KitsManager;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.Replacer;

public class InvClickEvent implements Listener {

    private QuartzDefenders plugin;

    public InvClickEvent(QuartzDefenders plugin) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e) {

        Inventory inv = e.getInventory();

        if (e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) {
            return;
        }

        if (!e.getCurrentItem().hasItemMeta()) {
            return;
        }

        ItemStack curr = e.getCurrentItem();

        Player p = (Player) e.getWhoClicked();

        /*
		 * GAMES
         */
        if (inv.getName().equals(Language.getString("GUI.games.name"))) {
            e.setCancelled(true);
            p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);

            for (Game game : plugin.getGames()) {
                if (curr.getItemMeta().getDisplayName().equals(game.getColorWorldName())) {
                    if (!game.containsPlayer(plugin.getGamePlayer(p))) {
                        game.joinGame(plugin.getGamePlayer(p));
                    } else {
                        game.quitGame(plugin.getGamePlayer(p));
                    }
                    p.closeInventory();
                    break;
                }
            }
            return;
        }

        /*
		 * STATS
         */
        if (inv.getName().equals(new StatsGUI(p).getInventory().getName())) {
            e.setCancelled(true);
            p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);

            return;
        }
        /*
		 * SHOP
         */
        if (inv.getName().equals(Language.getString("GUI.shop.name"))) {
            e.setCancelled(true);
            KitsManager m = plugin.getKitManager();
            p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);
            for (Kit kit : m.getKitsRegistry().keySet()) {
                if (curr.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', kit.getDisplayName()))) {
                        m.buyKit(kit, p);
                        p.closeInventory();
                }
            }

            return;
        }

        /*
		 * KITS
         */
        if (inv.getName().equals(Language.getString("GUI.kits.name"))) {
            e.setCancelled(true);
            KitsManager m = plugin.getKitManager();
            p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);
            GamePlayer gp = plugin.getGamePlayer(p);
            for (Kit kit : m.getKitsRegistry().keySet()) {
                if (curr.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', kit.getDisplayName()))) {
                        m.chooseKit(kit, plugin.getGame(p), plugin.getGamePlayer(p));
                        p.closeInventory();
                }
            }

            return;
        }

        /*
		 * ACHIEVEMENTS
         */
        if (inv.getName().equals(Language.getString("GUI.achievements.name"))) {
            e.setCancelled(true);
            p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);
            return;
        }

        /*
		 * TEAMS
         */
        if (inv.getName().equals(Language.getString("GUI.teams.name"))) {
            e.setCancelled(true);

            p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);

            Game game = plugin.getGame(p);

            if (curr.getItemMeta().getDisplayName().equalsIgnoreCase(Language.getString("GUI.teams.item_leave"))) {

                if (game.getTeam(p) != null) {
                    game.getTeam(p).quitTeam(plugin.getGamePlayer(p));
                    game.refreshScoreboard();
                }
                p.closeInventory();
                return;

            }

            for (GameTeam team : game.getTeams().values()) {

                if (curr.getItemMeta().getDisplayName().equals(Language.getString("GUI.teams.item_name", new Replacer("{c}", team.getColor() + ""), new Replacer("{0}", team.getName())))) {

                    if (new Balance(game, game.getBalanceType(), plugin.getGamePlayer(p), game.getTeams().values(), team).isTeamsBalanced()) {

                        team.joinTeam(plugin.getGamePlayer(p), false);
                        game.refreshScoreboard();
                    }

                    p.closeInventory();
                    return;
                }

            }
            return;
        }

        /*
                Admin panel
         */
        if (inv.getName().equals(Language.getString("GUI.admin.name"))) {
            e.setCancelled(true);
            String id = inv.getItem(4).getItemMeta().getLore().get(0).split(":")[1].substring(1);
            Game game = QuartzDefenders.getInstance().getGame(id);
            if(game==null) return;
            p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);
            if (curr.getItemMeta().getDisplayName().equals(Language.getString("admin.start_game"))) {
                game.startCountdown();
            }
            if (curr.getItemMeta().getDisplayName().equals(Language.getString("admin.stop_game"))) {
                game.endGame();
            }
            p.closeInventory();
            return;
        }
        /*
		 * Player's inventory
         */
        if (!curr.getItemMeta().hasDisplayName()) {
            return;
        }
        for (ItemStack item : QItems.getAllTechItems()) {
            if (curr.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
                e.setCancelled(true);
                return;
            }

        }

    }
}
