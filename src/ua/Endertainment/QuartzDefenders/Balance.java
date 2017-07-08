package ua.Endertainment.QuartzDefenders;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import ua.Endertainment.QuartzDefenders.Game.BalanceType;
import ua.Endertainment.QuartzDefenders.Stats.StatsManager;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;

public class Balance {

	private Game game;
	private BalanceType type;
	private GameTeam chooseTeam;
	private Set<GameTeam> setOfTeams;
	private GamePlayer player;
	
	public Balance(Game game, BalanceType balanceType, GamePlayer player, Collection<GameTeam> setOfTeams, GameTeam chooseTeam) {
		this.game = game;
		this.type = balanceType;
		this.chooseTeam = chooseTeam;
		this.player = player;
		this.setOfTeams = new HashSet<>();
		for(GameTeam team : setOfTeams) {
			if(team != this.chooseTeam) 
				this.setOfTeams.add(team); 
		}		
	}
	Game getGame() {
		return game;
	}
	public boolean isTeamsBalanced() {
		switch(type) {
		case DEFAULT_BALANCE:
			return this.DEFAULT_BALANCE();
		case NO_BALANCE:
			return this.NO_BALANCE();
		case TEAM_KD_BALANCE:
			return this.TEAM_KD_BALANCE();
		default:
			break;
		}
		return false;
	}
	
	private boolean NO_BALANCE() {
		return true;
	}
	
	private boolean DEFAULT_BALANCE() {
		if(player.getPlayer().hasPermission("QuartzDefenders.team.balanceJoin")) return true; 
		for(GameTeam team : setOfTeams) {			
			if(this.chooseTeam.getPlayers().size() > team.getPlayers().size()) {
				player.sendMessage(LoggerUtil.gameMessage("Team", "Teams inbalanced. Choose another team"));
				return false;
			}
		}
		return true;
	}
	
	private boolean TEAM_KD_BALANCE() {
		if(player.getPlayer().hasPermission("QuartzDefenders.team.balanceJoin")) return true;
		for(GameTeam team : setOfTeams) {
			if(StatsManager.getTeamKDR(chooseTeam) > ( StatsManager.getTeamKDR(team) + 1 ) ) {
				player.sendMessage(LoggerUtil.gameMessage("Team", "Teams inbalanced. Choose another team"));
				return false;
			}
		}
		return true;
	}
}
