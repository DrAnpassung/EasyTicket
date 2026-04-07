package dranpassung.easyTicket;

import org.bukkit.plugin.java.JavaPlugin;
import java.util.Objects;

public class EasyTicket extends JavaPlugin {
    @Override
    public void onEnable() {
        ConfigManager cm = new ConfigManager(this);
        TicketManager tm = new TicketManager(getDataFolder(), cm);

        Objects.requireNonNull(getCommand("ticket")).setExecutor(new TicketCommand(tm, cm));
        getServer().getPluginManager().registerEvents(new TicketListener(tm, cm), this);

        String C = "\u001B[36m";
        String Y = "\u001B[33m";

        System.out.println(
                C + "\n" +
                        C + "    _/_/_/_/        _/_/         _/_/_/   _/      _/\n" +
                        C + "   _/            _/    _/     _/           _/  _/\n" +
                        C + "  _/_/_/        _/_/_/_/       _/_/         _/\n" +
                        C + " _/            _/    _/           _/       _/\n" +
                        C + "_/_/_/_/      _/    _/     _/_/_/         _/\n" +
                        "\n" +
                        Y + "_/_/_/_/_/      _/_/_/       _/_/_/      _/    _/      _/_/_/_/  _/_/_/_/_/\n" +
                        Y + "   _/            _/       _/            _/  _/        _/            _/\n" +
                        Y + "  _/            _/       _/            _/_/          _/_/_/        _/\n" +
                        Y + " _/            _/       _/            _/  _/        _/            _/\n" +
                        Y + "_/          _/_/_/       _/_/_/      _/    _/      _/_/_/_/      _/\n"
        );
    }
}
