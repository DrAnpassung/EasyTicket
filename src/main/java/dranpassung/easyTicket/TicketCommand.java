package dranpassung.easyTicket;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TicketCommand implements CommandExecutor {
    private final TicketManager manager;
    private final ConfigManager config;

    public TicketCommand(TicketManager manager, ConfigManager config) {
        this.manager = manager;
        this.config = config;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) return true;


        try {
            if (args.length == 0) {
                boolean isStaff = player.hasPermission("easyticket.staff");
                TicketGUI.openDashboard(player, manager, isStaff, 0);
                return true;
            }

            String message = String.join(" ", args);
            if (!manager.canCreateTicket(player.getUniqueId())) {
                player.sendMessage(config.getPrefix().append(Component.text("You already hit the ticket limit of "+config.getTicketLimit()+"! Please wait.", NamedTextColor.RED)));
                return true;
            }

            Ticket ticket = new Ticket(player.getUniqueId(), player.getName(), message);
            manager.createTicket(ticket);
            player.sendMessage(config.getPrefix().append(Component.text("Ticket created successfully!", NamedTextColor.GREEN)));
        } catch (Exception e) {
            player.sendMessage(Component.text("Error opening ticket menu. Please contact an admin or dev.", NamedTextColor.RED));
            e.printStackTrace();
        }
        return true;
    }
}