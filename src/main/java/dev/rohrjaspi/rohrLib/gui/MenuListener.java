package dev.rohrjaspi.rohrLib.gui;


import dev.rohrjaspi.main_library.Main;
import dev.rohrjaspi.main_library.gui.Menu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

	@EventHandler
	public void onMenuClick(InventoryClickEvent e) {

		InventoryHolder holder = e.getInventory().getHolder();

		if (holder instanceof dev.rohrjaspi.main_library.gui.Menu) {
			e.setCancelled(true);
			if (e.getCurrentItem() == null) {
				return;
			}

			dev.rohrjaspi.main_library.gui.Menu menu = (Menu) holder;

			menu.handleMenu(e);
		}

	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Main.getPlayerMenuUtilityMap().remove(event.getPlayer().getUniqueId());
	}


}