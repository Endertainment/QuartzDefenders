package ua.endertainment.quartzdefenders.utils;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import ua.endertainment.quartzdefenders.QuartzDefenders;

public class MapManager {


	private String worldS;
	private World world;
	private File sourseDir;
	private File serverDir;
	
	private boolean success = false;
	
	public MapManager(String world) {
		this.worldS = world;
		this.sourseDir = new File(QuartzDefenders.getInstance().getDataFolder(), "maps" + File.separator + world);
		this.serverDir = QuartzDefenders.getInstance().getServer().getWorldContainer();
	}
	
	public void resetMap() {
		this.deleteMap();
		this.copyMap();
	}
	
	public void deleteMap() {
		World w = Bukkit.getWorld(worldS);
		if(w != null) {
			File worldFolder = w.getWorldFolder();
			Bukkit.unloadWorld(w, false);
			try {
				FileUtils.deleteDirectory(worldFolder);
			} catch (IOException e) {
				LoggerUtil.error(Language.getString("logger.delete_map_failed", new Replacer("{0}", worldS)));
			}
		}
	}
	
	public void copyMap() {
		if(!isFolderExist()) {
			LoggerUtil.error(Language.getString("logger.find_map_failed", new Replacer("{0}", worldS), new Replacer("{1}", sourseDir.toString())));
			return;
		}
		try {
			File srvDirF = new File(serverDir + File.separator + worldS);
			srvDirF.mkdir();
			FileUtils.copyDirectory(sourseDir, srvDirF);
		} catch (IOException e) {
			LoggerUtil.error(Language.getString("logger.copy_map_failed"));
		}
		this.world = Bukkit.getServer().createWorld(new WorldCreator(worldS));
                this.world.setTime(6000);
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
