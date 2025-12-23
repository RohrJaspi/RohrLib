package dev.rohrjaspi.rohrLib.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand {

    protected final @NotNull String name;
    protected final @Nullable String description;
    protected final @Nullable List<String> aliases;

    public AbstractCommand(
          final @NotNull String name,
          final @Nullable String description,
          final @Nullable List<String> aliases
    ) {
        this.name = name;
        this.description = description;
        this.aliases = aliases == null ? null : new ArrayList<>(aliases);
    }

    public AbstractCommand(
          final @NotNull String name,
          final @Nullable String description,
          final @NotNull String... aliases
    ) {
        this(name, description, List.of(aliases));
    }

    // When importing CommandSourceStack, be sure to use the io.papermc import
    protected abstract @NotNull LiteralCommandNode<CommandSourceStack> node();

}