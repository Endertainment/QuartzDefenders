package ua.endertainment.quartzdefenders;

public class PermissionsList {	
	
	private static final char DOT = '.';
	
	private static final String QD = "QuartzDefenders";
	
	private static final String LOBBY = "lobby";
	private static final String GAME  = "game";
	private static final String TEAM  = "team";
	private static final String CHAT  = "chat";
	private static final String STATS = "stats";
	private static final String FUN   = "fun";
	private static final String KIT   = "kit";
	
	
	/*
	 * Lobby
	 */
	public static final String LOBBY_VISIBLE  		= QD + DOT + LOBBY + DOT + ".visible";
	public static final String LOBBY_COLOR_NAME  	= QD + DOT + LOBBY + DOT + ".colorName";
	public static final String LOBBY_BLOCK_PLACE	= QD + DOT + LOBBY + DOT + ".blockPlace";
	public static final String LOBBY_BLOCK_BREAK 	= QD + DOT + LOBBY + DOT + ".blockBreak";
	public static final String LOBBY_ALERT_JOIN  	= QD + DOT + LOBBY + DOT + ".alertJoin";
	public static final String LOBBY_ALERT_QUIT  	= QD + DOT + LOBBY + DOT + ".alertQuit";
	public static final String LOBBY_SETUP_SIGNS  	= QD + DOT + LOBBY + DOT + ".setupSigns";
	public static final String LOBBY_REMOVE_SIGNS  	= QD + DOT + LOBBY + DOT + ".removeSigns";
	
	/*
	 * Game
	 */
	public static final String GAME_BROADCAST 		= QD + DOT + GAME + DOT + "broadcast";
	public static final String GAME_ADD 			= QD + DOT + GAME + DOT + "addGame";
	public static final String GAME_END 			= QD + DOT + GAME + DOT + "endGame";
	public static final String GAME_START 			= QD + DOT + GAME + DOT + "startGame";
	public static final String GAME_LIST 			= QD + DOT + GAME + DOT + "gamesList";
	public static final String GAME_LOCK 			= QD + DOT + GAME + DOT + "lockGame";
	public static final String GAME_REMOVE 			= QD + DOT + GAME + DOT + "removeGame";
	public static final String GAME_ADMIN_GUI 		= QD + DOT + GAME + DOT + "adminGUI";
	public static final String GAME_ADMIN_JOIN		= QD + DOT + GAME + DOT + "adminJoin";
	public static final String GAME_SETUP_BLOCKS 	= QD + DOT + GAME + DOT + "setupRegenBlocks";
	public static final String GAME_SETUP_QUARTZ 	= QD + DOT + GAME + DOT + "setupQuartz";
	public static final String GAME_SETUP_SPAWN 	= QD + DOT + GAME + DOT + "setupSpawn";
	
	/*
	 * Kit
	 */
	public static final String KIT_GIVE 			= QD + DOT + KIT + DOT + "give";
	public static final String KIT_REMOVE 			= QD + DOT + KIT + DOT + "remove";
	
	
	
	/*
	 * Fun
	 */
	public static final String FUN_FIREWORK 		= QD + DOT + FUN + DOT + "firework";
	public static final String FUN_SET_NAME 		= QD + DOT + FUN + DOT + "setName";
	public static final String FUN_SET_TAB_NAME 	= QD + DOT + FUN + DOT + "setTabName";
	
	
	
	/*
	 * Team
	 */
	public static final String TEAM_BYPASS_JOIN		= QD + DOT + TEAM + DOT + "bypassJoin";
	public static final String TEAM_BALANCE_JOIN	= QD + DOT + TEAM + DOT + "balanceJoin";
	public static final String TEAM_RANDOM			= QD + DOT + TEAM + DOT + "random";
	public static final String TEAM_REMOVE_PLAYER	= QD + DOT + TEAM + DOT + "removePlayer";
	public static final String TEAM_ADD_PLAYER		= QD + DOT + TEAM + DOT + "addPlayer";
	public static final String TEAM_LOCK			= QD + DOT + TEAM + DOT + "lockTeam";
	public static final String TEAM_INFO			= QD + DOT + TEAM + DOT + "info";
	
	
	/*
	 * Chat
	 */
	public static final String CHAT_COLOR			= QD + DOT + CHAT + DOT + "colorMSG";
	
	/*
	 * Stats
	 */
	public static final String STATS_SET_PLAYED_GAMES 	= QD + DOT + STATS + DOT + "setPlayedGames";
	public static final String STATS_SET_PLACED_BLOCKS 	= QD + DOT + STATS + DOT + "setPlacedBlocks";
	public static final String STATS_SET_KILLS			= QD + DOT + STATS + DOT + "setKills";
	public static final String STATS_SET_DEATHS		 	= QD + DOT + STATS + DOT + "setDeaths";
	public static final String STATS_RESET				= QD + DOT + STATS + DOT + "reset";
	public static final String STATS_REMOVE_POINTS		= QD + DOT + STATS + DOT + "removePoints";
	public static final String STATS_REMOVE_COINS		= QD + DOT + STATS + DOT + "removeCoins";
	public static final String STATS_SEE_INFO			= QD + DOT + STATS + DOT + "seeInfo";
	public static final String STATS_ADD_POINTS			= QD + DOT + STATS + DOT + "addPoints";
	public static final String STATS_ADD_COINS 			= QD + DOT + STATS + DOT + "addCoins";
}
