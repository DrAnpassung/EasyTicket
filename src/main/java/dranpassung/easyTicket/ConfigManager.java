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
        String raw = plugin.getConfig().getString("gsettings.prefix", "&8[&bTicket&8]");
        return LegacyComponentSerializer.legacyAmpersand().deserialize(raw + " ");
    }

    public int getTicketLimit() {
        return plugin.getConfig().getInt("gsettings.ticket-limit", 5);
    }

    public Sound getNotifySound() {
        return parseSound(plugin.getConfig().getString("sounds.notify",   "block.note_block.chime"), "block.note_block.chime");
    }

    public Sound getUiClickSound() {
        return parseSound(plugin.getConfig().getString("sounds.ui-click", "ui.button.click"),        "ui.button.click");
    }

    public Sound getErrorSound() {
        return parseSound(plugin.getConfig().getString("sounds.error",    "entity.villager.no"),     "entity.villager.no");
    }

    public Sound getSuccessSound() {
        return parseSound(plugin.getConfig().getString("sounds.success",  "entity.player.levelup"),  "entity.player.levelup");
    }

    private Sound parseSound(String raw, String fallbackKey) {
        if (raw != null && !raw.isBlank()) {
            Sound sound = Registry.SOUNDS.get(NamespacedKey.minecraft(raw.toLowerCase()));
            if (sound != null) return sound;

            sound = Registry.SOUNDS.get(NamespacedKey.minecraft(raw.toLowerCase().replace('_', '.')));
            if (sound != null) return sound;

            plugin.getLogger().warning("[EasyTicket] Unknown sound '" + raw + "', falling back to " + fallbackKey);
        }
        return Registry.SOUNDS.get(NamespacedKey.minecraft(fallbackKey));
    }
}
