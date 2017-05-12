package ua.Coolboy.QuartzDefenders.Shop;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Utils.FilesUtil;
import ua.Endertainment.QuartzDefenders.Utils.ItemUtil;

abstract public class Shop {

    QuartzDefenders plugin;
    Inventory main;
    FilesUtil files = new FilesUtil(plugin);
    ItemUtil item = new ItemUtil();

    String name = files.getLang().getString("shop.name");
    String stuffName = files.getLang().getString("shop.stuff");
    String resourcesName = files.getLang().getString("shop.resources");
    String potionsName = files.getLang().getString("shop.potions");
    String enchantName = files.getLang().getString("shop.enchant");
    String otherName = files.getLang().getString("shop.other");
    String foodName = files.getLang().getString("shop.food");
    String blocksName = files.getLang().getString("shop.blocks");

    public Shop() {
        this.main = Bukkit.createInventory(null, 27, ChatColor.GOLD + name);

        ItemStack enchant = new ItemStack(Material.EXP_BOTTLE);
        enchant = item.setName(enchant, enchantName);
        ItemStack potions = new ItemStack(Material.POTION);
        potions = item.setName(potions, potionsName);
        ItemStack food = new ItemStack(Material.BREAD);
        food = item.setName(food, foodName);
        ItemStack other = new ItemStack(Material.NAME_TAG);
        other = item.setName(other, otherName);
        ItemStack blocks = new ItemStack(Material.BRICK);
        blocks = item.setName(blocks, blocksName);
        ItemStack stuff = new ItemStack(Material.ARROW);
        stuff = item.setName(stuff, stuffName);
        ItemStack resources = new ItemStack(Material.DIAMOND_PICKAXE);
        resources = item.setName(resources, resourcesName);

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

    public Merchant getSection(String sectionName, String merchantName) {
        FileConfiguration shop = files.getShopInfo();
        ConfigurationSection o = shop.getConfigurationSection(sectionName);
        List<MerchantRecipe> recipes=null;
        
        for (Integer integ : o.getIntegerList(sectionName)) {
            ConfigurationSection section = shop.getConfigurationSection(sectionName + "." +Integer.toString(integ));
            ConfigurationSection primary = section.getConfigurationSection("primary");
            ConfigurationSection result = section.getConfigurationSection("result");

            String primaryMaterial = primary.getString("material");
            int primaryAmount = primary.getInt("amount");
            int primaryDamage = primary.getInt("damage");
            ItemStack primaryStack = new ItemStack(Material.valueOf(primaryMaterial), primaryAmount, (short) primaryDamage);
            
            ItemStack optionalStack=null;
            if (section.isConfigurationSection("option")) {
                ConfigurationSection optional = section.getConfigurationSection("optional");
                String optionalMaterial = optional.getString("material");
                int optionalAmount = optional.getInt("amount");
                int optionalDamage = optional.getInt("damage");
                optionalStack = new ItemStack(Material.valueOf(optionalMaterial), optionalDamage, (short) optionalDamage);
            }

            String resultMaterial = result.getString("material");
            int resultAmount = result.getInt("amount");
            int resultDamage = result.getInt("damage");
            ItemStack resultStack = new ItemStack(Material.valueOf(resultMaterial), resultDamage, (short) resultDamage);
            if (result.isString("name")) {
                String resultName = result.getString("name");
                resultStack = item.setName(resultStack, resultName);
            }
            if (result.isList("lore")) {
                List<String> resultLore = result.getStringList("lore");
                resultStack = item.setLore(resultStack, resultLore);
            }

            if (result.isList("enchantments")) {
                List<String> resultEnch = result.getStringList("enchantments");
                for (String enchant : resultEnch) {
                    resultStack.addEnchantment(Enchantment.getByName(enchant), 1);
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
            MerchantRecipe recipe = new MerchantRecipe(resultStack,999);
            recipe.addIngredient(primaryStack);
            if(optionalStack != null) {
                recipe.addIngredient(optionalStack);
            }
            recipes.add(recipe);
        }
        Merchant m = Bukkit.createMerchant(merchantName);
        m.setRecipes(recipes);
        return m;
    }
}

