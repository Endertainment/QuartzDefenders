package ua.endertainment.quartzdefenders.commands.team;

import java.util.Collection;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.Game.GameState;
import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.PermissionsList;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.Symbols;

public class Random extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(!sender.hasPermission(PermissionsList.TEAM_RANDOM)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou do not have permissions"));
			return;
		}
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cOnly players can use this command"));
			return;
		}
		
		Player p = (Player) sender;
		
		Game game = QuartzDefenders.getInstance().getGame(p);
		
		if(game == null) {
			sender.sendMessage(LoggerUtil.gameMessage("Chat", "&cYou can not use this command then you is not in game"));
			return;
		}
		
		if(!game.isGameState(GameState.WAITING)) {
			return;
		}
		
		java.util.Random rand = new java.util.Random();
		
		int z = 0;
		
		for(GamePlayer gp : game.getPlayers()) {
			int i = rand.nextInt(game.getTeamsCount()) + 1;
			if(i == z) i = rand.nextInt(game.getTeamsCount()) + 1;
			z = i;
			getTeamByInt(game, i).joinTeam(gp, true);
			
		}
		
		sender.sendMessage(LoggerUtil.gameMessage("Chat", "&aRandomize complete"));
		
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

	@Override
	public String getUsage() {
		return new ColorFormat("&8"+Symbols.RIGHT_QUOTE+" &b/team random &8- &bRandomly sort players in teams").format();
	}

}
