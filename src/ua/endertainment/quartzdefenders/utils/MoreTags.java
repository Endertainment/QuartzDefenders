package ua.endertainment.quartzdefenders.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import ua.endertainment.quartzdefenders.QuartzDefenders;

// idk how to create default Bukkit's tags, so made my own class
public final class MoreTags {

    private static final QuartzDefenders PLUGIN = QuartzDefenders.getInstance();
    
    public static final CustomTag<Material> STAINED_GLASS = new CustomTag<>(
            new NamespacedKey(PLUGIN, "stained_glass"),
            Arrays.asList(
                    Material.WHITE_STAINED_GLASS,
                    Material.ORANGE_STAINED_GLASS,
                    Material.MAGENTA_STAINED_GLASS,
                    Material.LIGHT_BLUE_STAINED_GLASS,
                    Material.YELLOW_STAINED_GLASS,
                    Material.LIME_STAINED_GLASS,
                    Material.PINK_STAINED_GLASS,
                    Material.GRAY_STAINED_GLASS,
                    Material.LIGHT_GRAY_STAINED_GLASS,
                    Material.CYAN_STAINED_GLASS,
                    Material.PURPLE_STAINED_GLASS,
                    Material.BLUE_STAINED_GLASS,
                    Material.BROWN_STAINED_GLASS,
                    Material.GREEN_STAINED_GLASS,
                    Material.RED_STAINED_GLASS,
                    Material.BLACK_STAINED_GLASS
            )
    );
    
    public static final CustomTag<Material> STAINED_GLASS_PANES = new CustomTag<>(
            new NamespacedKey(PLUGIN, "stained_glass_panes"),
            Arrays.asList(
                    Material.WHITE_STAINED_GLASS_PANE,
                    Material.ORANGE_STAINED_GLASS_PANE,
                    Material.MAGENTA_STAINED_GLASS_PANE,
                    Material.LIGHT_BLUE_STAINED_GLASS_PANE,
                    Material.YELLOW_STAINED_GLASS_PANE,
                    Material.LIME_STAINED_GLASS_PANE,
                    Material.PINK_STAINED_GLASS_PANE,
                    Material.GRAY_STAINED_GLASS_PANE,
                    Material.LIGHT_GRAY_STAINED_GLASS_PANE,
                    Material.CYAN_STAINED_GLASS_PANE,
                    Material.PURPLE_STAINED_GLASS_PANE,
                    Material.BLUE_STAINED_GLASS_PANE,
                    Material.BROWN_STAINED_GLASS_PANE,
                    Material.GREEN_STAINED_GLASS_PANE,
                    Material.RED_STAINED_GLASS_PANE,
                    Material.BLACK_STAINED_GLASS_PANE
            )
    );

    public static class CustomTag<T extends Keyed> implements Tag<T> {
        
        private NamespacedKey key;
        private Set<T> values;
        
        public CustomTag(NamespacedKey key, Collection<T> values) {
            this.key = key;
            this.values = Collections.unmodifiableSet(new HashSet<T>(values));
        }

        @Override
        public Set getValues() {
            return values;
        }

        @Override
        public NamespacedKey getKey() {
            return key;
        }

        @Override
        public boolean isTagged(T t) {
            return values.contains(t);
        }
    
    }
}
