package ua.endertainment.quartzdefenders.commands.kit;

import java.util.LinkedHashMap;

import ua.endertainment.quartzdefenders.commands.SubCommand;

public class CommandKitManager {

	private static CommandKitManager ckm = new CommandKitManager();
	
	public static CommandKitManager getInstance() {
		return ckm;
	}
	
	private CommandKitManager() {
		subCommandsKit = new LinkedHashMap<>();
		subCommandsKit.put("help", new Help());
		subCommandsKit.put("give", new Give());
		subCommandsKit.put("remove", new Remove());
	}
	
	private static LinkedHashMap<String, SubCommand> subCommandsKit;
	
	public SubCommand find(String name) {
		for(String s : subCommandsKit.keySet()) {
			if(s.equalsIgnoreCase(name)) return subCommandsKit.get(s);
		}

		return subCommandsKit.get(name);
	}
	
	public static LinkedHashMap<String, SubCommand> getCommands() {
		return subCommandsKit;
	}
	
}
