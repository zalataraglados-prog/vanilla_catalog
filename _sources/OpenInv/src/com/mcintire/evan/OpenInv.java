package com.mcintire.evan;

import org.bukkit.plugin.java.JavaPlugin;

public class OpenInv extends JavaPlugin {
	@Override
	public void onEnable() {
		getLogger().info("Enabling");
		// Register the command and listener
		getCommand("openinv").setExecutor(new CommandOpenInv(this));
		getServer().getPluginManager().registerEvents(new CommandOpenInv(this), this);
		
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Disabling");
	}
}
