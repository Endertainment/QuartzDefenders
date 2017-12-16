package ua.endertainment.quartzdefenders.kits;

import java.util.List;

import org.bukkit.inventory.ItemStack;

import ua.endertainment.quartzdefenders.game.GamePlayer;

public interface Kit {

	public abstract String getKitID();
	
    public abstract String getDisplayName();

    public abstract String getName();

    public abstract int getPrice();

    public abstract int getLevel();
    
    public abstract String getAchievemet();
    
    public abstract String getPermission();

    public abstract ItemStack getPreviewItem();

    public abstract List<String> getDescription();

    public abstract List<KitItem> getItems();

    public abstract void apply(GamePlayer gp);
}
