package ua.Endertainment.QuartzDefenders.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

public abstract class NMSUtil {

    public static Class<?> getNMSClass(String name) {
        String version = org.bukkit.Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", new Class[]{NMSUtil.getNMSClass("Packet")}).invoke(playerConnection, new Object[]{packet});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InvocationTargetException {
        Method getHandle = player.getClass().getMethod("getHandle");
        Object nmsPlayer = getHandle.invoke(player);
        Field conField = nmsPlayer.getClass().getField("playerConnection");
        Object con = conField.get(nmsPlayer);
        return con;
    }

    public static void setDamage(Arrow arrow, int damage) {
        try {
            Constructor<?> constructor = getNMSClass("NBTTagCompound").getConstructor();
            Object tag = constructor.newInstance();
            Method setDouble = tag.getClass().getDeclaredMethod("setDouble", String.class, Double.TYPE);
            setDouble.invoke(tag, "damage", 4);
            getNMSClass("EntityArrow").getMethod("b", tag.getClass()).invoke(getNMSClass("EntityArrow"), tag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
