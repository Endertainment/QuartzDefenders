package ua.coolboy.quartzdefenders.turrets;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import ua.endertainment.quartzdefenders.QuartzDefenders;

public class TurretInventory implements Listener {

    QuartzDefenders plugin;

    public TurretInventory(QuartzDefenders plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void stand(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked().getName().startsWith("Turret")) {
            event.setCancelled(true);
            if ( //&& event.getPlayer().getScoreboard().getTeam(event.getRightClicked().getMetadata("team").get(0).asString()).hasEntry(player.getName())
                    event.getRightClicked().getMetadata("type").get(0).asString().equals("normal")) {
                Inventory inv = Bukkit.createInventory(player, 9, ChatColor.RED + "Turret");
                event.getPlayer().setMetadata("turretEdit", new FixedMetadataValue(plugin, event.getRightClicked()));

                ItemStack fire = new ItemStack(Material.BLAZE_POWDER, 1);
                ItemMeta firem = fire.getItemMeta();
                firem.setDisplayName("Fire");
                firem.setLore(Arrays.asList("Turret shoots fire arrows", "Required level: 10"));
                fire.setItemMeta(firem);

                ItemStack slow = new ItemStack(Material.PACKED_ICE, 1);
                ItemMeta slowm = slow.getItemMeta();
                slowm.setDisplayName("Slowness");
                slowm.setLore(Arrays.asList("Turret shoots slowness arrows", "Required level: 10"));
                slow.setItemMeta(slowm);

                ItemStack glow = new ItemStack(Material.SPECTRAL_ARROW, 1);
                ItemMeta glowm = glow.getItemMeta();
                glowm.setDisplayName("Glowing");
                glowm.setLore(Arrays.asList("Turret shoots spectral arrow", "Required level: 20"));
                glow.setItemMeta(glowm);

                ItemStack poison = new ItemStack(Material.POTION, 1);
                PotionMeta meta = (PotionMeta) poison.getItemMeta();
                meta.setBasePotionData(new PotionData(PotionType.POISON, false, false));
                meta.setDisplayName("Poison");
                meta.setLore(Arrays.asList("Turret shoots poisoned arrows", "Required level: 15"));
                meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                poison.setItemMeta(meta);

                ItemStack exit = new ItemStack(Material.BARRIER, 1);
                ItemMeta exitm = exit.getItemMeta();
                exitm.setDisplayName("Exit");
                exit.setItemMeta(exitm);

                inv.setItem(0, fire);
                inv.setItem(1, slow);
                inv.setItem(2, poison);
                inv.setItem(3, glow);
                inv.setItem(8, exit);
                player.openInventory(inv);
            }
        }
    }

    @EventHandler
    public void upgrade(InventoryClickEvent event) {
        String message = ChatColor.RED + "You haven't enought experience";
        if (!event.getInventory().getName().equalsIgnoreCase(ChatColor.RED + "Turret")) {
            return;
        }

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.BARRIER || !event.getCurrentItem().hasItemMeta()) {
            player.closeInventory();
        }
        ArmorStand stand = (ArmorStand) event.getWhoClicked().getMetadata("turretEdit").get(0).value();
        if (null != event.getCurrentItem()) {
            switch (event.getCurrentItem().getItemMeta().getDisplayName()) {
                case "Fire":
                    if (player.getLevel() > 10) {
                        player.setLevel(player.getLevel() - 10);
                        stand.setMetadata("type", new FixedMetadataValue(plugin, "fire"));
                        stand.setMetadata("lived", new FixedMetadataValue(plugin, 0));
                        player.closeInventory();
                    } else {
                        player.sendMessage(message);
                    }
                    break;
                case "Poison":
                    if (player.getLevel() > 15) {
                        player.setLevel(player.getLevel() - 15);
                        stand.setMetadata("type", new FixedMetadataValue(plugin, "poison"));
                        stand.setMetadata("lived", new FixedMetadataValue(plugin, 0));
                        player.closeInventory();
                    } else {
                        player.sendMessage(message);
                    }
                    break;
                case "Slowness":
                    if (player.getLevel() > 10) {
                        player.setLevel(player.getLevel() - 15);
                        stand.setMetadata("type", new FixedMetadataValue(plugin, "slow"));
                        stand.setMetadata("lived", new FixedMetadataValue(plugin, 0));
                        player.closeInventory();
                    } else {
                        player.sendMessage(message);
                    }
                    break;
                case "Glowing":
                    if (player.getLevel() > 20) {
                        player.setLevel(player.getLevel() - 15);
                        stand.setMetadata("type", new FixedMetadataValue(plugin, "glow"));
                        stand.setMetadata("lived", new FixedMetadataValue(plugin, 0));
                        player.closeInventory();
                    } else {
                        player.sendMessage(message);
                    }
                    break;
                case "Exit":
                    player.closeInventory();
                    break;
            }
            player.removeMetadata("turretEdit", plugin);
        }
    }
}
