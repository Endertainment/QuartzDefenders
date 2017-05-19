package ua.Endertainment.QuartzDefenders.Utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import net.minecraft.server.v1_11_R1.PacketPlayOutTitle;

public class ActionBarMessage {

	
	private String version;
	
	public ActionBarMessage(Player player, String s) {
		
		this.version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		 
		PacketPlayOutChat packet = new PacketPlayOutChat(ChatSerializer.a(""));
		 
		Class<?> packetPlayOutChat;
		Class<?> iChatBaseComponent;
		Class<?> chatSerializer;
		 
		Object o;
		
		Constructor<?> aBar;
		try {
			packetPlayOutChat = Class.forName("net.minecraft.server." + version + "." + "PacketPlayOutChat");
			iChatBaseComponent = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
			chatSerializer = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat.ChatSerializer");
			
			s = new ColorFormat(s).format();
			
			o = iChatBaseComponent.getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + s + "\"}");
			
			//aBar = packetPlayOutChat.getConstructor(packetPlayOutChat.getDeclaredClasses()[2], 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		sendPacket(player, packet);
	}

	private void sendPacket(Player player, Object packet) {
        try {
        	Class<?> packetC = Class.forName("net.minecraft.server." + version + "." + "Packet");
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", packetC).invoke(playerConnection, packet);
        }
       
        catch (Exception e) {
            e.printStackTrace();
        }
	}
}
