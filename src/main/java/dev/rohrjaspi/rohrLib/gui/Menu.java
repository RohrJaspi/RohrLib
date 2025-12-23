package dev.rohrjaspi.rohrLib.gui;

import dev.rohrjaspi.rohrLib.RohrLib;
import dev.rohrjaspi.rohrLib.utils.ItemCreator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public abstract class Menu implements InventoryHolder {


	protected PlayerMenuUtility playerMenuUtility;
	protected Inventory inventory;
	protected ItemStack FILLER_GLASS = ItemCreator.material(Material.GRAY_STAINED_GLASS_PANE).displayName(Component.text(" ")).build();
	protected ItemStack CLOSE = ItemCreator.material(Material.BARRIER).displayName(MiniMessage.miniMessage().deserialize("<red><bold>Close")).build();
	private final Plugin plugin;



	public Menu(Player player, Plugin plugin) {
		this.playerMenuUtility = RohrLib.getPlayerMenuUtility(player);
		this.plugin = plugin;
	}


	public abstract Component getMenuName();


	public abstract int getSlots();


	public abstract void handleMenu(InventoryClickEvent e);


	public abstract void setMenuItems();


	public void open() {
		Player p = playerMenuUtility.getOwner();
		if (p == null || !p.isOnline()) return;

		this.inventory = Bukkit.createInventory(this, getSlots(), getMenuName());
		setMenuItems();

		Bukkit.getScheduler().runTask(plugin, () -> p.openInventory(inventory));
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	public void setFillerGlass(){
		for (int i = 0; i < getSlots(); i++) {
			if (inventory.getItem(i) == null){
				inventory.setItem(i, FILLER_GLASS);
			}
		}
	}

	public void setCloseItem(int slot){
		inventory.setItem(slot, CLOSE);
	}
}