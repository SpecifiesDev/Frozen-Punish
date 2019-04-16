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
	
	
	/**
	 * openGUI is a utility method designed to open the punishment GUI.
	 * @param sender Executor of the command, the person who will execute punishment(s).
	 * @param target The target of the command, the person who will receive punishment(s).
	 * @param plugin Instance of our main class.
	 */
	public void openGUI(Player sender, Player target, Punish plugin) {
		
		// Create a new inventory with 54 slots, and the title of "Punish: playerName"
		// Setting their name is essential, as in Inventory click we parse the title to get a player to punish.
		Inventory inv = Bukkit.createInventory(null, 54, plugin.color("Punish: &e" + target.getName()));
		
		// The amount of groups in the "groups" section. So we can loop throuugh and do necessary computation.
		int groupsInConfig = plugin.getConfig().getConfigurationSection("groups").getKeys(false).size();
		
		// This list & for loop grabs the raw string of each group in the section, so we can refer to them in the loop
		List<String> groups = new ArrayList();
		
		for(String groupTag : plugin.getConfig().getConfigurationSection("groups").getKeys(false)) {
			groups.add(groupTag);
		}
		
		for(int i = 0; i < groupsInConfig; i++) {
			
			// Declare itemstack
			ItemStack item;
			
			// Declare a config variable so we don't have to keep redundantly typing
			FileConfiguration conf = plugin.getConfig();
			
			// Declare a static instance of what group we're performing computation in during this iteration
			String group = (String) groups.get(i);
			

			// Check if the item has a meta value
			if(conf.contains("groups." + group + ".item-data")) {
				item = new ItemStack(Material.valueOf(conf.getString("groups." + group + ".item")), 1, (short) conf.getInt("groups." + group + ".item-data"));
			} else {
				item = new ItemStack(Material.valueOf(conf.getString("groups." + group + ".item")));
			}
			
			// Grab the itemMeta for manipulation.
			ItemMeta itemMeta = item.getItemMeta();
			
			// Add necessary lore strings, and replace {PLAYER} with the target player.
			List<String> lore = new ArrayList();
			for(String loreLine : conf.getStringList("groups." + group + ".lore")) {
				lore.add(plugin.color(loreLine).replace("{PLAYER}", target.getName()));
			}
			
			// Set meta to the item
			itemMeta.setLore(lore);
			itemMeta.setDisplayName(plugin.color(conf.getString("groups." + group + ".display-name")));
			
			item.setItemMeta(itemMeta);
			
			// Is it decorative or not?
			// If it's decorative, we refer to "slots", for an integerList. If not, we refer to slot as we can naturally assume there will only be one copy of said item.
			if(conf.getBoolean("groups." + group + ".decorative")) {
				for(int slotToSet : conf.getIntegerList("groups." + group + ".slots")) {
					inv.setItem(slotToSet, item);
				}
			}
			
			if(!conf.getBoolean("groups." + group + ".decorative")) {
				inv.setItem(conf.getInt("groups." + group + ".slot"), item);
			}
			

			
		}
		
		sender.openInventory(inv);
		
	}
	
	/**
	 * waitX was designed in case we needed a cool down. Turned out it wasn't necessary, but will remain here for documentation purposes.
	 * @param time Seconds to count to
	 * @param command Command to send
	 * @param plugin Instance of main class
	 */
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
