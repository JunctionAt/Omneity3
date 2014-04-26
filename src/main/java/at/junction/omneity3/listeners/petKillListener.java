package at.junction.omneity3.listeners;

import at.junction.omneity3.Omneity3;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class petKillListener implements Listener {

    Omneity3 plugin;

    public petKillListener(Omneity3 plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onEntityDamageEntityEvent(EntityDamageByEntityEvent event) {
        if (plugin.config.petKill.DISABLED) {
            Entity entity = event.getEntity();

            if (!(entity instanceof Tameable)) {
                return;
            }

            if (event.getDamager() instanceof Player) {
                Player p = (Player) event.getDamager();
                Tameable pet = (Tameable) entity;

                if ((pet.getOwner() != null) && !(pet.getOwner().getUniqueId().equals(p.getUniqueId()))) {
                    p.sendMessage(String.format("%sYou cannot damage other player's pets.", ChatColor.RED));
                    event.setCancelled(true);
                }

            }

        }
    }
}
