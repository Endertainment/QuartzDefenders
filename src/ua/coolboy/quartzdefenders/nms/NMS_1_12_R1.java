package ua.coolboy.quartzdefenders.nms;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class NMS_1_12_R1 extends NMSAbstract {

    private final String version;
    private Map<Integer, Object> removedEnchants;
    private Class<?> NMSEnchantment;
    private Field enchantmentArray;
    private Object registryID;

    protected NMS_1_12_R1(String version) {
        this.version = version;
        removedEnchants = new HashMap<>();
        init();
    }

    @Override
    public void removeEnchantments(List<Enchantment> enchantments) {
        try {
            Object[] values = (Object[]) enchantmentArray.get(registryID);
            List<Object> remove = new ArrayList<>();
            for (Enchantment ench : enchantments) {
                Object rem = NMSEnchantment.getMethod("b", String.class).invoke(null, ench.getKey().getKey());
                if (rem != null) {
                    remove.add(rem);
                }
            }
            for (int i = 0; i < values.length; i++) {
                Object ench = values[i];
                if (remove.contains(ench)) {
                    removedEnchants.put(i, ench);
                    values[i] = null;
                }
            }
            enchantmentArray.set(registryID, values);

        } catch (Exception ex) {
            LoggerUtil.error("Failed to remove enchantments!");
            ex.printStackTrace();
        }

    }

    private void init() {
        try {
            NMSEnchantment = getNMSClass("Enchantment");
            Object registry = NMSEnchantment.getDeclaredField("enchantments").get(null);
            Class<?> regMat = getNMSClass("RegistryMaterials");
            Field regIDField = regMat.getDeclaredField("a");
            regIDField.setAccessible(true);
            registryID = regIDField.get(registry);
            Class<?> regIDClass = getNMSClass("RegistryID");
            enchantmentArray = regIDClass.getDeclaredField("d"); //d
            enchantmentArray.setAccessible(true);

        } catch (Exception ex) {
            LoggerUtil.error("Failed to init NMS for version " + version);
        }
    }

    @Override
    public void onDisable() {
        //Put removed enchants back
        try {
            Object[] values = (Object[]) enchantmentArray.get(registryID);
            for (int i = 0; i < values.length; i++) {
                if (removedEnchants.get(i) != null) {
                    values[i] = removedEnchants.get(i);
                }
            }
            enchantmentArray.set(registryID, values);
        } catch (Exception ex) {
            LoggerUtil.error("Failed to put back removed enchantments!\n" + ex.getMessage());
        }
    }

    private Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to get NMS class!", e);
        }
        return null;
    }

}
