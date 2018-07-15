package ua.endertainment.quartzdefenders.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.DataAdapter;
import ua.endertainment.quartzdefenders.utils.ItemUtil;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.Replacer;

public class TeamGUI {

    private Inventory inventory;

    private Game game;

    private Player p;

    private String title;

    public TeamGUI(Game game, Player p) {
        this.game = game;
        this.p = p;
        this.title = new ColorFormat(Language.getString("GUI.teams.name")).format();
        this.inventory = Bukkit.createInventory(new QuartzInventoryHolder(), 6 * 9, title);
        this.menuCorner();
        this.a();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void openInventory() {
        p.openInventory(inventory);
    }

    private void menuCorner() {
        DyeColor y = DyeColor.valueOf(Language.getString("GUI.teams.glass_color_1"));
        DyeColor z = DyeColor.valueOf(Language.getString("GUI.teams.glass_color_2"));
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
        HashMap<ItemStack, Integer> teamsIS = new HashMap<>();
        int x = 20;
        for (GameTeam team : game.getTeams().values()) {
            ArrayList<String> lore = new ArrayList<String>();
            lore.add(" ");
            lore.add(Language.getRawString("GUI.teams.players", new Replacer("{c}", team.getColor() + ""), new Replacer("{0}", team.getPlayersSize() + ""), new Replacer("{1}", team.intPlayersInTeam() + "")));
            lore.add(" ");
            ItemStack itemWool = ItemUtil.newItem(
                    Language.getRawString(
                            "GUI.teams.item_name",
                            new Replacer("{c}", team.getColor() + ""),
                            new Replacer("{0}", team.getName())),
                    lore,
                    DataAdapter.getWoolByColor(team.getDyeColor()), 1
            );
            if (x == 24) {
                x = 29;
            }
            teamsIS.put(itemWool, x);
            x++;
        }

        teamsIS.put(ItemUtil.newItem(Language.getRawString("GUI.teams.item_leave"), Arrays.asList(" ", " ", " "), Material.BARRIER, 1), 40);
        for (ItemStack s : teamsIS.keySet()) {
            inventory.setItem(teamsIS.get(s), s);
        }

    }

}
