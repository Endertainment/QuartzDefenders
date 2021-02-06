package ua.endertainment.quartzdefenders;

import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.game.Game;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

import ua.endertainment.quartzdefenders.items.QItems;
import ua.endertainment.quartzdefenders.stats.TopManager;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.LobbySidebar;

public class Lobby implements Listener {

    private QuartzDefenders plugin;
    private Location location;

    private ArrayList<Player> hide = new ArrayList<>();

    public Lobby(QuartzDefenders plugin) {
        this.plugin = plugin;

        if (plugin.getConfig().getString("Lobby.lobby_world_name") == null || plugin.getConfig().getString("Lobby.lobby_world_name").equalsIgnoreCase("null")) {
            LoggerUtil.error("Can not find 'Lobby.lobby_world_name' in configuration file. Check config or visit web page to see more info");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }
        
        World world = Bukkit.getWorld(plugin.getConfig().getString("Lobby.lobby_world_name"));
        double x = plugin.getConfig().getDouble("Lobby.spawn.x") + 0.5;
        double y = plugin.getConfig().getDouble("Lobby.spawn.y");
        double z = plugin.getConfig().getDouble("Lobby.spawn.z") + 0.5;
        float yaw = (float) plugin.getConfig().getDouble("Lobby.spawn.yaw");
        float pitch = (float) plugin.getConfig().getDouble("Lobby.spawn.pitch");
        this.location = new Location(world, x, y, z, yaw, pitch);
        Bukkit.getPluginManager().registerEvents(this, plugin);

    }

    public void sendTabList(Player p) {
        String header = Language.getString("tablist.lobby.header");
        String footer = Language.getString("tablist.lobby.footer");
        
        p.setPlayerListHeaderFooter(header, footer);
    }

    public void sendTabListForLobby() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld() == location.getWorld()) {
                sendTabList(p);
            }
        }
    }

    public void hidePlayers(Player p) {
        if (!p.getLocation().getWorld().equals(location.getWorld())) {
            return;
        }
        if (!hide.contains(p)) {
            hide.add(p);
            for (Player targ : Bukkit.getOnlinePlayers()) {
                if (targ.hasPermission("QuartzDefenders.lobby.visible")
                        && !targ.getLocation().getWorld().equals(location.getWorld())) {
                    p.hidePlayer(plugin, targ);
                }

                p.hidePlayer(plugin, targ);
            }
        }
    }

    public void showPlayers(Player p) {
        if (!p.getLocation().getWorld().equals(location.getWorld())) {
            return;
        }
        if (hide.contains(p)) {
            hide.remove(p);
            for (Player targ : location.getWorld().getPlayers()) {
                p.showPlayer(plugin, targ);
            }
        }
    }

    public ArrayList<Player> getHides() {
        return hide;
    }

    public void teleportToSpawn(Player p, boolean arg) {
        if (!arg) {
            if (p.getLocation() != location.getWorld()) {
                p.teleport(location);
            } else {
                p.teleport(location);
            }
        }
    }

    public Location getLocation() {
        return location;
    }

    public World getWorld() {
        return location.getWorld();
    }

    public void setLobbyTools(Player p) {
        if (p.getWorld() != getWorld()) {
            return;
        }
        p.getInventory().clear();

        p.getInventory().setItem(0, QItems.itemGamesChoose());
        p.getInventory().setItem(4, QItems.itemStats());
        p.getInventory().setItem(8, QItems.itemHidePlayers(getHides().contains(p)));
        p.getInventory().setItem(6, QItems.itemLobbyShop());
        p.getInventory().setItem(2, QItems.itemAchievements());
    }

    private boolean listContains(List<List<Integer>> where, List<Integer> find) {
        boolean finded;
        for (List<?> list : where) {
            finded = list.containsAll(find);
            if (finded == true) {
                return finded;
            }
        }
        return false;
    }

    public void addSignK(Location loc, Player p) {
        FileConfiguration cfg = plugin.getConfig();

        List<List<Integer>> list = TopManager.getIntegerListOfLists(cfg.getList("Signs.top_kills", new ArrayList<>()));
        List<Integer> locat = new ArrayList<>();
        locat.add(loc.getBlockX());
        locat.add(loc.getBlockY());
        locat.add(loc.getBlockZ());
        if (listContains(list, locat)) {
            p.sendMessage(LoggerUtil.gameMessage("Setup", "&3Sign removed"));
            list.remove(locat);
            cfg.set("Signs.top_wins", list);
            QuartzDefenders.getInstance().saveConfig();
            return;
        }
        list.add(locat);
        cfg.set("Signs.top_kills", list);

        p.sendMessage(LoggerUtil.gameMessage("Setup", "&bAdded new sign&f: " + "&f, X:&b" + loc.getX()
                + "&f,Y:&b" + loc.getY() + "&f,Z:&b" + loc.getZ()));
        QuartzDefenders.getInstance().saveConfig();
    }

    public void addSignW(Location loc, Player p) {
        FileConfiguration cfg = plugin.getConfig();

        List<List<Integer>> list = TopManager.getIntegerListOfLists(cfg.getList("Signs.top_wins", new ArrayList<>()));
        List<Integer> locat = new ArrayList<>();
        locat.add(loc.getBlockX());
        locat.add(loc.getBlockY());
        locat.add(loc.getBlockZ());
        if (listContains(list, locat)) {
            p.sendMessage(LoggerUtil.gameMessage("Setup", "&3Sign removed"));
            list.remove(locat);
            cfg.set("Signs.top_wins", list);
            QuartzDefenders.getInstance().saveConfig();
            return;
        }
        list.add(locat);
        cfg.set("Signs.top_wins", list);

        p.sendMessage(LoggerUtil.gameMessage("Setup", "&3Added new sign&f: " + "&f, X:&3" + loc.getX()
                + "&f,Y:&3" + loc.getY() + "&f,Z:&3" + loc.getZ()));
        QuartzDefenders.getInstance().saveConfig();
    }

    public void removeSigns() {
        FileConfiguration cfg = plugin.getConfig();
        cfg.set("Signs.top_wins", "{}");
        cfg.set("Signs.top_kills", "{}");
        QuartzDefenders.getInstance().saveConfig();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (plugin.getGame(p) != null) {
            plugin.getGame(p).reconnect(plugin.getGamePlayer(p));
            return;
        }
        p.setExp(0);
        p.setLevel(0);
        p.teleport(getLocation());
        sendTabList(p);
        for (Player pp : Bukkit.getOnlinePlayers()) {
            if (hide.contains(pp) && pp.getWorld().equals(location.getWorld()) && !p.hasPermission("QuartzDefenders.lobby.visible")) {
                pp.hidePlayer(plugin,p);
            }
            if (!pp.getWorld().equals(location.getWorld())) {
                p.hidePlayer(plugin,pp);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!plugin.getConfig().getBoolean("Lobby.disable_damage")) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        if (location.getWorld() == e.getEntity().getLocation().getWorld()) {
            e.setCancelled(true);
            if (e.getCause().equals(DamageCause.VOID)) {
                e.getEntity().setVelocity(new Vector(0, 0, 0));
                teleportToSpawn((Player) e.getEntity(), false);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (!plugin.getConfig().getBoolean("Lobby.infinity_food")) {
            return;
        }
        if (location.getWorld() == e.getEntity().getLocation().getWorld()) {
            e.setCancelled(true);
        }
        Game game = plugin.getGame((Player) e.getEntity());
        if (game != null && (game.getGameState().equals(Game.GameState.STARTING) || game.getGameState().equals(Game.GameState.LOBBY) || game.getGameState().equals(Game.GameState.WAITING))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getLocation().getWorld() != location.getWorld()) {
            return;
        }
        if (!e.getPlayer().hasPermission("QuartzDefenders.lobby.blockBreak")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getPlayer().getLocation().getWorld() != location.getWorld()) {
            return;
        }
        if (!e.getPlayer().hasPermission("QuartzDefenders.lobby.blockPlace")) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e) {
        if (e.getFrom() != location.getWorld()) {
            Player bp = e.getPlayer();
            e.getPlayer().setHealth(20);
            e.getPlayer().setFoodLevel(20);
            e.getPlayer().setExp(0);
            e.getPlayer().setLevel(0);

            bp.setGameMode(GameMode.ADVENTURE);

            bp.setHealth(20);
            bp.setFoodLevel(20);

            setLobbyTools(bp);

            GamePlayer p = plugin.getGamePlayer(e.getPlayer());
            p.resetDisplayName();
            if (p.getPlayer().hasPermission("QuartzDefenders.lobby.colorName")) {
                p.setDisplayName(ChatColor.AQUA);

            }

            sendTabList(bp);
            LobbySidebar sb = new LobbySidebar(plugin, bp);
            sb.setScoreboard();

            for (Player targ : location.getWorld().getPlayers()) {
            		targ.showPlayer(plugin, e.getPlayer());
                    e.getPlayer().showPlayer(plugin, targ);
            }
            for(Player targ : e.getFrom().getPlayers()) {
            	targ.hidePlayer(plugin, e.getPlayer());
            	e.getPlayer().hidePlayer(plugin, targ);
            }

        }

    }

}
