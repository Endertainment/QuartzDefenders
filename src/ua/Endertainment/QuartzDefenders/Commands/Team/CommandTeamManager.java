package ua.Endertainment.QuartzDefenders.Commands.Team;

import java.util.HashMap;
import java.util.Map;

import ua.Endertainment.QuartzDefenders.Commands.SubCommand;

public class CommandTeamManager {

	private static CommandTeamManager ctm = new CommandTeamManager();
	public static CommandTeamManager getInstance() {
		return ctm;
	}
	
	private CommandTeamManager() {
		subCommandsTeam.put("help", new Help());
		subCommandsTeam.put("join", new Join());
		subCommandsTeam.put("quit", new Quit());
		subCommandsTeam.put("lock", new Lock());
		subCommandsTeam.put("info", new Info());
	}
	
	private Map<String, SubCommand> subCommandsTeam = new HashMap<>();
	
	public SubCommand find(String name) {
		for(String s : subCommandsTeam.keySet()) {
			if(name.equalsIgnoreCase(s)) return subCommandsTeam.get(name);
		}

		return subCommandsTeam.get(name);
	}
	
	
}
