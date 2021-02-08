package ua.coolboy.quartzdefenders.mobs;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.Stack;

public abstract class MobsFactory {
    
    public static MobDraft getDraftFor(String path) {
        return deserialize(QuartzDefenders.getInstance().getConfigs().getMobsInfo().getConfigurationSection(path));
    }
    
    public static MobDraft deserialize(ConfigurationSection section) {
        EntityType type = EntityType.valueOf(section.getString("type","UNDEFINED"));
        if (type == null) {
            LoggerUtil.error("Wrong EntityType: " + section.getString("type"));
            return null;
        }
        String name = new ColorFormat(section.getString("name", "")).format();
        boolean nameVisibility = section.getBoolean("name_visibility", false);
        HashMap<Attribute, Double> attributes = new HashMap<>();
        if (section.isConfigurationSection("attributes")) {
            for (String key : section.getConfigurationSection("attributes").getKeys(false)) {
                Attribute attribute = Attribute.valueOf(key);
                if (attribute == null) {
                    LoggerUtil.info("Wrong attribute: " + key);
                    continue;
                }
                attributes.put(attribute, section.getConfigurationSection("attributes").getDouble(key));
            }
        }
        List<Map.Entry<ItemStack, Float>> equipment = new ArrayList<>(8);
        ConfigurationSection equip = section.getConfigurationSection("equipment");
        if (equip != null) {
            ConfigurationSection boots = equip.getConfigurationSection("boots");
            ConfigurationSection leggings = equip.getConfigurationSection("leggings");
            ConfigurationSection chestplate = equip.getConfigurationSection("chestplate");
            ConfigurationSection helmet = equip.getConfigurationSection("helmet");
            ConfigurationSection rarm = equip.getConfigurationSection("right_hand");
            ConfigurationSection larm = equip.getConfigurationSection("left_hand");
            if (boots != null) {
                equipment.add(new AbstractMap.SimpleEntry<>(new Stack(boots.getConfigurationSection("item")).getStack(), (float) boots.getDouble("drop_chance", 0.185)));
            } else equipment.add(null);
            if (leggings != null) {
                equipment.add(new AbstractMap.SimpleEntry<>(new Stack(leggings.getConfigurationSection("item")).getStack(), (float) leggings.getDouble("drop_chance", 0.185)));
            } else equipment.add(null);
            if (chestplate != null) {
                equipment.add(new AbstractMap.SimpleEntry<>(new Stack(chestplate.getConfigurationSection("item")).getStack(), (float) chestplate.getDouble("drop_chance", 0.185)));
            } else equipment.add(null);
            if (helmet != null) {
                equipment.add(new AbstractMap.SimpleEntry<>(new Stack(helmet.getConfigurationSection("item")).getStack(), (float) helmet.getDouble("drop_chance", 0.185)));
            } else equipment.add(null);
            if (rarm != null) {
                equipment.add(new AbstractMap.SimpleEntry<>(new Stack(rarm.getConfigurationSection("item")).getStack(), (float) rarm.getDouble("drop_chance", 0.185)));
            } else equipment.add(null);
            if (larm != null) {
                equipment.add(new AbstractMap.SimpleEntry<>(new Stack(larm.getConfigurationSection("item")).getStack(), (float) larm.getDouble("drop_chance", 0.185)));
            } else equipment.add(null);
        }
        return MobDraft.getBuilder().setType(type.getEntityClass()).setName(name)
                .setCustomNameVisible(nameVisibility).setAttributes(attributes).setEquipment(equipment)
                .build();
    }

}
