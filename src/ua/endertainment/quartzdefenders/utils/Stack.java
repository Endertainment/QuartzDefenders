package ua.endertainment.quartzdefenders.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class Stack {

    private Material material;
    private int amount;
    private int data;
    private String name;
    private List<String> lore;
    private Map<Enchantment, Integer> enchant;
    private Map<Enchantment, Integer> bookEnchants;
    private List<ItemFlag> flags;
    private boolean unbreak;

    public Stack(ConfigurationSection config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null!");
        }
        this.material = Material.valueOf(config.getString("material", "isn't specified").toUpperCase()); //if there's no string in that path - set "isn't specified"
        if (material == null) {
            throw new IllegalArgumentException("Invalid material: " + material);
        }
        this.amount = config.getInt("amount",1);
        this.data = config.getInt("data",0);
        this.name = config.getString("name");
        this.lore = (List<String>) config.get("lore");
        this.enchant = new HashMap<>();
        this.bookEnchants = new HashMap<>();
        for (String key : config.getConfigurationSection("enchantments").getKeys(false)) {
            Enchantment enchantment = Enchantment.getByName(key.toUpperCase());
            if (enchantment == null) {
                LoggerUtil.info("Invalid enchantment: " + key); //Util for colored logs
                continue;
            }
            enchant.put(enchantment, config.getConfigurationSection("enchantments").getInt(key, 0));
        }

        if (material.equals(Material.ENCHANTED_BOOK)) {
            for (String key : config.getConfigurationSection("book_enchantments").getKeys(false)) {
                Enchantment enchantment = Enchantment.getByName(key.toUpperCase());
                if (enchantment == null) {
                    LoggerUtil.info("Invalid enchantment: " + key);
                    continue;
                }
                bookEnchants.put(enchantment, config.getConfigurationSection("book_enchantments").getInt(key, 0));
            }
        }

        config.getStringList("flags").forEach(fl -> {
            ItemFlag flag = ItemFlag.valueOf(fl.toUpperCase());
            if (flag == null) {
                LoggerUtil.info("Invalid ItemFlag: " + fl);
            }
            this.flags.add(flag);
        });

        this.unbreak = config.getBoolean("unbreak");
    }

    public ItemStack getStack() {
        ItemStack stack = new ItemStack(material, amount, (short) data);
        ItemMeta meta = stack.getItemMeta();
        if (name != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        if (lore != null) {
            meta.setLore(new ColorFormat(lore).getFormatedList()); //format color codes
        }
        if (!enchant.isEmpty()) {
            for (Enchantment ench : enchant.keySet()) {
                meta.addEnchant(ench, enchant.get(ench), true);
            }
        }
        if (!flags.isEmpty()) {
            meta.addItemFlags(flags.toArray(new ItemFlag[flags.size()]));
        }
        if (unbreak) {
            meta.setUnbreakable(true);
        }
        stack.setItemMeta(meta);
        if (material.equals(Material.ENCHANTED_BOOK) && !bookEnchants.isEmpty()) {
            EnchantmentStorageMeta bookmeta = (EnchantmentStorageMeta) stack.getItemMeta();
            for (Map.Entry<Enchantment, Integer> entry : bookEnchants.entrySet()) {
                bookmeta.addStoredEnchant(entry.getKey(), entry.getValue(), true);
            }
            stack.setItemMeta(meta);
        }
        return stack;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
