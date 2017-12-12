package ua.endertainment.quartzdefenders.commands.team;

import java.util.HashMap;
import java.util.Map;

import ua.endertainment.quartzdefenders.commands.SubCommand;

public class CommandTeamManager {

	private static CommandTeamManager ctm = new CommandTeamManager();
	public static CommandTeamManager getInstance() {
		return ctm;
	}
	
	private CommandTeamManager() {
		subCommandsTeam.put("help", new Help());
		subCommandsTeam.put("join", new Join());
		subCommandsTeam.put("quit", new Quit());
		subCommandsTeam.put("leave", new Quit());
		subCommandsTeam.put("kick", new Quit());
		subCommandsTeam.put("lock", new Lock());
		subCommandsTeam.put("info", new Info());
		subCommandsTeam.put("random", new Random());
	}
	
	private Map<String, SubCommand> subCommandsTeam = new HashMap<>();
	
	public SubCommand find(String name) {
		for(String s : subCommandsTeam.keySet()) {
			if(s.equalsIgnoreCase(name)) return subCommandsTeam.get(s);
		}

		return subCommandsTeam.get(name);
	}
	
	
}
