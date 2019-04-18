package me.specifies.core.Utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.scheduler.BukkitRunnable;

import litebans.api.Database;
import me.specifies.core.Punish;

public class LiteBansQuery {
	
	private int returnBans = 0;
	private int spamInfractions = 0;
	
	public void setTotalBans(String uuid, Punish plugin) {
		
		String query = "SELECT * FROM {bans} WHERE uuid=?";
		
		
        	new BukkitRunnable() {
			@Override
			public void run() {
                		queryDatabase(query, uuid, "setTotalBans");
				this.cancel();
			}
			
		}.runTaskAsynchronously(plugin);
		
		
	}
	
	public void setSpammingInfractions(String uuid, Punish plugin) {
		
		String query = "SELECT * FROM {mutes} WHERE uuid = ?";
		
		new BukkitRunnable() {
			@Override
			public void run() {
				queryDatabase(query, uuid, "spamInfractions");
			}
		}.runTaskAsynchronously(plugin);
		
	}
	
	
	public int getBans() {
		return returnBans;
	}
	public int getSpamInfractions() {
		return spamInfractions;
	}
	
	private void queryDatabase(String query, String uuid, String caseS) {
		
		try (PreparedStatement st = Database.get().prepareStatement(query)){
			st.setString(1, uuid);
			st.setQueryTimeout(4);
			
			try (ResultSet set = st.executeQuery()) {
				while(set.next()) {
					switch(caseS) {
					  case "setTotalBans":
						  returnBans += 1;
					  case "spamInfractions":
						  String reason = set.getString("reason");
						  if(reason.contains("(Spamming)")) {
							  spamInfractions += 1;
						  }
						  
					}
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	

}
