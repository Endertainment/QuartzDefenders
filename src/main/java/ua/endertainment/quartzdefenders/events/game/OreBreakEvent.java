package ua.endertainment.quartzdefenders.events.game;

import org.bukkit.block.Block;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OreBreakEvent extends Event implements Cancellable{

    private boolean isCancelled;

    private Block block;

    public OreBreakEvent(Block block) {
        this.block = block;
        this.isCancelled = false;
    }
    
    public Block getBlock() {
        return block;
    }
    
    public static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean paramBoolean) {
        this.isCancelled = paramBoolean;
    }
}
