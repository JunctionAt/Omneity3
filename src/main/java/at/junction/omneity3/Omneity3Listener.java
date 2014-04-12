package at.junction.omneity3;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class Omneity3Listener implements Listener {
    Omneity3 plugin;
    public Omneity3Listener(Omneity3 plugin){
        this.plugin = plugin;
    }
    @EventHandler
    void onPlayerLoginEvent(PlayerLoginEvent event){
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()){
            player.teleport(plugin.config.spawn.LOCATION);
            if (plugin.config.spawn.FIRST_JOIN_ENABLED){
                plugin.getServer().broadcastMessage(ChatColor.DARK_PURPLE + plugin.config.spawn.FIRST_JOIN_MESSAGE);
            }
        }
    }

    @EventHandler
    void onPlayerPortalEvent(PlayerPortalEvent event){
        if (event.getFrom().getWorld().getEnvironment() == World.Environment.THE_END && !plugin.config.spawn.BED_SPAWN_ENABLED){
            event.setTo(plugin.config.spawn.LOCATION);
        }
    }

    @EventHandler
    void onPlayerRespawnEvent(PlayerRespawnEvent event){
        if (event.isBedSpawn() && !plugin.config.spawn.BED_SPAWN_ENABLED){
            event.setRespawnLocation(plugin.config.spawn.LOCATION);
        }
    }

}
