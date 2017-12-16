package ua.endertainment.quartzdefenders.events.game;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GameQuartz;
import ua.endertainment.quartzdefenders.game.GameTeam;

public class QuartzBreakEvent extends Event implements Cancellable{

    private boolean isCancelled;

    private GameQuartz quartz;

    public QuartzBreakEvent(GameQuartz quartz) {
        this.quartz = quartz;
        this.isCancelled = false;
    }
    
    public GameQuartz getQuartz() {
        return quartz;
    }
    
    public GameTeam getTeam() {
        return quartz.getTeam();
    }
    
    public int getHealth() {
        return quartz.getQuartzHealth();
    }
    
    public Game getGame() {
        return quartz.getGame();
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
