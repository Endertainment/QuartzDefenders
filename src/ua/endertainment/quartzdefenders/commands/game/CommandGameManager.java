package ua.endertainment.quartzdefenders.commands.game;

import java.util.HashMap;
import java.util.LinkedHashMap;

import ua.endertainment.quartzdefenders.commands.SubCommand;

public class CommandGameManager {

	private static CommandGameManager cgm = new CommandGameManager();
	
	public static CommandGameManager getInstance() {
		return cgm;
	}
	
	private CommandGameManager() {
		subCommandsGame = new LinkedHashMap<>();
		
		subCommandsGame.put("help", new Help());
		subCommandsGame.put("add", new Add());
		subCommandsGame.put("remove", new Remove());
		subCommandsGame.put("lock", new Lock());
		subCommandsGame.put("start", new Start());
		subCommandsGame.put("end", new End());
		subCommandsGame.put("gameslist", new GamesList());
		subCommandsGame.put("setupores", new SetupOres());
		subCommandsGame.put("setupquartz", new SetupQuartz());
		subCommandsGame.put("setupspawn", new SetupSpawn());
        subCommandsGame.put("admin", new Admin());
	}
	
	private static LinkedHashMap<String, SubCommand> subCommandsGame;
	
	public SubCommand find(String name) {
		for(String s : subCommandsGame.keySet()) {
			if(s.equalsIgnoreCase(name)) return subCommandsGame.get(s);
		}
		return subCommandsGame.get(name);
	}
	
	public static LinkedHashMap<String, SubCommand> getCommands() {
		return subCommandsGame;
	}
	
}
