package ua.endertainment.quartzdefenders.Commands.Kit;

import org.bukkit.command.CommandSender;

import ua.endertainment.quartzdefenders.commands.SubCommand;
import ua.endertainment.quartzdefenders.utils.ColorFormat;

public class Help extends SubCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		sender.sendMessage(new ColorFormat("&8-------------------- &3Help &8--------------------").format());
		sender.sendMessage(new ColorFormat("&8» &b/kit help &8- &bShow help").format());
		sender.sendMessage(new ColorFormat("&8» &b/kit give <kit> [player] &8- &bGive kit to player").format());
		sender.sendMessage(new ColorFormat("&8» &b/kit remove <kit> [player] &8- &bRemove kit from player").format());
	}

}
