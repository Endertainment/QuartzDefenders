package ua.endertainment.quartzdefenders.combo;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GamePlayer;

public class ComboManager {

    private Map<GamePlayer, Combo> comboList;

    public ComboManager() {
        this.comboList = new HashMap<>();
    }

    public Map<GamePlayer, Combo> getComboList() {
        return comboList;
    }

    public void start(GamePlayer p, Game game) {
        if (!comboList.containsKey(p)) {
            comboList.put(p, new Combo(p, game));
            return;
        }
        Combo c = comboList.get(p);
        if (c.compareTime()) {
            c.updateTime();
            c.up();
        } else {
            c.reset();
            c.up();
        }
    }
    
    public void reset() {
        comboList.clear();
    }

    public enum Kills {
        ZERO(ChatColor.WHITE+"Zero"),
        FIRST(ChatColor.YELLOW+"First"),
        DOUBLE(ChatColor.GOLD+"DOUBLE"),
        TRIPLE(ChatColor.BLUE+"TRIPLE"),
        ULTRA(ChatColor.RED+"ULTRA"),
        RAMPAGE(ChatColor.DARK_PURPLE+"RAMPAGE");
        
        private String name;
        private Kills(String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
    }

}
