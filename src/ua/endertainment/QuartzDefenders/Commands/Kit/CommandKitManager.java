package ua.endertainment.quartzdefenders.commands.kit;

import java.util.HashMap;

import ua.endertainment.quartzdefenders.commands.SubCommand;

public class CommandKitManager {

	private static CommandKitManager ckm = new CommandKitManager();
	
	public static CommandKitManager getInstance() {
		return ckm;
	}
	
	private CommandKitManager() {
		subCommandsKit.put("help", new Help());
		subCommandsKit.put("give", new Give());
		subCommandsKit.put("remove", new Remove());
	}
	
	private HashMap<String, SubCommand> subCommandsKit = new HashMap<>();
	
	public SubCommand find(String name) {
		for(String s : subCommandsKit.keySet()) {
			if(s.equalsIgnoreCase(name)) return subCommandsKit.get(s);
		}

		return subCommandsKit.get(name);
	}
	
}
