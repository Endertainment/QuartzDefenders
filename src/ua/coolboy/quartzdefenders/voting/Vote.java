package ua.coolboy.quartzdefenders.voting;

import java.util.HashMap;
import java.util.Map;
import ua.coolboy.quartzdefenders.voting.VoteObject.Type;
import ua.endertainment.quartzdefenders.game.GamePlayer;

public class Vote {

    private GamePlayer player;
    private Map<Type, VoteResult> votes;

    public Vote(GamePlayer player) {
        this.player = player;
        votes = new HashMap<>();
        for(Type type : Type.values()) {
            votes.put(type, VoteResult.NOT_VOTED);
        }
    }
    
    public VoteResult getResultFor(Type type) {
        return votes.get(type);
    }
    
    public void voteFor(Type type, VoteResult vote) {
        votes.put(type, vote);
    }

    public enum VoteResult {
        YES, NOT_VOTED, NO;
        
        public Boolean asBoolean() {
            switch(this) {
                case YES: return true;
                case NO: return false;
                default: return null;
            }
        }
    }

}
