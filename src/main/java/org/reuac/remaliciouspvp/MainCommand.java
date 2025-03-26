package org.reuac.remaliciouspvp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {
    reMaliciousPvp plugin;

    MainCommand(reMaliciousPvp reMaliciousPvp){
        plugin = reMaliciousPvp;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        plugin.reloadConfig();
        plugin.loadConfig();
        commandSender.sendMessage("Reload Completed");
        return false;
    }
}
