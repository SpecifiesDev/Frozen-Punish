package me.specifies.core.Beta;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.specifies.core.Punish;
import me.specifies.core.Utils.LiteBansQuery;

public class TestLiteBans implements CommandExecutor {
	
	// This class is an abritray command to test around with liteban's database api. The command should be removed when the plugin is released.
	
	Punish plugin;
	public TestLiteBans(Punish instance) {
		this.plugin = instance;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		
		// We don't need to test anything, as we intend to remove it.
		
		Player target = Bukkit.getPlayer(args[0]);
		String targetMethod = args[1];
		
		int amount = 0;
		
		LiteBansQuery query = new LiteBansQuery();
		
		switch(targetMethod) {
		  case "bans":
			query.setTotalBans(target.getUniqueId().toString(), plugin);
			task(query, args[0], args[1], sender, "bans");
		  case "spamming":
			query.setSpammingInfractions(target.getUniqueId().toString(), plugin);
			task(query, args[0], args[1], sender, "spam");
		}
		
		
		return true;
	}
	
	
	private void task(LiteBansQuery q, String args0, String args1, CommandSender sender, String switchS) {
		new BukkitRunnable() {
			int start = 0;
			public void run() {
				start += 1;
				if(start >= 2) {
					int amount = 0;
					switch(switchS) {
					case "bans":
						amount = q.getBans();
					case "spam":
						amount = q.getSpamInfractions();
					}
					sender.sendMessage(plugin.color("&7The player&8: " + args0 + " &7has a total of &6" + amount + " &7" + args1 + "."));
					this.cancel();
					
				}
			}
		}.runTaskTimer(plugin, 0, 20);
	}

}
