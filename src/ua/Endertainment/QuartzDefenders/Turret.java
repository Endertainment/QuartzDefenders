package ua.Endertainment.QuartzDefenders;

public class Turret {

	private Game game;
	private GameTeam team;
	
	
	
	public Turret(Game game, GameTeam team) {
		this.game = game;
		this.team = team;
	}

	public Game getGame() {
		return game;
	}

	public GameTeam getTeam() {
		return team;
	}

	
	
}
