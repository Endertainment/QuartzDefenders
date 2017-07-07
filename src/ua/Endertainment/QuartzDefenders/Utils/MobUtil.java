package ua.Endertainment.QuartzDefenders.Utils;

import org.bukkit.entity.Entity;

public abstract class MobUtil {
    
    public static void setNoCollide(Entity entity) {
        Class<?> CraftEntity = NMSUtil.getNMSClass("Entity");
        try {
            CraftEntity.getField("collides").set(entity, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
