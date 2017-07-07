package ua.Endertainment.QuartzDefenders.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;


public class ActionBarMessage {
	
	public ActionBarMessage(Player player, String message) {		
		
		sendActionBar(player, new ColorFormat(message).format());
		
	}

	private void sendActionBar(Player player, String message) {
	    try {
	        Class<?> packetPlayOutChat = NMSUtil.getNMSClass("PacketPlayOutChat");
	        Constructor<?> packetConstructor = packetPlayOutChat.getConstructor(NMSUtil.getNMSClass("IChatBaseComponent"), byte.class);
	        Class<?> ichat = NMSUtil.getNMSClass("IChatBaseComponent");
	        Class<?> chatSerializer = ichat.getClasses()[0];
	        Method csA = chatSerializer.getMethod("a", String.class);
	        Object component = csA.invoke(chatSerializer, "{\"text\": \"" + message + "\"}");
	        Object packet = packetConstructor.newInstance(component, (byte) 2);
	        Method sendPacket = NMSUtil.getConnection(player).getClass().getMethod("sendPacket", (NMSUtil.getNMSClass("Packet")));
	        sendPacket.invoke(packet);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}