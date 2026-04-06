package dranpassung.easyTicket;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;

public class ConfigManager {
    private final EasyTicket plugin;

    public ConfigManager(EasyTicket plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
    }

    public Component getPrefix() {
        String prefix = plugin.getConfig().getString("settings.prefix", "&8[&bTicket&8]");
        return LegacyComponentSerializer.legacyAmpersand().deserialize(prefix + " ");
    }

    public int getTicketLimit() {
        return plugin.getConfig().getInt("settings.ticket-limit", 5);
    }

    public Sound getNotifySound() {
        return Registry.SOUNDS.get(NamespacedKey.minecraft(plugin.getConfig().getString("sounds.notify", "BLOCK_NOTE_BLOCK_CHIME")));
    }

    public Sound getUiClickSound() {
        return Registry.SOUNDS.get(NamespacedKey.minecraft(plugin.getConfig().getString("sounds.ui-click", "UI_BUTTON_CLICK")));
    }

    public Sound getErrorSound() {
        return Registry.SOUNDS.get(NamespacedKey.minecraft(plugin.getConfig().getString("sounds.error", "ENTITY_VILLAGER_NO")));
    }

    public Sound getSuccessSound() {
        return Registry.SOUNDS.get(NamespacedKey.minecraft(plugin.getConfig().getString("sounds.success", "ENTITY_PLAYER_LEVELUP")));
    }
}