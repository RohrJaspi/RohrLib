package dev.rohrjaspi.rohrLib.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerMenuUtility {

	private UUID owner;

	public PlayerMenuUtility(Player p) {
		this.owner = p.getUniqueId();
	}

	public Player getOwner() {
		return Bukkit.getPlayer(owner);
	}
}