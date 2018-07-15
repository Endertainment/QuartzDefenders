package ua.endertainment.quartzdefenders.gui;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import ua.endertainment.quartzdefenders.QuartzDefenders;

import ua.endertainment.quartzdefenders.kits.Kit;
import ua.endertainment.quartzdefenders.kits.KitsManager;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.DataAdapter;
import ua.endertainment.quartzdefenders.utils.ItemUtil;
import ua.endertainment.quartzdefenders.utils.Language;

public class LobbyShopGUI {

    private Inventory inventory;

    private String title;

    private Player player;
    private KitsManager kitM;

    private int[] slots = {20, 21, 22, 23, 24, 28, 29, 30, 31, 32, 33, 37, 38, 39, 40, 41, 42};

    private ArrayList<ItemStack> items;

    public LobbyShopGUI(Player player) {
        this.title = new ColorFormat(Language.getString("GUI.shop.name")).format();
        this.inventory = Bukkit.createInventory(new QuartzInventoryHolder(), 6 * 9, title);
        this.player = player;
        this.kitM = QuartzDefenders.getInstance().getKitManager();
        this.items = new ArrayList<>();

        this.menuCorner();

        for (Kit kit : kitM.getKitsRegistry().keySet()) {
            ItemStack i = ItemUtil.addLore(kit.getPreviewItem(), kitM.getAvailability(kit, player));
            items.add(i);
        }

    }

    public Inventory getInventory() {
        return inventory;
    }

    public void openInventory() {
        a();
        player.openInventory(inventory);
    }

    private void menuCorner() {
        DyeColor y = DyeColor.valueOf(Language.getString("GUI.shop.glass_color_1"));
        DyeColor z = DyeColor.valueOf(Language.getString("GUI.shop.glass_color_2"));
        if (y == null) {
            y = DyeColor.PURPLE;
        }
        if (z == null) {
            z = DyeColor.BLACK;
        }
        int[] arg1 = {0, 2, 4, 6, 8, 18, 26, 36, 44, 46, 48, 50, 52}, arg2 = {1, 3, 5, 7, 9, 17, 27, 35, 45, 47, 49, 51, 53}; // z, y
        for (int a : arg1) {
            inventory.setItem(a, ItemUtil.newItem(" ", null, DataAdapter.getGlassPaneByColor(z), 1));
        }
        for (int b : arg2) {
            inventory.setItem(b, ItemUtil.newItem(" ", null, DataAdapter.getGlassPaneByColor(y), 1));
        }
    }

    private void a() {

        int index = 0;

        for (int i : slots) {
            if (index >= items.size()) {
                return;
            }
            inventory.setItem(i, items.get(index));
            index++;
        }

    }

//00 01 02 03 04 05 06 07 08
//09 10 11 12 13 14 15 16 17
//18 19 20 21 22 23 24 25 26
//27 28 29 30 31 32 33 34 35
//36 37 38 39 40 41 42 43 44
//45 46 47 48 49 50 51 52 53
}
