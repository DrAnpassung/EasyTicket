package dranpassung.easyTicket;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class TicketGUI {
    public static final NamespacedKey TICKET_ID_KEY = new NamespacedKey(JavaPlugin.getPlugin(EasyTicket.class), "ticket_id");
    public static final NamespacedKey ACTION_KEY = new NamespacedKey(JavaPlugin.getPlugin(EasyTicket.class), "gui_action");

    public static void openDashboard(Player player, TicketManager manager, boolean isStaff, int page) {
        Component title = Component.text((isStaff ? "Staff Dashboard" : "My Tickets") + " - Page " + (page + 1));
        Inventory inv = Bukkit.createInventory(null, 54, title);

        List<Ticket> allTickets = isStaff ? new ArrayList<>(manager.getActiveTickets()) : manager.getTicketsByPlayer(player.getUniqueId());
        TicketSortMode sortMode = manager.getSortMode(player.getUniqueId());

        // Filter
        if (sortMode == TicketSortMode.UNANSWERED) {
            allTickets.removeIf(t -> !t.isUnanswered());
        }

        // Sort
        if (!allTickets.isEmpty()) {
            allTickets.sort((t1, t2) -> {
                switch (sortMode) {
                    case DATE_OLDEST: return Long.compare(t1.getTimestamp(), t2.getTimestamp());
                    case UPDATE_NEWEST: return Long.compare(t2.getLastUpdate(), t1.getLastUpdate());
                    case UPDATE_OLDEST: return Long.compare(t1.getLastUpdate(), t2.getLastUpdate());
                    case UNANSWERED:
                    case DATE_NEWEST:
                    default: return Long.compare(t2.getTimestamp(), t1.getTimestamp());
                }
        });}

        // Pagination
        int ticketsPerPage = 45;
        int startIndex = page * ticketsPerPage;
        int endIndex = Math.min(startIndex + ticketsPerPage, allTickets.size());

        for (int i = startIndex; i < endIndex; i++) {
            Ticket ticket = allTickets.get(i);
            ItemStack head = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setOwningPlayer(Bukkit.getOfflinePlayer(ticket.getCreatorUUID()));
            meta.displayName(Component.text(ticket.getCreatorName() + "'s Ticket", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, false));

            List<Component> lore = new ArrayList<>();
            lore.add(Component.text("Subject: " + ticket.getMessage(), NamedTextColor.WHITE));
            lore.add(Component.empty());
            if (ticket.isUnanswered()) lore.add(Component.text("Status: UNANSWERED", NamedTextColor.YELLOW, TextDecoration.BOLD));
            else lore.add(Component.text("Status: Answered", NamedTextColor.GREEN));
            lore.add(Component.empty());
            lore.add(Component.text("LEFT-CLICK: Open Ticket", NamedTextColor.AQUA));
            if (isStaff) {
                lore.add(Component.text("MIDDLE-CLICK: Teleport to user", NamedTextColor.GRAY));
                lore.add(Component.text("RIGHT-CLICK: Close Ticket", NamedTextColor.RED));
            }

            meta.getPersistentDataContainer().set(TICKET_ID_KEY, PersistentDataType.STRING, ticket.getId().toString());
            meta.lore(lore);
            head.setItemMeta(meta);
            inv.addItem(head);
        }

        // Control Bar
        ItemStack pane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 45; i < 54; i++) inv.setItem(i, pane);

        // Sort Button
        ItemStack sortBtn = new ItemStack(Material.HOPPER);
        ItemMeta sMeta = sortBtn.getItemMeta();
        sMeta.displayName(Component.text("Sort / Filter", NamedTextColor.LIGHT_PURPLE, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        List<Component> sLore = new ArrayList<>();
        sLore.add(Component.text("Currently: " + sortMode.getDisplayName(), NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false));
        sLore.add(Component.text("Click to cycle", NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, false));
        sMeta.lore(sLore);
        sMeta.getPersistentDataContainer().set(ACTION_KEY, PersistentDataType.STRING, "CYCLE_SORT");
        sortBtn.setItemMeta(sMeta);
        inv.setItem(47, sortBtn);

        // Create Button
        ItemStack create = new ItemStack(Material.LIME_CONCRETE);
        ItemMeta cMeta = create.getItemMeta();
        cMeta.displayName(Component.text("NEW TICKET", NamedTextColor.GREEN, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false));
        cMeta.getPersistentDataContainer().set(ACTION_KEY, PersistentDataType.STRING, "CREATE");
        create.setItemMeta(cMeta);
        inv.setItem(49, create);

        // Arrows & Notifs
        if (page > 0) {
            ItemStack prev = new ItemStack(Material.ARROW);
            ItemMeta pMeta = prev.getItemMeta();
            pMeta.displayName(Component.text("← Previous Page", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            pMeta.getPersistentDataContainer().set(ACTION_KEY, PersistentDataType.STRING, "PAGE_" + (page - 1));
            prev.setItemMeta(pMeta);
            inv.setItem(45, prev);
        }

        if (allTickets.size() > endIndex) {
            ItemStack next = new ItemStack(Material.ARROW);
            ItemMeta nMeta = next.getItemMeta();
            nMeta.displayName(Component.text("Next Page →", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
            nMeta.getPersistentDataContainer().set(ACTION_KEY, PersistentDataType.STRING, "PAGE_" + (page + 1));
            next.setItemMeta(nMeta);
            inv.setItem(53, next);
        }

        if (isStaff) {
            boolean enabled = manager.hasNotifications(player.getUniqueId());
            ItemStack toggle = new ItemStack(enabled ? Material.BELL : Material.BARRIER);
            ItemMeta tMeta = toggle.getItemMeta();
            tMeta.displayName(Component.text("Notification: ", NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false)
                    .append(enabled ?
                            Component.text("ENABLED", NamedTextColor.GREEN, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false) :
                            Component.text("DISABLED", NamedTextColor.RED, TextDecoration.BOLD).decoration(TextDecoration.ITALIC, false)));
            tMeta.getPersistentDataContainer().set(ACTION_KEY, PersistentDataType.STRING, "TOGGLE_NOTIFS");
            toggle.setItemMeta(tMeta);
            inv.setItem(46, toggle);
        }

        player.openInventory(inv);
    }
}