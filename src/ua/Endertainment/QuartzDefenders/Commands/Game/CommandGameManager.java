package ua.Endertainment.QuartzDefenders.Commands.Game;

import java.util.HashMap;

import ua.Endertainment.QuartzDefenders.Commands.SubCommand;

public class CommandGameManager {

	private static CommandGameManager cgm = new CommandGameManager();
	
	public static CommandGameManager getInstance() {
		return cgm;
	}
	
	private CommandGameManager() {
		subCommandsGame.put("help", new Help());
		subCommandsGame.put("add", new Add());
		subCommandsGame.put("remove", new Remove());
		subCommandsGame.put("lock", new Lock());
		subCommandsGame.put("start", new Start());
		subCommandsGame.put("end", new End());
		subCommandsGame.put("gameslist", new GamesList());
	}
	
	private HashMap<String, SubCommand> subCommandsGame = new HashMap<>();
	
	public SubCommand find(String name) {
		for(String s : subCommandsGame.keySet()) {
			if(name.equalsIgnoreCase(s)) return subCommandsGame.get(name);
		}
		return subCommandsGame.get(name);
	}
	
}
