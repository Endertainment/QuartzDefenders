package ua.Endertainment.QuartzDefenders.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TitleUtil {

    @SuppressWarnings("rawtypes")
    public static void sendTitle(Player p, String title, String subtitle, Integer stay) {
        try {
            int fadeIn = 1;
            int fadeOut = 1;
            stay *= 20;
            if (title != null) {
                title = ChatColor.translateAlternateColorCodes('&', title);

                Object e = NMSUtil.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);

                Object chatTitle = NMSUtil.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{\"text\":\"" + title + "\"}"});

                Constructor subtitleConstructor = NMSUtil.getNMSClass("PacketPlayOutTitle").getConstructor(new Class[]{NMSUtil.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                    NMSUtil.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE});
                Object titlePacket = subtitleConstructor.newInstance(new Object[]{e, chatTitle, Integer.valueOf(fadeIn), stay, Integer.valueOf(fadeOut)});
                NMSUtil.sendPacket(p, titlePacket);

                e = NMSUtil.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);

                chatTitle = NMSUtil.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{\"text\":\"" + title + "\"}"});

                subtitleConstructor = NMSUtil.getNMSClass("PacketPlayOutTitle").getConstructor(new Class[]{NMSUtil.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                    NMSUtil.getNMSClass("IChatBaseComponent")});
                titlePacket = subtitleConstructor.newInstance(new Object[]{e, chatTitle});
                NMSUtil.sendPacket(p, titlePacket);
            }
            if (subtitle != null) {
                subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);

                Object e = NMSUtil.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);

                Object chatSubtitle = NMSUtil.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{\"text\":\"" + title + "\"}"});

                Constructor subtitleConstructor = NMSUtil.getNMSClass("PacketPlayOutTitle").getConstructor(new Class[]{NMSUtil.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                    NMSUtil.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE});

                Object subtitlePacket = subtitleConstructor.newInstance(new Object[]{e, chatSubtitle, Integer.valueOf(fadeIn), stay, Integer.valueOf(fadeOut)});
                NMSUtil.sendPacket(p, subtitlePacket);

                e = NMSUtil.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);

                chatSubtitle = NMSUtil.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{\"text\":\"" + subtitle + "\"}"});

                subtitleConstructor = NMSUtil.getNMSClass("PacketPlayOutTitle").getConstructor(new Class[]{NMSUtil.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                    NMSUtil.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE});

                subtitlePacket = subtitleConstructor.newInstance(new Object[]{e, chatSubtitle, Integer.valueOf(fadeIn), stay, Integer.valueOf(fadeOut)});
                NMSUtil.sendPacket(p, subtitlePacket);
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }
    }

    public static void sendTitle(String title, String subtitle, Integer stay) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendTitle(p, title, subtitle, stay);
        }
    }

    public static void sendTabTitle(Player player, String header, String footer) {
        if (header == null) {
            header = "";
        }
        header = ChatColor.translateAlternateColorCodes('&', header);
        if (footer == null) {
            footer = "";
        }
        footer = ChatColor.translateAlternateColorCodes('&', footer);
        header = header.replaceAll("%player%", player.getDisplayName());
        footer = footer.replaceAll("%player%", player.getDisplayName());
        try {
            Object tabHeader = NMSUtil.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{\"text\":\"" + header + "\"}"});

            Object tabFooter = NMSUtil.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", new Class[]{String.class}).invoke(null, new Object[]{"{\"text\":\"" + footer + "\"}"});

            Constructor<?> titleConstructor = NMSUtil.getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(new Class[]{NMSUtil.getNMSClass("IChatBaseComponent")});
            Object packet = titleConstructor.newInstance(new Object[]{tabHeader});
            Field field = packet.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, tabFooter);
            NMSUtil.sendPacket(player, packet);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
