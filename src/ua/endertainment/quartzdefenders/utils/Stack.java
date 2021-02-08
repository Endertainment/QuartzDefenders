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
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class Stack {

    private Material material;
    private int amount;
    private String name;
    private List<String> lore;
    private Map<Enchantment, Integer> enchant;
    private Map<Enchantment, Integer> bookEnchants;
    private List<PotionEffect> potionEffects;
    private PotionData potionData;
    private List<ItemFlag> flags;
    private boolean unbreak;

    public Stack(ConfigurationSection config) {
        if (config == null) {
            throw new IllegalArgumentException("Config cannot be null!");
        }
        this.material = Material.valueOf(config.getString("material", "UNDEFINED").toUpperCase());
        if (material == null) {
            throw new IllegalArgumentException("Invalid material: " + material);
        }
        this.amount = config.getInt("amount", 1);
        this.name = config.getString("name");
        this.lore = config.getStringList("lore");
        this.enchant = new HashMap<>();
        this.bookEnchants = new HashMap<>();
        this.potionEffects = new ArrayList<>();
        if (config.isConfigurationSection("enchantments")) {
            for (String key : config.getConfigurationSection("enchantments").getKeys(false)) {
                Enchantment enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(key.toLowerCase()));//Enchantment.getByName(key.toUpperCase());
                if (enchantment == null) {
                    LoggerUtil.error("Invalid enchantment: " + key);
                    continue;
                }
                enchant.put(enchantment, config.getConfigurationSection("enchantments").getInt(key, 0));
            }
        }
        
        if (material.equals(Material.ENCHANTED_BOOK) && config.isConfigurationSection("book_enchantments")) {
            for (String key : config.getConfigurationSection("book_enchantments").getKeys(false)) {
                Enchantment enchantment = EnchantmentWrapper.getByKey(NamespacedKey.minecraft(key.toLowerCase()));
                if (enchantment == null) {
                    LoggerUtil.error("Invalid book enchantment: " + key);
                    continue;
                }
                bookEnchants.put(enchantment, config.getConfigurationSection("book_enchantments").getInt(key, 0));
            }
        }
        
        if(material.equals(Material.POTION)) {
            
            String strDataType = config.getString("potion_data", "WATER");
            PotionType dataType = PotionType.valueOf(strDataType);
            if(dataType == null) {
                LoggerUtil.error("Invalid potion data type: "+strDataType);
            }
            potionData = new PotionData(dataType);
            
            if(config.isConfigurationSection("potion_effects")) {
                for(String key : config.getConfigurationSection("potion_effects").getKeys(false)) {
                    ConfigurationSection eff = config.getConfigurationSection("potion_effects."+key);
                    String strType = eff.getString(eff.getString("type"), "UNDEFINED");
                    PotionEffectType type = PotionEffectType.getByName(strType);
                    if(type == null) {
                        LoggerUtil.error("Invalid potion type: "+strType);
                        continue;
                    }
                    int duration = eff.getInt("duration", 0);
                    int amplifier = eff.getInt("amplifier", 0);
                    boolean ambient = eff.getBoolean("ambient", false);
                    boolean particles = eff.getBoolean("particles", true);
                    boolean icon = eff.getBoolean("icon", true);

                    PotionEffect effect = new PotionEffect(type, duration, amplifier, ambient, particles, icon);
                    potionEffects.add(effect);
                }
            }
        }
        
        flags = new ArrayList<>();
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
        ItemStack stack = new ItemStack(material, amount);
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
            stack.setItemMeta(bookmeta);
        }
        
        if(material.equals(Material.POTION)) {
                PotionMeta potionmeta = (PotionMeta) stack.getItemMeta();
                
                if(potionData != null) {
                    potionmeta.setBasePotionData(potionData);
                }
                if(!potionEffects.isEmpty()) {
                    for(PotionEffect effect : potionEffects) {
                        potionmeta.addCustomEffect(effect, true);
                    }  
                }
                stack.setItemMeta(potionmeta);
        }
        return stack;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
