package ua.endertainment.quartzdefenders.game;

import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.events.game.QuartzBreakEvent;
import ua.endertainment.quartzdefenders.stats.StatsPlayer;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.Replacer;

public class GameQuartz {

    private Location location;
    private int quartzHealth;
    private GameTeam team;
    private Game game;

    public GameQuartz(Game game, GameTeam team, Location location, int quartzHealth) {
        this.team = team;
        this.game = game;
        this.location = location;
        this.quartzHealth = quartzHealth;
    }

    public boolean breakQuartz(GamePlayer player) {

        QuartzBreakEvent event = new QuartzBreakEvent(this);
        Bukkit.getPluginManager().callEvent(event);
        if(event.isCancelled()) return false;
        if (team.contains(player)) {
            replace();
            player.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.quartz_break_failed")));
            return false;
        }

        for (GamePlayer p : team.getPlayers()) {
            p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_ANVIL_PLACE, 1, 1);
        }
        for (GamePlayer p : game.getPlayers()) {
            if (!team.contains(p)) {
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.BLOCK_STONE_BREAK, 1, 1);
            }
        }

        setQuartzHealth(getQuartzHealth() - 1);
        this.replace();

        player.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 4 * 20, 0), true);

        if (getQuartzHealth() == 0) {
            for (GamePlayer p : game.getPlayers()) {
                p.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.quartz_destroy", new Replacer("{0}", player.getDisplayName()), new Replacer("{1}", team.getName()))));
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);

                StatsPlayer sp = new StatsPlayer(player.getPlayer());
                sp.addBrokenQuartz();

            }
            for (GamePlayer p : team.getPlayers()) {
                p.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.quartz_broken")));
                p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0), true);
                p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 0), true);
                p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0), true);
            }
        }
        return true;
    }

    public void breakQuartz() {
        if(getQuartzHealth()==0) return; //prevent flood
        setQuartzHealth(getQuartzHealth() - 1);
        if(getQuartzHealth()==0) {
            for (GamePlayer p : game.getPlayers()) {
                p.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), "Quartz of team "+team.getName()+ChatColor.GRAY+" was destroyed!"));
                p.getPlayer().playSound(p.getPlayer().getLocation(), Sound.ENTITY_WITHER_DEATH, 1, 1);
            }
            for (GamePlayer p : team.getPlayers()) {
                p.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.quartz_broken")));
                p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0), true);
                p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, Integer.MAX_VALUE, 0), true);
                p.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0), true);
            }
        }
        game.refreshScoreboard();
    }

    public void destroyQuartz() {
        setQuartzHealth(0);
        replace();
    }

    public Location getLocation() {
        return location;
    }

    public int getQuartzHealth() {
        return quartzHealth;
    }

    public void setQuartzHealth(int quartzHealth) {
        if (quartzHealth < 0 || this.quartzHealth==0) {//why?
            return;
        }
        this.quartzHealth = quartzHealth;
    }

    public void checkQuartz() {
        Block b = game.getGameWorld().getBlockAt(getLocation());
        if (b.getType() != Material.QUARTZ_ORE || b.getType() != Material.BEDROCK) {
            replace();
        }
    }

    public void replace() {
        Block b = game.getGameWorld().getBlockAt(getLocation());
        if (getQuartzHealth() > 0) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(QuartzDefenders.getInstance(), () -> {
                b.setType(Material.QUARTZ_ORE);
            });
        } else {
            Bukkit.getScheduler().scheduleSyncDelayedTask(QuartzDefenders.getInstance(), () -> {
                b.setType(Material.BEDROCK);
            });
        }
    }

    public GameTeam getTeam() {
        return team;
    }

    public Game getGame() {
        return game;
    }

}
