package me.specifies.core.Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.specifies.core.Punish;

public class InventoryClick implements Listener {
	
	Punish plugin;
	public InventoryClick(Punish instance) {
		this.plugin = instance;
	}
	
	@EventHandler
	public void click(InventoryClickEvent e) {
		
		// For this method, much of this is the basics of Inventories.openGUI(), so I'll document what hasn't been.
		
		Player p = (Player) e.getWhoClicked();
		
		Inventory inv = e.getInventory();
		
		// Main punish screen
		if(inv.getName().contains(strip("Punish-Choice: "))) {
			
			loopGroup("groups", e, inv, p);
			
		}
		if(inv.getName().contains(strip("Punish-Game: "))) {
			
			loopGroup("punishgame", e, inv, p);
		}
		
		if(inv.getName().contains(strip("Punish-Chat: "))) {
			
			loopGroup("punishchat", e, inv, p);
		}
		
	
	}
	
	
	private String strip(String m) {
		return ChatColor.stripColor(m);
	}
	
	private void loopGroup(String groupS, InventoryClickEvent e, Inventory inv, Player p) {
		
		// cancel the take event
		e.setCancelled(true);
		
		String groupData = groupS + ".";
		
		// prevent NPEs
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
			return;
		}
		
		ItemStack item = e.getCurrentItem();
	
		String title = strip(inv.getName());
	    
		// Here, we're splitting "title" into an arraylist of Strings that are seperated between " "
		// So if our title is "Punish: Specifies" then parts[0] is "Punish:" and parts[1] is "Specifies"
		String[] titleParts = title.split(" ");
		
		FileConfiguration conf = plugin.getConfig();
		
		int groupsInConfig = conf.getConfigurationSection(groupS).getKeys(false).size();
		
		List<String> groups = new ArrayList();
		for(String groupTag : conf.getConfigurationSection(groupS).getKeys(false)) {
			groups.add(groupTag);
		}
		
		for(int i = 0; i < groupsInConfig; i++) {
			
			String group = (String) groups.get(i);
			
			// Check if it's a command item essentially, and if it's one in our config.

			if(item.getType() == Material.valueOf(conf.getString(groupData + group + ".item")) & !conf.getBoolean(groupData + group + ".decorative")) {
				p.closeInventory();
				
				List<String> commands = new ArrayList();
				for(String command : conf.getStringList(groupData + group + ".commands")) {
					commands.add(command.replace("{PLAYER}", titleParts[1]));
				}
				
				for(int j = 0; j < commands.size(); j++) {
					Bukkit.dispatchCommand(p, (String) commands.get(j));
				}
				
			}
			
		}
	}

}
