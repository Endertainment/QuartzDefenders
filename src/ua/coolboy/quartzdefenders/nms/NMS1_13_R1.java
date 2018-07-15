package ua.coolboy.quartzdefenders.nms;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.enchantments.Enchantment;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class NMS1_13_R1 extends NMS1_12_R1 {

    public NMS1_13_R1(String version) {
        super(version);
    }
    
    @Override
    public void removeEnchantments(List<Enchantment> enchantments) {
        try {
            Object[] values = (Object[]) enchantmentArray.get(registryID);
            List<Object> remove = new ArrayList<>();
            for (Enchantment ench : enchantments) {
                Object rem = cNMSEnchantment.getMethod("b", String.class).invoke(null, ench.getKey().getKey()); //only 1 changed thing
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

}
