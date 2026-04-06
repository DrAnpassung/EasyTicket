package dranpassung.easyTicket;

import org.bukkit.plugin.java.JavaPlugin;

public class EasyTicket extends JavaPlugin {
    @Override
    public void onEnable() {
        ConfigManager cm = new ConfigManager(this);
        TicketManager tm = new TicketManager(getDataFolder(), cm);

        getCommand("ticket").setExecutor(new TicketCommand(tm, cm));
        getServer().getPluginManager().registerEvents(new TicketListener(tm, cm), this);

        getLogger().info("""
                
                 _____              _____ _      _        _  \s
                |  ___|            |_   _(_)    | |      | | \s
                | |__  __ _ ___ _   _| |  _  ___| | _____| |_\s
                |  __|/ _` / __| | | | | | |/ __| |/ / _ \\ __|
                | |__| (_| \\__ \\ |_| | | | | (__|   <  __/ |_\s
                \\____/\\__,_|___/\\__, \\_/ |_|\\___|_|\\_\\___|\\__|
                                 __/ |                       \s
                                |___/                        \s""");
    }
}