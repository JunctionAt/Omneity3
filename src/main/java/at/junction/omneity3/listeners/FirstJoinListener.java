package at.junction.omneity3.listeners;

import at.junction.omneity3.Omneity3;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class FirstJoinListener implements Listener {
    Omneity3 plugin;

    public FirstJoinListener(Omneity3 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.config.firstJoin.ENABLED) {
            if (!event.getPlayer().hasPlayedBefore()) {
                event.getPlayer().getInventory().addItem((ItemStack[]) plugin.config.firstJoin.ITEMS.toArray());
                if (plugin.config.firstJoin.FIRST_JOIN_ENABLED) {
                    plugin.getServer().broadcastMessage(String.format(plugin.config.firstJoin.FIRST_JOIN_MESSAGE, ChatColor.DARK_PURPLE, event.getPlayer().getName()));
                }
            }
        }
    }
}
