package org.reuac.remaliciouspvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public final class reMaliciousPvp extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadConfig();

        Bukkit.getPluginManager().registerEvents(new MainListener(this), this);
        Bukkit.getPluginCommand("remaliciouspvp").setExecutor(new MainCommand(this));
    }

    @Override
    public void onDisable() {
    }

    public void loadConfig() {
        FileConfiguration config = getConfig();
        MainListener.ignoredWorlds = config.getStringList("ignoredWorlds");
        MainListener.consecutiveKills = config.getInt("consecutiveKills");
        MainListener.maximumInterval = config.getInt("maximumInterval");
        MainListener.victimTips = config.getStringList("victimTips").stream()
                .map(message_temp -> ChatColor.translateAlternateColorCodes('&', message_temp))
                .collect(Collectors.toList());
        MainListener.targetTips = config.getStringList("targetTips").stream()
                .map(message_temp -> ChatColor.translateAlternateColorCodes('&', message_temp))
                .collect(Collectors.toList());
        MainListener.victimConfirmTips = config.getStringList("victimConfirmTips").stream()
                .map(message_temp -> ChatColor.translateAlternateColorCodes('&', message_temp))
                .collect(Collectors.toList());
        MainListener.victimCancelTips = config.getStringList("victimCancelTips").stream()
                .map(message_temp -> ChatColor.translateAlternateColorCodes('&', message_temp))
                .collect(Collectors.toList());
        MainListener.victimReply = config.getStringList("victimReply");
        MainListener.commands = config.getStringList("commands");
    }
}
