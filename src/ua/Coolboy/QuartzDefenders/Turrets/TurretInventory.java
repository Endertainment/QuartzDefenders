package ua.Coolboy.QuartzDefenders.Turrets;

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
import ua.Endertainment.QuartzDefenders.QuartzDefenders;


public class TurretInventory implements Listener {
    QuartzDefenders plugin;
    
    public TurretInventory(QuartzDefenders plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void stand(PlayerArmorStandManipulateEvent event) {
        Player player = event.getPlayer();
        if (event.getRightClicked().getName().startsWith("Турель"))
        {
            event.setCancelled(true);
        if (
                //&& event.getPlayer().getScoreboard().getTeam(event.getRightClicked().getMetadata("team").get(0).asString()).hasEntry(player.getName())
                event.getRightClicked().getMetadata("type").get(0).asString().equals("normal")) {
            Inventory inv = Bukkit.createInventory(player, 9, ChatColor.RED + "Турель");
            event.getPlayer().setMetadata("turretEdit", new FixedMetadataValue(plugin, event.getRightClicked()));

            ItemStack fire = new ItemStack(Material.BLAZE_POWDER, 1);
            ItemMeta firem = fire.getItemMeta();
            firem.setDisplayName("Вогонь");
            firem.setLore(Arrays.asList("Створює запалені стріли", "Вартість: 10 рівнів"));
            fire.setItemMeta(firem);

            ItemStack slow = new ItemStack(Material.PACKED_ICE, 1);
            ItemMeta slowm = slow.getItemMeta();
            slowm.setDisplayName("Заповільнення");
            slowm.setLore(Arrays.asList("Створює заповільнюючі стріли", "Вартість: 10 рівнів"));
            slow.setItemMeta(slowm);

            ItemStack glow = new ItemStack(Material.SPECTRAL_ARROW, 1);
            ItemMeta glowm = glow.getItemMeta();
            glowm.setDisplayName("Підсвічування");
            glowm.setLore(Arrays.asList("Створює спектральні стріли", "Вартість: 20 рівнів"));
            glow.setItemMeta(glowm);

            ItemStack poison = new ItemStack(Material.POTION, 1);
            PotionMeta meta = (PotionMeta) poison.getItemMeta();
            meta.setBasePotionData(new PotionData(PotionType.POISON, false, false));
            meta.setDisplayName("Отрута");
            meta.setLore(Arrays.asList("Створює отруєні стріли", "Вартість: 15 рівнів"));
            meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            poison.setItemMeta(meta);

            ItemStack exit = new ItemStack(Material.BARRIER, 1);
            ItemMeta exitm = exit.getItemMeta();
            exitm.setDisplayName("Вихід");
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
        String message = ChatColor.RED + "У вас недостатньо досвіду";
        if (!event.getInventory().getName().equalsIgnoreCase(ChatColor.RED + "Турель")) {
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
                case "Вогонь":
                    if (player.getLevel() > 10) {
                        player.setLevel(player.getLevel() - 10);
                        stand.setMetadata("type", new FixedMetadataValue(plugin, "fire"));
                        stand.setMetadata("lived", new FixedMetadataValue(plugin, 0));
                        player.closeInventory();
                    } else {
                        player.sendMessage(message);
                    }
                    break;
                case "Отрута":
                    if (player.getLevel() > 15) {
                        player.setLevel(player.getLevel() - 15);
                        stand.setMetadata("type", new FixedMetadataValue(plugin, "poison"));
                        stand.setMetadata("lived", new FixedMetadataValue(plugin, 0));
                        player.closeInventory();
                    } else {
                        player.sendMessage(message);
                    }
                    break;
                case "Заповільнення":
                    if (player.getLevel() > 10) {
                        player.setLevel(player.getLevel() - 15);
                        stand.setMetadata("type", new FixedMetadataValue(plugin, "slow"));
                        stand.setMetadata("lived", new FixedMetadataValue(plugin, 0));
                        player.closeInventory();
                    } else {
                        player.sendMessage(message);
                    }
                    break;
                case "Підсвічування":
                    if (player.getLevel() > 20) {
                        player.setLevel(player.getLevel() - 15);
                        stand.setMetadata("type", new FixedMetadataValue(plugin, "glow"));
                        stand.setMetadata("lived", new FixedMetadataValue(plugin, 0));
                        player.closeInventory();
                    } else {
                        player.sendMessage(message);
                    }
                    break;
                case "Вихід":
                    player.closeInventory();
                    break;
            }
            player.removeMetadata("turretEdit", plugin);
        }
    }
}
