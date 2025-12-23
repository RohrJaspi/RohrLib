package dev.rohrjaspi.rohrlib;

import dev.rohrjaspi.rohrlib.gui.PlayerMenuUtility;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

public final class RohrLib extends JavaPlugin {

    @Getter
    private static RohrLib instance;

    @Getter
    private static final HashMap<UUID, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();


    @Override
    public void onLoad() {
        instance = this;

    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p.getUniqueId()))) {

            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p.getUniqueId(), playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p.getUniqueId());
        }
    }
}
