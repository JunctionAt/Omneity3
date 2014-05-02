package at.junction.omneity3.listeners;

import at.junction.omneity3.Omneity3;
import at.junction.omneity3.WarpZone;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class WarpZoneListener implements Listener {
    Omneity3 plugin;
    public WarpZoneListener(Omneity3 plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {
        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            return;
        }

        Block to = event.getTo().getBlock();

        for (WarpZone wz : plugin.config.warpZones.WARP_ZONES.values()) {
            if (wz.getFrom().contains(to)) {
                event.getPlayer().teleport(wz.getTo());
            }
        }

    }

}
