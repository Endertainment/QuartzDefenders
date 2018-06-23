package ua.coolboy.quartzdefenders.mobs;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class MobDraft<T extends LivingEntity> {

    private Class<T> clazz;
    //private List<ItemStack> inventory = new ArrayList<>(); //Living entities don't have inventories?
    private List<Entry<ItemStack, Float>> equipment = new ArrayList<>(8); //0 - boots, 1 - leggings, 2 - chest, 3 - helmet, 4- right_arm, 5-left_arm
    private HashMap<Attribute, Double> attributes = new HashMap<>();
    private String name;
    private boolean nameVisibility = false;

    protected MobDraft(Class<T> clazz) {
        if (clazz == null) {
            throw new RuntimeException("Entity class cannot be null!");
        }
        this.clazz = clazz;
    }

    protected MobDraft(EntityType type) {
        try {
            clazz = (Class<T>) type.getEntityClass();
        } catch (ClassCastException ex) {
            throw new RuntimeException("Only Living Entities supported!");
        }
    }

    public T spawn(Location location) {
        T entity = location.getWorld().spawn(location, clazz);
        if (name != null) {
            entity.setCustomName(name);
        }
        entity.setCustomNameVisible(nameVisibility);

        entity.getEquipment().setBoots(getOrDefault(equipment, 0).getKey());
        entity.getEquipment().setLeggings(getOrDefault(equipment, 1).getKey());
        entity.getEquipment().setChestplate(getOrDefault(equipment, 2).getKey());
        entity.getEquipment().setHelmet(getOrDefault(equipment, 3).getKey());
        entity.getEquipment().setItemInMainHand(getOrDefault(equipment, 4).getKey());
        entity.getEquipment().setItemInOffHand(getOrDefault(equipment, 5).getKey());

        entity.getEquipment().setBootsDropChance(getOrDefault(equipment, 0).getValue());
        entity.getEquipment().setLeggingsDropChance(getOrDefault(equipment, 1).getValue());
        entity.getEquipment().setChestplateDropChance(getOrDefault(equipment, 2).getValue());
        entity.getEquipment().setHelmetDropChance(getOrDefault(equipment, 3).getValue());
        entity.getEquipment().setItemInMainHandDropChance(getOrDefault(equipment, 4).getValue());
        entity.getEquipment().setItemInOffHandDropChance(getOrDefault(equipment, 5).getValue());

        for (Entry<Attribute, Double> entry : attributes.entrySet()) {
            AttributeInstance attr = entity.getAttribute(entry.getKey());
            if (attr != null) {
                attr.setBaseValue(entry.getValue());
                if(entry.getKey().equals(Attribute.GENERIC_MAX_HEALTH)) entity.setHealth(entry.getValue()); //set max health
            } else {
                LoggerUtil.error(entity.getType() + " doesn't have attribute "+entry.getKey());
            }
        }
        return entity;
    }

    private Entry<ItemStack, Float> getOrDefault(List<Entry<ItemStack, Float>> stacks, int i) {
        AbstractMap.SimpleEntry<ItemStack, Float> air = new AbstractMap.SimpleEntry<>(new ItemStack(Material.AIR), (float) 0);
        if (stacks.size() > i) {
            return stacks.get(i) == null ? air : stacks.get(i);
        }
        return air;
    }

    public Class<T> getEntityClass() {
        return clazz;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCustomNameVisible(boolean visible) {
        this.nameVisibility = visible;
    }

    public boolean getCustomNameVisible() {
        return nameVisibility;
    }

    public void setHealth(int health) {
        attributes.put(Attribute.GENERIC_MAX_HEALTH, (double) health);
    }

    public HashMap<Attribute, Double> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<Attribute, Double> attr) {
        attributes = attr;
    }

    public void setAttribute(Attribute attribute, double value) {
        attributes.put(attribute, value);
    }

    public List<Entry<ItemStack, Float>> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Entry<ItemStack, Float>> eq) {
        equipment = eq;
    }

    public void setBoots(ItemStack boots, float dropChance) {
        equipment.add(0, new AbstractMap.SimpleEntry<>(boots, dropChance));
    }

    public void setLeggins(ItemStack leggins, float dropChance) {
        equipment.add(1, new AbstractMap.SimpleEntry<>(leggins, dropChance));
    }

    public void setChestplate(ItemStack chestplate, float dropChance) {
        equipment.add(2, new AbstractMap.SimpleEntry<>(chestplate, dropChance));
    }

    public void setHelmet(ItemStack helmet, float dropChance) {
        equipment.add(3, new AbstractMap.SimpleEntry<>(helmet, dropChance));
    }

    public void setRightArm(ItemStack rarm, float dropChance) {
        equipment.add(4, new AbstractMap.SimpleEntry<>(rarm, dropChance));
    }

    public void setLeftArm(ItemStack larm, float dropChance) {
        equipment.add(5, new AbstractMap.SimpleEntry<>(larm, dropChance));
    }

    /*public List<ItemStack> getInventory() {
        return inventory;
    }

    public void setInventory(List<ItemStack> inv) {
        inventory = inv;
    }

    public void addToInventory(ItemStack stack) {
        inventory.add(stack);
    }

    public void addAllToInventory(Collection<ItemStack> stacks) {
        inventory.addAll(stacks);
    }*/
    public static MobDraftBuilder getBuilder() {
        return new MobDraftBuilder();
    }

    public static class MobDraftBuilder<T extends LivingEntity> {

        private Class<T> clazz;
        //private List<ItemStack> inventory = new ArrayList<>();
        private List<Entry<ItemStack, Float>> equipment = new ArrayList<>(8); //0 - boots, 1 - leggings, 2 - chest, 3 - helmet, 4- right_arm, 5-left_arm
        private HashMap<Attribute, Double> attributes = new HashMap<>();
        private String name = null;
        private boolean nameVisibility = false;

        public MobDraftBuilder(Class<T> clazz) {
            this.clazz = clazz;
        }

        public MobDraftBuilder() {
            this.clazz = null;
        }

        public MobDraftBuilder<T> setType(Class<T> clazz) {
            this.clazz = clazz;
            return this;
        }

        public MobDraftBuilder<T> setHealth(int health) {
            attributes.put(Attribute.GENERIC_MAX_HEALTH, (double) health);
            return this;
        }

        public MobDraftBuilder<T> setAttributes(HashMap<Attribute, Double> map) {
            attributes = map;
            return this;
        }

        public MobDraftBuilder<T> setAttribute(Attribute attribute, double value) {
            attributes.put(attribute, value);
            return this;
        }

        public MobDraftBuilder<T> setName(String customname) {
            name = customname;
            return this;
        }

        public MobDraftBuilder<T> setEquipment(List<Entry<ItemStack, Float>> equip) {
            equipment = equip;
            return this;
        }

        public MobDraftBuilder<T> setBoots(ItemStack boots, float dropChance) {
            equipment.add(0, new AbstractMap.SimpleEntry<>(boots, dropChance));
            return this;
        }

        public MobDraftBuilder<T> setLeggins(ItemStack leggins, float dropChance) {
            equipment.add(1, new AbstractMap.SimpleEntry<>(leggins, dropChance));
            return this;
        }

        public MobDraftBuilder<T> setChestplate(ItemStack chestplate, float dropChance) {
            equipment.add(2, new AbstractMap.SimpleEntry<>(chestplate, dropChance));
            return this;
        }

        public MobDraftBuilder<T> setHelmet(ItemStack helmet, float dropChance) {
            equipment.add(3, new AbstractMap.SimpleEntry<>(helmet, dropChance));
            return this;
        }

        public MobDraftBuilder<T> setRightArm(ItemStack rarm, float dropChance) {
            equipment.add(4, new AbstractMap.SimpleEntry<>(rarm, dropChance));
            return this;
        }

        public MobDraftBuilder<T> setLeftArm(ItemStack larm, float dropChance) {
            equipment.add(5, new AbstractMap.SimpleEntry<>(larm, dropChance));
            return this;
        }

        /*
        public MobDraftBuilder<T> addToInventory(ItemStack stack) {
            inventory.add(stack);
            return this;
        }

        public MobDraftBuilder<T> addAllToInventory(Collection<ItemStack> stacks) {
            inventory.addAll(stacks);
            return this;
        }
         */
        public MobDraftBuilder<T> setCustomNameVisible(boolean visible) {
            nameVisibility = visible;
            return this;
        }

        public MobDraft build() {
            MobDraft draft = new MobDraft(clazz);
            if (name != null) {
                draft.setName(name);
            }
            draft.setAttributes(attributes);
            draft.setEquipment(equipment);
            draft.setCustomNameVisible(nameVisibility);
            //draft.setInventory(inventory);
            return draft;
        }
    }
}
