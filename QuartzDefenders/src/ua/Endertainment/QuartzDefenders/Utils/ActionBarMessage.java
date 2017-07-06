package ua.Endertainment.QuartzDefenders.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class ActionBarMessage {
	
	public ActionBarMessage(Player player, String message) {		
		
		sendActionBar(player, new ColorFormat(message).format());
		
	}

	private Class<?> getNMSClass(String classname) {
	    String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
	    String name = "net.minecraft.server." + version + classname;
	    Class<?> nmsClass = null;
	    try {
	        nmsClass = Class.forName(name);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    }
	    return nmsClass;
	}

	private Object getConnection(Player player) throws SecurityException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, InvocationTargetException
	{
	    Method getHandle = player.getClass().getMethod("getHandle");
	    Object nmsPlayer = getHandle.invoke(player);
	    Field conField = nmsPlayer.getClass().getField("playerConnection");
	    Object con = conField.get(nmsPlayer);
	    return con;
	}

	private void sendActionBar(Player player, String message) {
	    try {
	        Class<?> packetPlayOutChat = getNMSClass("PacketPlayOutChat");
	        Constructor<?> packetConstructor = packetPlayOutChat.getConstructor(getNMSClass("IChatBaseComponent"), byte.class);
	        Class<?> ichat = getNMSClass("IChatBaseComponent");
	        Class<?> chatSerializer = ichat.getClasses()[0];
	        Method csA = chatSerializer.getMethod("a", String.class);
	        Object component = csA.invoke(chatSerializer, "{\"text\": \"" + message + "\"}");
	        Object packet = packetConstructor.newInstance(component, (byte) 2);
	        Method sendPacket = getConnection(player).getClass().getMethod("sendPacket", (getNMSClass("Packet")));
	        sendPacket.invoke(packet);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
