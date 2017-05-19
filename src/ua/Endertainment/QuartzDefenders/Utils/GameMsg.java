package ua.Endertainment.QuartzDefenders.Utils;

public class GameMsg {

	private static String prefix = new ColorFormat("&b&lQuartz &3\u00BB &8: &f").format();
	
	public static String getPrefix() {
		return prefix;
	}
	public static String gameMessage(String type, String message) {
		return new ColorFormat(getPrefix() + "&7" + type + " &8: &7" + message).format();
	}
	
}
