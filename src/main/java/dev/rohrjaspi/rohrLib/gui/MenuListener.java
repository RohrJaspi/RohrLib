package dev.rohrjaspi.rohrLib.gui;

import dev.rohrjaspi.rohrLib.RohrLib;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

	@EventHandler
	public void onMenuClick(InventoryClickEvent e) {

		InventoryHolder holder = e.getInventory().getHolder();

		if (holder instanceof Menu) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null) {
				return;
			}

			Menu menu = (Menu) holder;

			menu.handleMenu(e);
		}

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		RohrLib.getPlayerMenuUtilityMap().remove(event.getPlayer().getUniqueId());
	}


}