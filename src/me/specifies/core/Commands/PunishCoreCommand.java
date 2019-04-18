package me.specifies.core.Commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.specifies.core.Punish;
import me.specifies.core.Utils.Inventories;

public class PunishCoreCommand implements CommandExecutor {
	
	Punish plugin;
	public PunishCoreCommand(Punish instance) {
		this.plugin = instance;
	}
	
	// Old PunishGame
	// Command: /punish
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		// Check sender instance, since this is a GUI plugin, console can't access
		if(!(sender instanceof Player)) {
			sender.sendMessage(plugin.color("&cYou have to be a player to perform this command."));
			return true;
		}
		
		// If not sender, cast
		Player p = (Player) sender;
		
		// Check main permissions. Note, this is intended as a simple GUI for staff to dish punishments. 
		// This plugin does not handle the actual punishment permissions.
		if(!p.hasPermission("punish.gui")) {
			p.sendMessage(plugin.color("&cYou don't have permission to execute this command."));
			return true;
		}
	
		// Args under or over 1. This command doesn't require more.
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
		
		// Reset the configuration to it's default value. Delete, save, send. Catch just incase.
		if(args[0].equalsIgnoreCase("setdefault") && p.hasPermission("punish.setdefault")) {
			try {
				File configPath = new File(plugin.getDataFolder(), "config.yml");
				configPath.delete();
				plugin.saveDefaultConfig();
				p.sendMessage(plugin.color("&aPunish configuration has been successfully set to its default."));
			}
			catch(Exception e) {
				p.sendMessage("&cThere was an internal error with resetting the configuration. For more information, refer to the console.");
				System.out.print(e);
			}
			return true;
		}
		
		// In v1.5, this was hand typed in code, but I decided to add a stringlist in configuration for higher levels of configuration.
		if(args[0].equalsIgnoreCase("help")) {
			for(String toSend : plugin.getConfig().getStringList("help-command")) {
				p.sendMessage(plugin.color(toSend));
			}
			return true;
		}
		
		// If player isn't online, return. May add something to punish offline players in the future.
		if(Bukkit.getPlayer(args[0]) == null) {
			p.sendMessage(plugin.color("&cThe player&7: &b" + args[0] + "&c is not currently online."));
			return true;
		}
		
		// Punishing target.
		Player target = Bukkit.getPlayer(args[0]);
		
		// Grab our inventories utility class, so we can open a new gui
		Inventories util = new Inventories();
		
		// OpenGUI with parameters, sender, target, and plugin.
		util.openGUI(p, target, plugin, "groups", "&7Punish-Choice: &e");
	
		
		return true;
	}

}
