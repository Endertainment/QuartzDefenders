package ua.Endertainment.QuartzDefenders.Commands.Stats;

import java.util.HashMap;

import ua.Endertainment.QuartzDefenders.Commands.SubCommand;

public class CommandStatsManager {

	private static CommandStatsManager csm = new CommandStatsManager();
	public static CommandStatsManager getInstance() {
		return csm;
	}
	
	private CommandStatsManager() {
		subCommandsStats.put("info", new Info());
		subCommandsStats.put("help", new Help());
		subCommandsStats.put("addcoins", new AddCoins());
		subCommandsStats.put("removecoins", new RemoveCoins());
		subCommandsStats.put("addpoints", new AddPoints());
		subCommandsStats.put("removepoints", new RemovePoints());
	}
	
	private HashMap<String, SubCommand> subCommandsStats = new HashMap<>();
	
	public SubCommand find(String name) {
		for(String s : subCommandsStats.keySet()) {
			if(name.equalsIgnoreCase(s)) return subCommandsStats.get(name);
		}

		return subCommandsStats.get(name);
	}
}
