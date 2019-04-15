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
		
		Player p = (Player) e.getWhoClicked();
		
		Inventory inv = e.getInventory();
		
		if(!inv.getName().contains(ChatColor.stripColor("Punish: "))) {
			return;
		}
		
		e.setCancelled(true);
		
		if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) {
			return;
		}
		
		ItemStack item = e.getCurrentItem();
		
		String title = strip(inv.getName());
		
		String[] titleParts = title.split(" ");
		
		FileConfiguration conf = plugin.getConfig();
		
		int groupsInConfig = conf.getConfigurationSection("groups").getKeys(false).size();
		
		List<String> groups = new ArrayList();
		for(String groupTag : conf.getConfigurationSection("groups").getKeys(false)) {
			groups.add(groupTag);
		}
		
		for(int i = 0; i < groupsInConfig; i++) {
			
			String group = (String) groups.get(i);
			
			if(item.getType() == Material.valueOf(conf.getString("groups." + group + ".item")) & !conf.getBoolean("groups." + group + ".decorative")) {
				p.closeInventory();
				
				List<String> commands = new ArrayList();
				for(String command : conf.getStringList("groups." + group + ".commands")) {
					commands.add(command.replace("{PLAYER}", titleParts[1]));
				}
				
				for(int j = 0; j < commands.size(); j++) {
					Bukkit.dispatchCommand(p, (String) commands.get(j));
				}
				
			}
			
		}
	}
	
	
	private String strip(String m) {
		return ChatColor.stripColor(m);
	}

}
