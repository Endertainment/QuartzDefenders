package ua.Endertainment.QuartzDefenders.Utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class MapManager {


	private String world;
	private File sourseDir;
	private File serverDir;
	
	private boolean success = false;
	
	public MapManager(String world) {
		this.world = world;
		this.sourseDir = new File(QuartzDefenders.getInstance().getDataFolder().getPath(), "maps" + File.separator + world);
		this.serverDir = new File(QuartzDefenders.getInstance().getServer().getWorldContainer().getAbsolutePath());
	}
	
	public void resetMap() {
		this.deleteMap();
		this.copyMap();
	}
	
	public void deleteMap() {
		World w = Bukkit.getWorld(world);
		if(w != null) {
			File worldFolder = new File(w.getWorldFolder().getPath());
			Bukkit.unloadWorld(w, false);
			try {
				FileUtils.deleteDirectory(worldFolder);
			} catch (IOException e) {
				Bukkit.broadcastMessage(GameMsg.gameMessage("Error", "&cCould not delete a map " + world));
			}
		}
	}
	
	public void copyMap() {
		if(!isFolderExist()) {
			Bukkit.broadcastMessage(GameMsg.gameMessage("Error", "&cCould not find a map " + world + " in " + sourseDir.toString()));
			return;
		}
		try {
			FileUtils.copyDirectory(sourseDir, serverDir);
		} catch (IOException e) {
			Bukkit.broadcastMessage(GameMsg.gameMessage("Error", "&cCould not copy a world to server directory. Check config or console"));
		}
		Bukkit.getServer().createWorld(new WorldCreator(world));
		this.success = true;
	}
	
	public boolean isFolderExist() {
		if(sourseDir.exists()) return true;
		return false;
	}

	public boolean isSuccess() {
		return success;
	}

	
}
