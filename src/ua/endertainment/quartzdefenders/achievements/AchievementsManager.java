package ua.endertainment.quartzdefenders.achievements;

import java.util.ArrayList;


import ua.endertainment.quartzdefenders.QuartzDefenders;

public class AchievementsManager {

	QuartzDefenders plugin;
	
	private ArrayList<Achievement> achievements;
	
	public AchievementsManager(QuartzDefenders plugin) {
		this.plugin = plugin;
		this.achievements = new ArrayList<>();
		
		this.achievements.add(new FirstGameAchievement());
		// TODO more achievements
	}
	
	public ArrayList<Achievement> getAchievements() {
		return achievements;
	}
	
	/*public boolean isUnlocked(Achievement achievement, Player player) {
		FileConfiguration c = QuartzDefenders.getInstance().getConfigs().getAchvInfo();
		
		if(c.isList(player.getUniqueId().toString())) {
			ArrayList<String> l = (ArrayList<String>) c.getStringList(player.getUniqueId().toString());
			if(l.contains(achievement.getName())) {
				return true;
			}
		}
		
		return false;
	}
	
	public void unlockAchievement(Achievement achievement, Player player) {
		if(isUnlocked(achievement, player)) {
			return;
		}
		StatsPlayer sp = new StatsPlayer(player);
		sp.addCoins(achievement.getReward());
		
		FileConfiguration c = QuartzDefenders.getInstance().getConfigs().getAchvInfo();
		
		if(c.isList(player.getUniqueId().toString())) {
			ArrayList<String> l = (ArrayList<String>) c.getStringList(player.getUniqueId().toString());
			if(l.contains(achievement.getName())) {
				return;
			}
			l.add(achievement.getName());
			c.set(player.getUniqueId().toString(), l);
		} else {
			ArrayList<String> l = new ArrayList<>();
			l.add(achievement.getName());
			c.set(player.getUniqueId().toString(), l);
		}
		QuartzDefenders.getInstance().getConfigs().saveAchvInfo();
		player.sendMessage(LoggerUtil.gameMessage("AM", "Achievement get: &a" + achievement.getName()));
	}*/
	
}
