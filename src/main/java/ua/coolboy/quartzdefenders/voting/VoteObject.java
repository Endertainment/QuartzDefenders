package ua.coolboy.quartzdefenders.voting;

import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import ua.coolboy.quartzdefenders.voting.VoteManager.Default;
import ua.endertainment.quartzdefenders.utils.ItemUtil;

public class VoteObject {

    private Type type;
    private Default defaultt;
    private boolean isVoting;

    public VoteObject(Type type, Default defaultt, boolean isVoting) {
        this.type = type;
        this.defaultt = defaultt;
        this.isVoting = isVoting;
    }

    public boolean isDefault() {
        return !defaultt.equals(Default.NOT_SET);
    }
    
    public Type getType() {
        return type;
    }
    
    public boolean isVoting() {
        return isVoting;
    }

    public Default getDefault() {
        return defaultt;
    }

    public enum Type {
        AUTOSMELT("autosmelt", ItemUtil.newItem(ChatColor.DARK_PURPLE + "AutoSmelt", Arrays.asList("Ores drop ingots"), Material.FURNACE, 1)),
        BLOCK_RANGED("block_ranged", ItemUtil.newItem(ChatColor.GOLD + "Block ranged weapons", Arrays.asList("Blocks bow and crossbow recipe"), Material.BOW, 1)),
        DIAMOND_DEFENDERS("diamond_defenders", ItemUtil.newItem(ChatColor.AQUA + "Diamond Defenders", Arrays.asList("When diamond ore is mined, it spawns two defenders"), Material.DIAMOND_SWORD, 1)),
        UNDEFINED("undefined", ItemUtil.newItem(ChatColor.DARK_RED + "UNDEFINED", Arrays.asList("ERROR IN CONFIG"), Material.STRUCTURE_VOID, 1));
        private String id;
        private ItemStack item;

        private Type(String id, ItemStack item) {
            this.id = id;
            this.item = item;
        }

        public String getID() {
            return id;
        }

        public ItemStack getItem() {
            return item.clone();
        }

        public static Type fromID(String id) {
            for (Type type : values()) {
                if (id.toLowerCase().equals(type.getID())) {
                    return type;
                }
            }
            return Type.UNDEFINED;
        }
    }

}
