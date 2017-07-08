package ua.Endertainment.QuartzDefenders.Utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class MapManager {


	private String worldS;
	private World world;
	private File sourseDir;
	private File serverDir;
	
	private boolean success = false;
	
	public MapManager(String world) {
		this.worldS = world;
		this.sourseDir = new File(QuartzDefenders.getInstance().getDataFolder().getPath(), "maps" + File.separator + world);
		this.serverDir = new File(QuartzDefenders.getInstance().getServer().getWorldContainer().getAbsolutePath());
	}
	
	public void resetMap() {
		this.deleteMap();
		this.copyMap();
	}
	
	public void deleteMap() {
		World w = Bukkit.getWorld(worldS);
		if(w != null) {
			File worldFolder = new File(w.getWorldFolder().getPath());
			Bukkit.unloadWorld(w, false);
			try {
				FileUtils.deleteDirectory(worldFolder);
			} catch (IOException e) {
				LoggerUtil.logError("&cCould not delete a map " + worldS);
			}
		}
	}
	
	public void copyMap() {
		if(!isFolderExist()) {
			LoggerUtil.logError("&cCould not find a map " + worldS + " in " + sourseDir.toString())              ;
			return;
		}
		try {
			FileUtils.copyDirectory(sourseDir, serverDir);
		} catch (IOException e) {
			LoggerUtil.logError("&cCould not copy a world to server directory. Check config or console");
		}
		this.world = Bukkit.getServer().createWorld(new WorldCreator(worldS));
		this.success = true;
	}
	
	public World getWorld() {
		if(world == null) return null;
		return world;
	}
	
	public boolean isFolderExist() {
		if(sourseDir.exists()) return true;
		return false;
	}

	public boolean isSuccess() {
		return success;
	}

	
}
