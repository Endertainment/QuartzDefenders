package ua.endertainment.quartzdefenders.game;

import ua.endertainment.quartzdefenders.game.Game;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import ua.endertainment.quartzdefenders.game.Game.GameState;
import ua.endertainment.quartzdefenders.utils.ColorFormat;

public class GameSidebar {

	
	private Game game;
	
	private Scoreboard scoreboard;
	private Objective objective;
	private DisplaySlot slot = DisplaySlot.SIDEBAR;
	
	private int index;
	
	private String title = new ColorFormat("&3«&b&l Quartz Defenders &3»").format();	
	
	public GameSidebar(Game game, Scoreboard scoreboard) {
		this.game = game;
		this.scoreboard = scoreboard;
		this.objective = this.scoreboard.registerNewObjective("GameBar", "MEOW");
		
		this.index = 0;
		build(game.getGameState());		
	}
	
	public void refresh() {
		this.index = 0;
		build(game.getGameState());
	}
	
	
	private void build(GameState state) {
		objective.unregister();
		objective = this.scoreboard.registerNewObjective("GameBar", "dummy");
		objective.setDisplayName(title);
		objective.setDisplaySlot(slot);
		
		addString("&fGame: &b" + game.getGameName());
		addString("&f&m------------------");
		
		switch(state) {
                        case LOBBY:
			case WAITING : {
				addString("&fPlayers:");
				for(GameTeam team : game.getTeams().values()) 
					addString("&f  " + team.getName() + "&f: " + team.getColor() + team.getPlayersSize() + "&f/" + team.getColor() + game.getPlayersInTeam());
				break;
			}
			case STARTING: {
				addString("&f  &6 ");
				addString("    &fGame starting");
				addString("&3  &6 ");
				break;
			}
			case ACTIVE: {
				for(GameTeam team : game.getTeams().values()) 
					addString("&f  " + team.getName() + "&f: " + team.getColor() + game.getQuartz(team).getQuartzHealth() + "&f/" + team.getColor() + game.getQuartzHealth());
				break;
			}
			case ENDING: {
				addString("&f  &6 ");
				addString("    &fGame ended");
				addString("&3  &6 ");
				break;
			}
			default:
				break;
		}
		
		addString("&f&m------------------&6 ");
		addString("&3«&b&l Playcraft.com.ua &3»");		
	}
	
	
	private void addString(String s) {
		Score score = objective.getScore(new ColorFormat(s).format());
		score.setScore(index);
		index--;
	}	
	
}
/* < QuartzDefenders >
 * Game: gameName;
 * ----------------
 * Teams:
 *   Red: 75/100   
 * 	 Blue: 72/100
 * ----------------
 * Playcraft.COM.UA
 */