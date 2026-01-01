package com.mcintire.evan;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CommandOpenInv implements CommandExecutor, Listener {
	private OpenInv	plugin;
	
	public CommandOpenInv(OpenInv plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("openinv")) {
			// We only want one argument;If there were more a fight would happen
			if (args.length != 1) return false;
			
			// Can't let those pesky robots use this command
			if (!(sender instanceof Player)) return true;
			
			Player player = (Player) sender;
			Player target = plugin.getServer().getPlayer(args[0]);
			
			// If the target has override, do nothing
			if (target != null) {
				if (target.hasPermission("cubeplugin.openinv.override")) {
					player.sendMessage(ChatColor.RED + "That inventory is protected");
					return true;
				}
				
				// Get the target's inventory as well as create a new one with
				// 45 slots
				Inventory targetInventory = target.getInventory();
				Inventory inv = Bukkit.createInventory(target, 45, "OpenInv:" + target.getName());
				
				// loop through the target inventory and copy the items over to
				// the new one
				for (int i = 0; i < 36; i++) {
					if (targetInventory.getItem(i) != null) {
						inv.setItem(i, targetInventory.getItem(i));
					}
				}
				
				// Loop through the armor and add it to the new inventory
				int pos = 36;
				for (ItemStack item : target.getInventory().getArmorContents()) {
					if (item != null) inv.setItem(pos, item);
					pos++;
				}
				
				// Open the inventory; everything went smooth
				player.openInventory(inv);
				return true;
			} else {
				// The specified player does not exist
				player.sendMessage(ChatColor.DARK_RED + "Player Not Found");
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * This function runs when the inventory is closed. It checks if it has the
	 * correct name, and if it does, it goes through and syncs the inventory
	 * with the player's
	 */
	@EventHandler
	public void onOpenInvClose(InventoryCloseEvent event) {
		if (event.getInventory().getName().contains("OpenInv")) {
			String invTitle = event.getInventory().getName();
			String[] results = invTitle.split(":");
			
			Inventory toBeSynced = event.getInventory();
			
			Player target = plugin.getServer().getPlayer(results[1]);
			
			if (target == null || !target.isOnline()) return;
			
			// Syncs the armor.
			// Magic Numbers galore.
			target.getInventory().setBoots(toBeSynced.getItem(36));
			target.getInventory().setLeggings(toBeSynced.getItem(37));
			target.getInventory().setChestplate(toBeSynced.getItem(38));
			target.getInventory().setHelmet(toBeSynced.getItem(39));
			
			// Loops through and syncs all the slots
			for (int i = 0; i < 36; i++) {
				if (toBeSynced.getItem(i) != null) {
					target.getInventory().setItem(i, toBeSynced.getItem(i));
				}
			}
		}
	}
}