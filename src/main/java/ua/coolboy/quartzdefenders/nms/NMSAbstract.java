package ua.coolboy.quartzdefenders.nms;

import java.util.List;
import org.bukkit.enchantments.Enchantment;

public class NMSAbstract {

    public void removeEnchantments(List<Enchantment> enchantments) {
        throw new UnsupportedOperationException("Are you using supported version of server?");
    }
    
    public void onDisable() {
        //if there's need to do something
    }

}
