package dev.rohrjaspi.rohrlib.commands;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class CommandManager {

    private final @NotNull LifecycleEventManager<@NotNull Plugin> manager;

    public CommandManager(final @NotNull JavaPlugin plugin) {
        manager = plugin.getLifecycleManager();
    }

    public void register(final @NotNull AbstractCommand command) {
        manager.registerEventHandler(LifecycleEvents.COMMANDS, handler ->
            handler.registrar().register(command.node(), command.description, command.aliases)
        );
    }

}