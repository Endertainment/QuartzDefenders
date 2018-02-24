package ua.coolboy.quartzdefenders.mobs;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public abstract class MobsFactory {

    public MobDraft deserialize(ConfigurationSection section) {
        EntityType type = EntityType.valueOf(section.getString("type"));
        if(type==null) {
            LoggerUtil.error("Wrong EntityType: "+section.getString("type"));
            return null;
        }
        return MobDraft.getBuilder().setType(type.getEntityClass()).build();
    } 

}
