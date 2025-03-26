package org.reuac.remaliciouspvp;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MainListener implements Listener {
    reMaliciousPvp plugin;

    MainListener(reMaliciousPvp reMaliciousPvp){
        plugin = reMaliciousPvp;
    }

    private static class KillRecord {
        public String victimId;
        public Instant timestamp;

        public KillRecord(String victimId, Instant timestamp) {
            this.victimId = victimId;
            this.timestamp = timestamp;
        }
    }

    public static List<String> ignoredWorlds;
    public static int consecutiveKills;
    public static int maximumInterval;
    public static List<String> victimTips;
    public static List<String> targetTips;
    public static List<String> victimConfirmTips;
    public static List<String> victimCancelTips;
    public static List<String> victimReply;
    public static List<String> commands;

    private final Map<String, LinkedList<KillRecord>> killRecords = new HashMap<>();
    private final Map<String, String> promptedVictims = new HashMap<>();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer == null || killer == victim) {
            return;
        }

        World world = victim.getWorld();
        if (ignoredWorlds.contains(world.getName())) {
            return;
        }

        String killerId = killer.getName();
        String victimId = victim.getName();

        killRecords.computeIfAbsent(killerId, k -> new LinkedList<>()).add(new KillRecord(victimId, Instant.now()));
        LinkedList<KillRecord> records = killRecords.get(killerId);

        records.removeIf(record -> record.timestamp.isBefore(Instant.now().minusSeconds(maximumInterval)));

        while (records.size() > consecutiveKills) {
            records.removeFirst();
        }

        if (records.size() == consecutiveKills) {
            KillRecord firstRecord = records.getFirst();
            if (!firstRecord.timestamp.isBefore(Instant.now().minusSeconds(maximumInterval))) {
                Player latestVictim = Bukkit.getPlayer(records.getLast().victimId);
                if (latestVictim != null && latestVictim.isOnline()) {
                    promptedVictims.put(latestVictim.getName(), killerId);
                    for (String tip : victimTips) {
                        latestVictim.sendMessage(replacePlayerName(tip,killerId,victimId));
                    }
                    for (String tip : targetTips) {
                        killer.sendMessage(replacePlayerName(tip,killerId,victimId));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (promptedVictims.containsKey(player.getName())) {
            if (victimReply.contains(message)) {
                String killerId = promptedVictims.remove(player.getName());
                Bukkit.getScheduler().runTask(plugin, () -> {
                    for (String tip : victimConfirmTips){
                        player.sendMessage(replacePlayerName(tip,killerId,player.getName()));
                    }
                    executeCommands(killerId, player.getName());
                    killRecords.remove(killerId);
                });
                event.setCancelled(true);
            } else {
                String killerId = promptedVictims.remove(player.getName());
                for (String tip : victimCancelTips){
                    player.sendMessage(replacePlayerName(tip,killerId,player.getName()));
                }
            }
        }
    }

    private void executeCommands(String killerId, String victimId) {
        for (String command : commands) {
            String formattedCommand = replacePlayerName(command,killerId,victimId);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCommand);
        }
    }

    private String replacePlayerName(String message, String targetPlayerName, String victimName) {
        return message.replace("%target_player_name%", targetPlayerName)
                      .replace("%victim_player_name%", victimName);
    }

}
