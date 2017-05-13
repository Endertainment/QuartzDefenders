package ua.Coolboy.QuartzDefenders.Shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Utils.FilesUtil;
import ua.Endertainment.QuartzDefenders.Utils.ItemUtil;

public class Shop {

    QuartzDefenders plugin;
    Inventory main;
    FilesUtil files;
    ItemUtil item = new ItemUtil();

    String name, stuffName, resourcesName, potionsName, enchantName, otherName, foodName, blocksName;

    public Shop(QuartzDefenders plugin) {
        this.plugin = plugin;
        this.files = plugin.getConfigs();
        this.name = files.getLang().getString("shop.name").replace('&', '§');
        this.stuffName = files.getLang().getString("shop.stuff").replace('&', '§');
        this.resourcesName = files.getLang().getString("shop.resources").replace('&', '§');
        this.potionsName = files.getLang().getString("shop.potions").replace('&', '§');
        this.enchantName = files.getLang().getString("shop.enchant").replace('&', '§');
        this.otherName = files.getLang().getString("shop.other").replace('&', '§');
        this.foodName = files.getLang().getString("shop.food").replace('&', '§');
        this.blocksName = files.getLang().getString("shop.blocks").replace('&', '§');

        this.main = Bukkit.createInventory(null, 27, ChatColor.GOLD + name);

        ItemStack enchant = new ItemStack(Material.EXP_BOTTLE);
        enchant = item.setName(enchant, enchantName);
        ItemStack potions = new ItemStack(Material.POTION);
        potions = item.hideAll(item.setName(potions, potionsName));
        ItemStack food = new ItemStack(Material.BREAD);
        food = item.setName(food, foodName);
        ItemStack other = new ItemStack(Material.NAME_TAG);
        other = item.setName(other, otherName);
        ItemStack blocks = new ItemStack(Material.BRICK);
        blocks = item.setName(blocks, blocksName);
        ItemStack stuff = new ItemStack(Material.ARROW);
        stuff = item.setName(stuff, stuffName); 
        ItemStack resources = new ItemStack(Material.DIAMOND_PICKAXE);
        resources = item.hideAll(item.setName(resources, resourcesName));

        ItemStack frame = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        frame = item.setName(frame, " ");

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
    }

    public Inventory getInventory() {
        return main;
    }

    public Merchant getSection(String sectionName, String merchantName/*, GameTeam team*/) {
        FileConfiguration shop = files.getShopInfo();
        ConfigurationSection value = shop.getConfigurationSection(sectionName);
        List<MerchantRecipe> recipes = new ArrayList();
        for (String strng : value.getKeys(false)) {
            ConfigurationSection section = value.getConfigurationSection(strng);
            ConfigurationSection primary = section.getConfigurationSection("primary");
            ConfigurationSection result = section.getConfigurationSection("result");

            Boolean resultTeam = false;
            int resultDamage = 0;

            String primaryMaterial = primary.getString("material");
            int primaryAmount = primary.getInt("amount");
            int primaryDamage = primary.getInt("damage");
            ItemStack primaryStack = new ItemStack(Material.valueOf(primaryMaterial), primaryAmount, (short) primaryDamage);

            ItemStack optionalStack = null;
            if (section.isConfigurationSection("optional")) {
                ConfigurationSection optional = section.getConfigurationSection("optional");
                String optionalMaterial = optional.getString("material");
                int optionalAmount = optional.getInt("amount");
                int optionalDamage = optional.getInt("damage");
                optionalStack = new ItemStack(Material.valueOf(optionalMaterial), optionalAmount, (short) optionalDamage);
            }

            String resultMaterial = result.getString("material");
            int resultAmount = result.getInt("amount");
            if (result.isBoolean("team")) {
                resultTeam = result.getBoolean("team");
                resultDamage = 5;//getDamageByColor(team.getColor());
            }
            if (!resultTeam) {
                resultDamage = result.getInt("damage");
            }
            ItemStack resultStack = new ItemStack(Material.valueOf(resultMaterial), resultAmount, (short) resultDamage);
            if (result.isString("name")) {
                String resultName = result.getString("name");
                resultStack = item.setName(resultStack, resultName);
            }
            if (result.isList("lore")) {
                List<String> resultLore = result.getStringList("lore");
                resultStack = item.setLore(resultStack, resultLore);
            }

            if (result.isConfigurationSection("enchantments")) {
                Map<String, Object> enchant = result.getConfigurationSection("enchantments").getValues(false);
                for (Map.Entry<String, Object> entry : enchant.entrySet()) {
                    Enchantment ench = Enchantment.getByName(entry.getKey());
                    int enchLvl = (int) entry.getValue();
                    resultStack.addEnchantment(ench, enchLvl);
                }
            }
            if (resultMaterial.equals("ENCHANTED_BOOK")) {
                Map<String, Object> bookEnchant = result.getConfigurationSection("bookEnchant").getValues(false);
                EnchantmentStorageMeta meta = (EnchantmentStorageMeta) resultStack.getItemMeta();
                for (Map.Entry<String, Object> entry : bookEnchant.entrySet()) {
                    Enchantment ench = Enchantment.getByName(entry.getKey());
                    int enchLvl = (int) entry.getValue();
                    meta.addStoredEnchant(ench, enchLvl, true);
                }
                resultStack.setItemMeta(meta);
            }
            MerchantRecipe recipe = new MerchantRecipe(resultStack, 999);
            recipe.addIngredient(primaryStack);
            if (optionalStack != null) {
                recipe.addIngredient(optionalStack);
            }
            recipes.add(recipe);
        }
        Merchant m = Bukkit.createMerchant(merchantName);
        m.setRecipes(recipes);
        return m;
    }

    public Integer getDamageByColor(ChatColor color) {
        switch (color) {
            case RED:
                return 14;
            case BLUE:
                return 11;
            case GREEN:
                return 13;
            case YELLOW:
                return 4;
            case DARK_PURPLE:
                return 10;
            case AQUA:
                return 3;
            case DARK_GRAY:
                return 7;
            case WHITE:
                return 0;
        }
        return 0;
    }
    private void bcast(String text) {
        Bukkit.getServer().broadcastMessage(text);
    }
}
