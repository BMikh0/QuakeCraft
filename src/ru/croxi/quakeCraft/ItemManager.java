package ru.croxi.quakeCraft;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import ru.croxi.quakeCraft.game.GameState;
import ru.croxi.quakeCraft.utils.StringModifier;

import java.util.HashMap;
import java.util.Map;

public class ItemManager {

    private static Map<String, ItemStack> items;

    ItemManager() {
        items = new HashMap<>();

        ConfigurationSection cfgSection = QuakeCraft.getInstance().getConfig().getConfigurationSection("items");

        items.put("gun", createItem(StringModifier.getModifiedString(cfgSection.getString("gun.displayName")), Material.valueOf(cfgSection.getString("gun.material"))));
        items.put("helmet", createItem(StringModifier.getModifiedString(cfgSection.getString("helmet.displayName")), Material.valueOf(cfgSection.getString("helmet.material"))));
        items.put("chestplate", createItem(StringModifier.getModifiedString(cfgSection.getString("chestplate.displayName")), Material.valueOf(cfgSection.getString("chestplate.material"))));
        items.put("leggings", createItem(StringModifier.getModifiedString(cfgSection.getString("leggings.displayName")), Material.valueOf(cfgSection.getString("leggings.material"))));
        items.put("boots", createItem(StringModifier.getModifiedString(cfgSection.getString("boots.displayName")), Material.valueOf(cfgSection.getString("boots.material"))));
    }

    public void giveItems() {
        GameState gameState = QuakeCraft.getInstance().getGame().getGameState();

        for (Player player: Bukkit.getOnlinePlayers()) {
            PlayerInventory inventory = player.getInventory();

            inventory.clear();
            inventory.setArmorContents(null);;

            switch(gameState) {
                case GAME:
                    inventory.setItem(0, getItem("gun"));
                    inventory.setHelmet(getItem("helmet"));
                    inventory.setChestplate(getItem("chestplate"));
                    inventory.setLeggings(getItem("leggings"));
                    inventory.setBoots(getItem("boots"));
            }
        }

    }

    public ItemStack getItem(String item) {
        return items.get(item);
    }

    private ItemStack createItem(String displayName, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(displayName);
        item.setItemMeta(meta);

        return item;
    }

}
