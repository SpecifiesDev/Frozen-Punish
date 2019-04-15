package me.specifies.core.Utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import me.specifies.core.Punish;

public class Inventories {
	
	public void openGUI(Player sender, Player target, Punish plugin) {
		Inventory inv = Bukkit.createInventory(null, 54, plugin.color("Punish: &e" + target.getName()));
		
		int groupsInConfig = plugin.getConfig().getConfigurationSection("groups").getKeys(false).size();
		
		List<String> groups = new ArrayList();
		
		for(String groupTag : plugin.getConfig().getConfigurationSection("groups").getKeys(false)) {
			groups.add(groupTag);
		}
		
		for(int i = 0; i < groupsInConfig; i++) {
			
			ItemStack item;
			
			// Declare a config variable so we don't have to keep redundantly typing
			FileConfiguration conf = plugin.getConfig();
			
			String group = (String) groups.get(i);
			

		// Check if the item has a meta value
		if(conf.contains("groups." + group + ".item-data")) {
			item = new ItemStack(Material.valueOf(conf.getString("groups." + group + ".item")), 1, (short) conf.getInt("groups." + group + ".item-data"));
		} else {
			item = new ItemStack(Material.valueOf(conf.getString("groups." + group + ".item")));
		}
				
		ItemMeta itemMeta = item.getItemMeta();
				
		List<String> lore = new ArrayList();
		for(String loreLine : conf.getStringList("groups." + group + ".lore")) {
			lore.add(plugin.color(loreLine).replace("{PLAYER}", target.getName()));
		}
				
		itemMeta.setLore(lore);
		
		
		System.out.print(conf.getString("groups." + group + ".display-name"));
		itemMeta.setDisplayName(plugin.color(conf.getString("groups." + group + ".display-name")));
		
		item.setItemMeta(itemMeta);
		
		inv.setItem(conf.getInt("groups." + group + ".slot"), item);

			

			
		}
		
		sender.openInventory(inv);
		
	}
	
	public void waitX(int time, String command, Punish plugin) {
		new BukkitRunnable() {
			int start = 0;
			public void run() {
				start += 1;
				if (start >= time) {
					// dispatch
				}
			}
		}.runTaskTimer(plugin, 0, 20);
	}
	

	
}
