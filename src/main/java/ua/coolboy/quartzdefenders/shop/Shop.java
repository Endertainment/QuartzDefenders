package ua.coolboy.quartzdefenders.shop;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.DataAdapter;
import ua.endertainment.quartzdefenders.utils.ItemUtil;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.Stack;

public class Shop {

    FileConfiguration shop;
    Inventory main;
    ItemUtil item;

    public static String name, stuffName, resourcesName, potionsName,
            enchantName, otherName, foodName, blocksName;

    public Shop(FileConfiguration shop) {
        this.shop = shop;
        name = Language.getString("shop.name");
        stuffName = Language.getString("shop.stuff");
        resourcesName = Language.getString("shop.resources");
        potionsName = Language.getString("shop.potions");
        enchantName = Language.getString("shop.enchant");
        otherName = Language.getString("shop.other");
        foodName = Language.getString("shop.food");
        blocksName = Language.getString("shop.blocks");
    }

    public Inventory getInventory(GameTeam team) {
        this.main = Bukkit.createInventory(null, 27, ChatColor.GOLD + name);
        ItemStack enchant = new ItemStack(Material.EXPERIENCE_BOTTLE);
        enchant = ItemUtil.setName(enchant, enchantName);
        ItemStack potions = new ItemStack(Material.POTION);
        potions = ItemUtil.hideAll(ItemUtil.setName(potions, potionsName));
        ItemStack food = new ItemStack(Material.BREAD);
        food = ItemUtil.setName(food, foodName);
        ItemStack other = new ItemStack(Material.NAME_TAG);
        other = ItemUtil.setName(other, otherName);
        ItemStack blocks = new ItemStack(Material.BRICK);
        blocks = ItemUtil.setName(blocks, blocksName);
        ItemStack stuff = new ItemStack(Material.ARROW);
        stuff = ItemUtil.setName(stuff, stuffName);
        ItemStack resources = new ItemStack(Material.DIAMOND_PICKAXE);
        resources = ItemUtil.hideAll(ItemUtil.setName(resources, resourcesName));
        ItemStack frame = new ItemStack(DataAdapter.getForColor(team.getColor(), "STAINED_GLASS", Material.BLACK_STAINED_GLASS));
        frame = ItemUtil.setName(frame, " ");

        main.setItem(10, enchant);
        main.setItem(11, potions);
        main.setItem(12, food);
        main.setItem(13, other);
        main.setItem(14, blocks);
        main.setItem(15, stuff);
        main.setItem(16, resources);

        int[] g = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        for (int z : g) {
            main.setItem(z, frame);
        }
        return main;
    }

    public Merchant getSection(String sectionName, String merchantName, GameTeam team) {
        ConfigurationSection sect = shop.getConfigurationSection("default");;
        if (team == null || !team.getGame().getCustomShop()) {
            sect = shop.getConfigurationSection("default");
        }
        ConfigurationSection value;
        try {
            value = sect.getConfigurationSection(sectionName);
        } catch (NullPointerException ex) {
            QuartzDefenders.sendInfo(name);
            ex.printStackTrace();
            Merchant m = Bukkit.createMerchant(ChatColor.DARK_RED + "Error");
            return m;
        }
        List<MerchantRecipe> recipes = new ArrayList<>();
        for (String strng : value.getKeys(false)) {
            ConfigurationSection section = value.getConfigurationSection(strng);
            ItemStack primary = createItem(section, "primary", team);

            ItemStack optional = null;
            if (section.isConfigurationSection("optional")) {
                optional = createItem(section, "optional", team);
            }

            ItemStack result = createItem(section, "result", team);
            MerchantRecipe recipe = new MerchantRecipe(result, 999);
            recipe.addIngredient(primary);
            if (optional != null) {
                recipe.addIngredient(optional);
            }
            recipes.add(recipe);
        }
        Merchant m = Bukkit.createMerchant(ChatColor.BOLD + merchantName);
        m.setRecipes(recipes);
        return m;
    }

    private ItemStack createItem(ConfigurationSection section, String itemDir, GameTeam team) {
        ConfigurationSection dir = section.getConfigurationSection(itemDir);

        ItemStack stack = new Stack(dir).getStack();
         if (dir.getBoolean("team", false)) {
            ChatColor color = team == null ? ChatColor.GRAY : team.getColor();
            String material = stack.getType().toString();
            String base = material.substring(material.indexOf("_")+1); // RED_WOOL -> WOOL
            Material mat = DataAdapter.getForColor(color, base);
            stack.setType(mat);
        }
        return stack;
    }
}
