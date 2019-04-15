package me.specifies.core.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.specifies.core.Punish;
import me.specifies.core.Utils.Inventories;

public class PunishGame implements CommandExecutor {
	
	Punish plugin;
	public PunishGame(Punish instance) {
		this.plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(plugin.color("&cYou have to be a player to perform this command."));
			return true;
		}
		
		Player p = (Player) sender;
		
		if(!p.hasPermission("punish.gui")) {
			p.sendMessage(plugin.color("&cYou don't have permission to execute this command."));
			return true;
		}
	
			
		if(args.length < 1 || args.length > 1) {
			p.sendMessage(plugin.color("&cInvalid arguments. Do /punish <player>. For more assistance, type /punish help"));
			return true;
		}
			
		// Reload configuration, with some catch instances
		if(args[0].equalsIgnoreCase("reload") && p.hasPermission("punish.reload")) {
			try {
				plugin.reloadConfiguration();
				p.sendMessage(plugin.color("&aPunish configuration has been reloaded successfully."));
			} 
			catch(Exception e) {
				p.sendMessage(plugin.color("&cThere was an internal error with reloading the configuration. For more information, refer to the server logs."));
				System.out.print(e);
			}
			return true;
		}
		
		if(args[0].equalsIgnoreCase("help")) {
			p.sendMessage(plugin.color("&b&m======&7[&bFrozen Punish&7]&b&m======"));
			p.sendMessage(plugin.color("&b/punish <player>"));
			p.sendMessage(plugin.color("&7Open a GUI containing punishments you can provide a player."));
			p.sendMessage("");
			p.sendMessage(plugin.color("&b/punish reload"));
			p.sendMessage(plugin.color("&7Admin command to reload the punishment configuration."));
			p.sendMessage(plugin.color("&b&m========================="));
			return true;
		}
			
		if(Bukkit.getPlayer(args[0]) == null) {
			p.sendMessage(plugin.color("&cThe player has never played or is not currently online."));
			return true;
		}
			
		Player target = Bukkit.getPlayer(args[0]);
		
		Inventories util = new Inventories();
		
		util.openGUI(p, target, plugin);
	
		
		return true;
	}

}
