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
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.Stack;

public abstract class MobsFactory {

    public MobDraft deserialize(ConfigurationSection section) {
        EntityType type = EntityType.valueOf(section.getString("type"));
        if (type == null) {
            LoggerUtil.error("Wrong EntityType: " + section.getString("type"));
            return null;
        }
        String name = section.getString("name");
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
        List<Map.Entry<ItemStack, Float>> equipment = new ArrayList<>();
        ConfigurationSection equip = section.getConfigurationSection("equipment");
        if (equip != null) {
            ConfigurationSection boots = equip.getConfigurationSection("boots");
            ConfigurationSection leggings = equip.getConfigurationSection("leggings");
            ConfigurationSection chestplate = equip.getConfigurationSection("chestplate");
            ConfigurationSection helmet = equip.getConfigurationSection("helmet");
            ConfigurationSection rarm = equip.getConfigurationSection("right_arm");
            ConfigurationSection larm = equip.getConfigurationSection("left_arm");
            if (boots != null) {
                equipment.add(0, new AbstractMap.SimpleEntry<>(new Stack(boots.getConfigurationSection("item")).getStack(), (float) boots.getDouble("drop_chance", 0)));
            }
            if (leggings != null) {
                equipment.add(1, new AbstractMap.SimpleEntry<>(new Stack(leggings.getConfigurationSection("item")).getStack(), (float) leggings.getDouble("drop_chance", 0)));
            }
            if (chestplate != null) {
                equipment.add(2, new AbstractMap.SimpleEntry<>(new Stack(chestplate.getConfigurationSection("item")).getStack(), (float) chestplate.getDouble("drop_chance", 0)));
            }
            if (helmet != null) {
                equipment.add(3, new AbstractMap.SimpleEntry<>(new Stack(helmet.getConfigurationSection("item")).getStack(), (float) helmet.getDouble("drop_chance", 0)));
            }
            if (rarm != null) {
                equipment.add(4, new AbstractMap.SimpleEntry<>(new Stack(rarm.getConfigurationSection("item")).getStack(), (float) rarm.getDouble("drop_chance", 0)));
            }
            if (larm != null) {
                equipment.add(5, new AbstractMap.SimpleEntry<>(new Stack(larm.getConfigurationSection("item")).getStack(), (float) larm.getDouble("drop_chance", 0)));
            }
        }
        return MobDraft.getBuilder().setType(type.getEntityClass()).setName(name)
                .setCustomNameVisible(nameVisibility).setAttributes(attributes).setEquipment(equipment)
                .build();
    }

}
