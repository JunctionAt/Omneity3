package at.junction.omneity3.listeners;

import at.junction.omneity3.Omneity3;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class EntityMagnetListener implements Listener {
    Omneity3 plugin;

    public EntityMagnetListener(Omneity3 plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata("entity-magnet")) {
            EntityType type = (EntityType) player.getMetadata("entity-magnet").get(0).value();
            Location loc = event.getTo();
            for (Entity e : loc.getChunk().getEntities()) {
                if (e.getType() == player.getMetadata("entity-magnet").get(0).value()) {
                    if (e.getLocation().distanceSquared(loc) < 25) {
                        player.sendMessage("Found entity! Teleporting to you.");
                        e.setFallDistance(0.0f);
                        e.teleport(loc);
                    }
                }
            }
        }
    }
}