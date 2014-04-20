package at.junction.omneity3.listeners;

import at.junction.omneity3.Omneity3;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class PortalListener implements Listener {
    Omneity3 plugin;
    public PortalListener(Omneity3 plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPortalCreateEvent(PortalCreateEvent event) {
        if (event.getReason() == PortalCreateEvent.CreateReason.FIRE) {

            //Check to see if a moderator lit the portal
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                if (p.hasPermission("omneity.portal")) {
                    double distance = p.getLocation().distanceSquared(event.getBlocks().get(0).getLocation());
                    if (distance < 10) {
                        p.sendMessage(String.format("%s%s %s", ChatColor.GREEN, "Portal lit at ", event.getBlocks().get(0).getLocation().toString()));
                        return;
                    }
                }
            }

            if (!plugin.config.portals.PORTALS_ENABLED || plugin.config.portals.MODREQ_PORTALS) {
                int lowest = 500;

                for (Block b : event.getBlocks()) {
                    if (b.getLocation().getBlockY() < lowest) {
                        lowest = b.getLocation().getBlockY();
                    }
                }
                for (Block b : event.getBlocks()) {
                    if (b.getLocation().getBlockY() == lowest) {
                        Block above = b.getRelative(BlockFace.UP);
                        above.setType(Material.SIGN_POST);
                        Sign sign = (Sign) above.getState();
                        above.getRelative(BlockFace.UP).setType(Material.AIR);
                        if (!plugin.config.portals.PORTALS_ENABLED) {
                            sign.setLine(0, "Portals are");
                            sign.setLine(1, "disabled on");
                            sign.setLine(2, "this server.");

                        } else if (plugin.config.portals.MODREQ_PORTALS) {
                            sign.setLine(0, "Please make a");
                            sign.setLine(1, "modreq to light");
                            sign.setLine(2, "this portal.");
                            sign.setLine(3, "Thanks!");

                        }

                        sign.update();
                        event.setCancelled(true);
                    }
                }
            }
        } else if (event.getReason() == PortalCreateEvent.CreateReason.OBC_DESTINATION) {
            if (plugin.config.portals.DISABLE_DESTINATION_BUILD) {
                event.setCancelled(true);
            }
        }
    }

}
