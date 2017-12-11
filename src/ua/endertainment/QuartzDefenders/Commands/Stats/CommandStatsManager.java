package ua.endertainment.quartzdefenders.commands.stats;

import java.util.HashMap;

import ua.endertainment.quartzdefenders.commands.SubCommand;

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
		subCommandsStats.put("reset", new Reset());
		subCommandsStats.put("setkills", new SetKills());
		subCommandsStats.put("setdeaths", new SetDeaths());
		subCommandsStats.put("setplayedgames", new SetPlayedGames());
		subCommandsStats.put("setplacedblocks", new SetPlacedBlocks());
	}
	
	private HashMap<String, SubCommand> subCommandsStats = new HashMap<>();
	
	public SubCommand find(String name) {
		for(String s : subCommandsStats.keySet()) {
			if(s.equalsIgnoreCase(name)) return subCommandsStats.get(s);
		}

		return subCommandsStats.get(name);
	}
}
