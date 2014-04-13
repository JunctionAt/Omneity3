package at.junction.omneity3;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

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
                plugin.getServer().broadcastMessage(String.format(plugin.config.spawn.FIRST_JOIN_MESSAGE, ChatColor.DARK_PURPLE, player.getName()));
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

    @EventHandler
    void onPlayerFishEvent(PlayerFishEvent event){
        if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY || event.getState() == PlayerFishEvent.State.CAUGHT_FISH){
            if (new Random().nextInt(100) < 10){
                ItemStack item;

                if (new Random().nextInt(2) == 1){
                    item = new ItemStack(Material.NAME_TAG);
                } else {
                    item = new ItemStack(Material.SADDLE);
                }
                event.getPlayer().getLocation().getWorld().dropItem(event.getPlayer().getLocation(), item);
            }
        }
    }
}
