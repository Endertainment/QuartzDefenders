package ua.endertainment.quartzdefenders.gui;

import java.util.List;
import javafx.util.Pair;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import ua.coolboy.quartzdefenders.voting.Vote;
import ua.coolboy.quartzdefenders.voting.VoteManager;
import ua.coolboy.quartzdefenders.voting.VoteObject;
import ua.coolboy.quartzdefenders.voting.VoteObject.Type;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GamePlayer;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.ItemUtil;
import ua.endertainment.quartzdefenders.utils.Language;

public class VoteGUI {

    private String title;
    private Inventory inventory;
    private VoteManager voteManager;
    private Game game;

    public VoteGUI(Game game) {
        this.game = game;
        voteManager = game.getVoteManager();
        int size = voteManager.getVoteObjects().size();
        if (size % 9 != 0) {
            size = (int) (Math.floor((double)size / 9) + 9); //nearby multiplier of 9
        }
        this.title = new ColorFormat(Language.getString("GUI.vote.name")).format();
        this.inventory = Bukkit.createInventory(new QuartzInventoryHolder(), size, title);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void openInventory(Player p) {
        if(game==null) return;
        for (VoteObject obj : voteManager.getVoteObjects()) {
            if (obj.getType().equals(Type.UNDEFINED) || !obj.isVoting()) {
                continue;
            }
            ItemStack item = addLore(obj.getType().getItem(), obj.getType());
            inventory.addItem(item);
        }
        p.openInventory(inventory);
    }

    public String getName() {
        return title;
    }
    
    public static void submitVote(Player player, Type type) {
        GamePlayer gp = QuartzDefenders.getInstance().getGamePlayer(player);
        Inventory inv = Bukkit.createInventory(new QuartzInventoryHolder(), 9, WordUtils.capitalize(type.getID().replace("_", " ")));
        
        Vote.VoteResult result = gp.getVote().getResultFor(type);
        
        ItemStack submit = new Wool(DyeColor.LIME).toItemStack(1);
        submit = ItemUtil.setName(submit, Language.getString("generic.yes"));
        
        ItemStack decline = new Wool(DyeColor.RED).toItemStack(1);
        decline = ItemUtil.setName(decline, Language.getString("generic.no"));
        
        if(result.equals(Vote.VoteResult.YES)) {
            submit = ItemUtil.addGlow(submit);
        }
        
        if(result.equals(Vote.VoteResult.NO)) {
            decline = ItemUtil.addGlow(decline);
        }
        
        inv.setItem(0, submit);
        inv.setItem(8, decline);
        
        player.openInventory(inv);
    }

    private ItemStack addLore(ItemStack stack, Type type) {
        ItemMeta meta = stack.getItemMeta();
        List<String> lore = meta.getLore();
        Pair<Integer, Integer> votes = voteManager.countVotes(type);
        String string = Language.getString("vote.count",votes.getKey(),votes.getValue());
        lore.add(string);
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return stack;
    }

}
