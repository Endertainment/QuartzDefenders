package ua.Endertainment.QuartzDefenders.Commands.Kit;

import java.util.HashMap;

import ua.Endertainment.QuartzDefenders.Commands.SubCommand;

public class CommandKitManager {

	private static CommandKitManager ckm = new CommandKitManager();
	
	public static CommandKitManager getInstance() {
		return ckm;
	}
	
	private CommandKitManager() {
		subCommandsKit.put("help", new Help());
		subCommandsKit.put("give", new Give());
	}
	
	private HashMap<String, SubCommand> subCommandsKit = new HashMap<>();
	
	public SubCommand find(String name) {
		for(String s : subCommandsKit.keySet()) {
			if(name.equalsIgnoreCase(s)) return subCommandsKit.get(name);
		}

		return subCommandsKit.get(name);
	}
	
}
