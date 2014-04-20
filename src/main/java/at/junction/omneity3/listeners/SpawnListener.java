package at.junction.omneity3.listeners;

import at.junction.omneity3.Omneity3;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SpawnListener implements Listener {
    Omneity3 plugin;

    public SpawnListener(Omneity3 plugin){
        this.plugin = plugin;
    }

    @EventHandler
    void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            player.teleport(plugin.config.spawn.LOCATION);
        }
    }

    @EventHandler
    void onPlayerPortalEvent(PlayerPortalEvent event) {
        if (event.getFrom().getWorld().getEnvironment() == World.Environment.THE_END && !plugin.config.spawn.BED_SPAWN_ENABLED) {
            event.setTo(plugin.config.spawn.LOCATION);
        }
    }

    @EventHandler
    void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        if (event.isBedSpawn() && !plugin.config.spawn.BED_SPAWN_ENABLED) {
            event.setRespawnLocation(plugin.config.spawn.LOCATION);
        }
    }


}
