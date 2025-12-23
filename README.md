# RohrLib

RohrLib is a lightweight utility library for Bukkit / Paper plugins. It bundles commonly needed systems into one reusable library so you don’t have to rewrite the same boilerplate for every plugin.

The library is designed to be:

- **Safe by default** (never overwrites user data)
- **Library-first** (usable across many plugins)
- **Paper-friendly** (uses modern APIs like Adventure & Brigadier)

---

## Quick Start

Get up and running with RohrLib in under 5 minutes.

### 1) Add RohrLib to your project

**Gradle**

```gradle
dependencies {
    implementation("dev.rohrjaspi:rohrlib:VERSION")
}
```

**Maven**

```xml
<dependency>
  <groupId>dev.rohrjaspi</groupId>
  <artifactId>rohrlib</artifactId>
  <version>VERSION</version>
</dependency>
```

---

## Commands

RohrLib provides a small command framework built on **Paper’s Brigadier API**. It removes the need for `plugin.yml` command definitions and uses lifecycle-based registration.

### `AbstractCommand`

Extend `AbstractCommand` and return a Brigadier command node.

Features:

- Supports command name, description, and aliases
- Uses `CommandSourceStack` (Paper)
- Clean separation of command definition and registration

Example:

```java
public final class PingCommand extends AbstractCommand {

    public PingCommand() {
        super("ping", "Shows pong", "p");
    }

    @Override
    protected @NotNull LiteralCommandNode<CommandSourceStack> node() {
        // build and return your brigadier node
        return /* ... */;
    }
}
```

### `CommandManager`

Registers commands using Paper lifecycle events:

```java
CommandManager manager = new CommandManager(this);
manager.register(new PingCommand());
```

> Note: Always import `CommandSourceStack` from `io.papermc.paper.command.brigadier`.

---

## GUI / Menu

RohrLib includes a simple inventory GUI system to speed up menu creation.

### `Menu`

Base class for inventory menus.

Features:

- Safe inventory opening on the main thread
- Built-in filler glass item
- Built-in close item
- Utility methods for common patterns

Common methods:

- `open()` – opens the menu
- `setFillerGlass()` – fills empty slots
- `setCloseItem(slot)` – places a close button

Example:

```java
public final class ExampleMenu extends Menu {

    public ExampleMenu(Player player, Plugin plugin) {
        super(player, plugin);
    }

    @Override
    public Component getMenuName() {
        return MiniMessage.miniMessage().deserialize("<gold>Example Menu</gold>");
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void setMenuItems() {
        setFillerGlass();
        setCloseItem(49);
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        // handle clicks
    }
}
```

Open the menu:

```java
new ExampleMenu(player, this).open();
```

### `PaginatedMenu`

Extends `Menu` and adds pagination support.

Features:

- Page tracking (`page`, `index`)
- Configurable `maxItemsPerPage` (default 28)
- Built-in navigation buttons

Typical use cases:

- Player lists
- Item lists
- Shops / auctions / bounties

---

## ItemCreator

A fluent builder for `ItemStack` using Adventure `Component`s.

Features:

- Display name & lore via `Component`
- Custom model data
- Optional glow effect
- Enchantment handling
- Player head owner support

Example:

```java
ItemStack item = ItemCreator.material(Material.DIAMOND)
    .displayName(MiniMessage.miniMessage().deserialize("<aqua><bold>Cool Item</bold>"))
    .withGlow(true)
    .modelData(1001)
    .build();
```

---

## File / Config Loader

RohrLib includes a **versioned config loader** that safely updates YAML files.

### Key Features

- Uses `config-version` in the file
- Automatically adds new keys from defaults
- Never overwrites existing user values
- Optional migration system for breaking changes
- Works from a shared library

### Basic Usage

Default config inside your plugin jar:

```yml
config-version: 1

feature:
  enabled: true
```

Load and update:

```java
ConfigLoader loader = new ConfigLoader(
    getDataFolder().toPath(),
    "config.yml",
    this::getResource,
    getLogger()
);

FileConfiguration config = loader.loadAndUpdate();
```

### What happens on startup

1. File is created if missing
2. User config is loaded
3. Default config is loaded from jar
4. Missing keys are added
5. Version is updated
6. User values remain untouched

### Migrations (Optional)

Migrations are only needed when you:

- Rename config keys
- Move paths
- Change structure or type

Example:

```java
MigrationRegistry registry = new MigrationRegistry()
    .step(1, (user, defaults) -> {
        MigrationHelpers.renameKey(user, "old.path", "new.path");
    });
```

---

## License

MIT License

