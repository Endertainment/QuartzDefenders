package ua.endertainment.quartzdefenders.achievements;

import java.util.ArrayList;
import java.util.List;

public abstract class Achievement {

	private final String name;
	private final boolean secret;
	private final int reward;
	private final List<String> descriptions;
	
	public Achievement(String name, boolean secret, int reward, String... description) {
		this.name = name;
		this.secret = secret;
		this.reward = reward;
                this.descriptions = new ArrayList<>();
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
	
	public final List<String> getDescription() {
		return descriptions;
	}
	
	
}
