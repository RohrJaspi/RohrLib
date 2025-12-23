package dev.rohrjaspi.rohrLib.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCreator {

    private Material mat;
    private boolean glow;
    private Integer amount;
    private Component displayComponent;
    private Integer modelData;
    private List<Component> loreComponents;
    private Map<Enchantment, Integer> enchantments;
    private String skullOwnerName;


    public ItemCreator() {
        this.enchantments = new HashMap<>();
    }

    public ItemCreator(ItemStack item) {
        this.enchantments = new HashMap<>();
        this.mat = item.getType();
        this.amount = item.getAmount();
        if (item.hasItemMeta()) {
            if (item.getItemMeta().hasDisplayName()) {
                this.displayComponent = item.getItemMeta().displayName();
            }
            if (item.getItemMeta().hasLore()) {
                this.loreComponents = item.getItemMeta().lore();
            }
            if (item.getItemMeta().hasEnchants()) {
                this.enchantments = item.getItemMeta().getEnchants();
            }
            if (item.getItemMeta().hasCustomModelData()) {
                this.modelData = item.getItemMeta().getCustomModelData();
            }
        }
    }

    public ItemCreator fromMaterial(Material mat) {
        this.mat = mat;
        return this;
    }

    public ItemCreator amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemCreator displayName(Component comp) {
        this.displayComponent = comp;
        return this;
    }

    public ItemCreator modelData(int modelData) {
        this.modelData = modelData;
        return this;
    }

    public ItemCreator lore(List<Component> comps) {
        this.loreComponents = comps;
        return this;
    }

    public ItemCreator withGlow(boolean glows) {
        this.glow = glows;
        return this;
    }

    public ItemCreator addEnchant(Enchantment ench, int level) {
        this.enchantments.put(ench, level);
        return this;
    }

    public ItemCreator removeEnchant(Enchantment ench) {
        if (!this.enchantments.containsKey(ench)) {
            return this;
        }
        this.enchantments.remove(ench);
        return this;
    }

    public ItemCreator skullOwner(String name) {
        this.skullOwnerName = name;
        return this;
    }


    public Material getMaterial() {
        return this.mat;
    }

    public Integer getAmount() {
        return this.amount;
    }

    public Component getDisplayName() {
        return this.displayComponent;
    }

    public Integer getModeData() {
        return this.modelData;
    }

    public List<Component> getLore() {
        return this.loreComponents;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }


    public ItemStack build() {
        ItemStack item = null;
        if (this.mat == null) return null;

        if (this.amount == null) this.amount = 1;

        // create item FIRST
        item = new ItemStack(this.mat, this.amount);

        if (!this.enchantments.isEmpty()) {
            item.addUnsafeEnchantments(this.enchantments);
        }

        if (this.displayComponent != null || this.loreComponents != null || this.modelData != null || this.glow || this.skullOwnerName != null) {
            ItemMeta meta = item.getItemMeta();
            if (this.displayComponent != null) meta.displayName(this.displayComponent);
            if (this.loreComponents != null) meta.lore(this.loreComponents);
            if (this.glow) {
                meta.addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, true);
                meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
            }
            if (this.modelData != null) meta.setCustomModelData(this.modelData);
            if (this.mat == Material.PLAYER_HEAD && this.skullOwnerName != null && meta instanceof SkullMeta skullMeta) {
                skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(this.skullOwnerName));
                meta = skullMeta;
            }
            item.setItemMeta(meta);
        }
        return item;
    }


    public static ItemCreator material(final Material mat) {

        if (mat == null) {
            System.out.println("Material cannot be null!");
        }

        return new ItemCreator().fromMaterial(mat);
    }

}
