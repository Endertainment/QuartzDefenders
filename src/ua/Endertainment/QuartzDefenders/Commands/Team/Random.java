package ua.Endertainment.QuartzDefenders.Commands.Team;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.Game.GameState;
import ua.Endertainment.QuartzDefenders.GamePlayer;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Commands.SubCommand;
import ua.Endertainment.QuartzDefenders.Utils.GameMsg;

public class Random extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission("QuartzDefenders.team.random")) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(GameMsg.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}
		
		Player p = (Player) sender;
		
		Game game = QuartzDefenders.getInstance().getGame(p);
		
		if(!game.isGameState(GameState.WAITING)) {
			return;
		}
		
		java.util.Random rand = new java.util.Random();
		
		for(GamePlayer gp : game.getPlayers()) {
			int i = rand.nextInt(game.getTeamsCount()) + 1;
			
			getTeamByInt(game, i).joinTeam(gp);
			
		}
		
		sender.sendMessage(GameMsg.gameMessage("Chat", "&aRandomize complete"));
		
	}
	
	private GameTeam getTeamByInt(Game game, int i) {
		int x = 0;
		int y = i-1;
		Collection<GameTeam> coll = game.getTeams().values();
		
		for(GameTeam team : coll) {
			if(x == y) return team;
			x++;
		}
		
		return null;
	}

}
