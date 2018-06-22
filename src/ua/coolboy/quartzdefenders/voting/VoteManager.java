package ua.coolboy.quartzdefenders.voting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.util.Pair;
import javax.annotation.Nullable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import ua.coolboy.quartzdefenders.voting.Vote.VoteResult;
import ua.coolboy.quartzdefenders.voting.VoteObject.Type;
import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GamePlayer;

public class VoteManager {

    private List<VoteObject> objects;
    private Game game;

    public VoteManager(FileConfiguration config, Game game) {
        this.game = game;
        ConfigurationSection section = config.getConfigurationSection("Games."+game.getGameId()+".voting");
        objects = new ArrayList<>();
        for (String key : section.getKeys(false)) {
            Type type = Type.fromID(key);
            if (section.isBoolean(key)) {
                objects.add(new VoteObject(type, Default.fromBoolean(section.getBoolean(key))));
            } else {
                String value = section.getString(key, "vote");
                if (value.equalsIgnoreCase("vote")) {
                    objects.add(new VoteObject(type, Default.NOT_SET));
                }
            }
        }
    }
    
    public List<VoteObject> getVoteObjects() {
        return Collections.unmodifiableList(objects);
    }
    
    public Map<VoteObject, VoteResult> getResults() {
        Map<VoteObject, VoteResult> map = new HashMap<>();
        for(VoteObject object : objects) {
            Pair<Integer,Integer> votes = countVotes(object.getType());
            VoteResult result = VoteResult.NOT_VOTED;
            if(votes.getKey()>votes.getValue()) result = VoteResult.YES;
            if(votes.getKey()<votes.getValue()) result = VoteResult.NO;
            if(votes.getKey().equals(votes.getValue())) result = VoteResult.NOT_VOTED;
            map.put(object, result);
        }
        return map;
    }
    
    /**
     *
     * @param game - Game
     * @param type - VoteType
     * @return Yes and no
     */
    public Pair<Integer, Integer> countVotes(Type type) {
        int yes = 0, no = 0;
        for (GamePlayer player : game.getPlayers()) {
            if(!game.isInTeam(player)) continue;
            Vote.VoteResult result = player.getVote().getResultFor(type);
            if (result.equals(Vote.VoteResult.NO)) {
                no++;
            }
            if (result.equals(Vote.VoteResult.YES)) {
                yes++;
            }
        }
        return new Pair<>(yes, no);
    }

    public enum Default {
        YES, NOT_SET, NO;
        private Random random = new Random();
        @Nullable
        public boolean asBoolean() {
            switch (this) {
                case YES:
                    return true;
                case NO:
                    return false;
                default:
                    return random.nextBoolean();
            }
        }

        public static Default fromBoolean(boolean bool) {
            return bool ? Default.YES : Default.NO;
        }
    }

}
