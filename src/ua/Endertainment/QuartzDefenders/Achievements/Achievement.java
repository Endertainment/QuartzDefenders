package ua.Endertainment.QuartzDefenders.Achievements;

import java.util.ArrayList;

public abstract class Achievement {

	private String name;
	private boolean secret;
	private int reward;
	private ArrayList<String> descriptions;
	
	public Achievement(String name, boolean secret, int reward, String... description) {
		this.name = name;
		this.secret = secret;
		this.reward = reward;
		for(String s : description) {
			this.descriptions.add(s);
		}
	}
	
	public final String getName() {
		return name;
	}
	
	public final boolean isSecret() {
		return secret;
	}
	
	public final int getReward() {
		return reward;
	}
	
	public final ArrayList<String> getDscription() {
		return descriptions;
	}
	
	
}
