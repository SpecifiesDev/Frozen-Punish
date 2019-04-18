package me.specifies.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.specifies.core.Commands.PunishChat;
import me.specifies.core.Commands.PunishCoreCommand;
import me.specifies.core.Commands.PunishGame;
import me.specifies.core.Events.InventoryClick;

public class Punish extends JavaPlugin {
	
	
	public void onEnable() {
		this.saveDefaultConfig();
		registerCommands();
		registerEvents();
	}
	
	private void registerCommands() {
		getCommand("punish").setExecutor(new PunishCoreCommand(this));
		getCommand("punishgame").setExecutor(new PunishGame(this));
		getCommand("punishchat").setExecutor(new PunishChat(this));
	
	}
	
	private void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new InventoryClick(this), this);
	}
	
	// Utils
	
	public String color(String m) {
		return ChatColor.translateAlternateColorCodes('&', m);
	}
	
	public void reloadConfiguration() {
		this.reloadConfig();
	}

}
