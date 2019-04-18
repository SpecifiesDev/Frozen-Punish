package me.specifies.core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.specifies.core.Punish;
import me.specifies.core.Utils.Inventories;

public class PunishChat implements CommandExecutor {
	
	Punish plugin;
	public PunishChat(Punish instance) {
		this.plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(plugin.color("&cYou have to be a player to perform this command."));
			return true;
		}
		
		Player p = (Player) sender;
		
		if(!p.hasPermission("punish.chat")) {
			p.sendMessage(plugin.color("&cYou don't have permission to execute this command."));
			return true;
		}
		
		if(args.length < 1 || args.length > 1) {
			p.sendMessage(plugin.color("&cInvalid arguments. /punishchat <player>"));
			return true;
		}
		
		// Exclude help , reload & setdefault
		
		if(Bukkit.getPlayer(args[0]) == null) {
			p.sendMessage(plugin.color("&cThe player&7: &b" + args[0] + "&c is not currently online."));
			return true;
		}
		
		Player target = Bukkit.getPlayer(args[0]);
		
		Inventories util = new Inventories();
		
		util.openGUI(p, target, plugin, "punishchat", "&7Punish-Chat: &e");
		
		return true;
	}

}
