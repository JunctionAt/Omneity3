package at.junction.omneity3;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Omneity3Listener implements Listener {
    Omneity3 plugin;

    public Omneity3Listener(Omneity3 plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore()) {
            player.teleport(plugin.config.spawn.LOCATION);
            if (plugin.config.spawn.FIRST_JOIN_ENABLED) {
                plugin.getServer().broadcastMessage(String.format(plugin.config.spawn.FIRST_JOIN_MESSAGE, ChatColor.DARK_PURPLE, player.getName()));
            }
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

    @EventHandler
    void onPlayerFishEvent(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY || event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if (new Random().nextInt(100) < 10) {
                ItemStack item;

                if (new Random().nextInt(2) == 1) {
                    item = new ItemStack(Material.NAME_TAG);
                } else {
                    item = new ItemStack(Material.SADDLE);
                }
                event.getPlayer().getLocation().getWorld().dropItem(event.getPlayer().getLocation(), item);
            }
        }
    }

    //When a chunk unloads, remove its monsters, excluding Withers
    //EnderDragon is NOT a monster, it is a ComplexLivingEntity (LEAVE ENDER ALONE)
    //Hoping this cuts down on lag?
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk chunk = event.getChunk();
        for (Entity entity : chunk.getEntities()) {
            if (!entity.isDead() && entity instanceof Monster) {
                Monster monster = (Monster) entity;

                Boolean remove = true;

                if (monster.getCustomName() != null) {
                    remove = false;
                }
                if (monster.getType() == EntityType.WITHER) {
                    remove = false;
                }
                for (ItemStack item : monster.getEquipment().getArmorContents()) {
                    if (item != null) {
                        remove = false;
                        break;
                    }
                }
                if (remove)
                    monster.remove();
            }
        }
    }

    @EventHandler
    public void onPortalCreateEvent(EntityCreatePortalEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!plugin.config.portals.PORTALS_ENABLED || plugin.config.portals.MODREQ_PORTALS) {
                if (player.hasPermission("omneity.portal")){
                    return;
                }

                int lowest = 500;

                for (BlockState b : event.getBlocks()) {
                    if (b.getLocation().getBlockY() < lowest) {
                        lowest = b.getLocation().getBlockY();
                    }
                }
                for (BlockState blockState : event.getBlocks()) {
                    if (blockState.getLocation().getBlockY() == lowest + 1) {
                        blockState.setType(Material.SIGN_POST);
                        Sign sign = (Sign) blockState;
                        blockState.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
                        if (!plugin.config.portals.PORTALS_ENABLED) {
                            sign.setLine(0, "Portals are");
                            sign.setLine(1, "disabled on");
                            sign.setLine(2, "this server.");

                        } else if (plugin.config.portals.MODREQ_PORTALS) {

                            sign.setLine(0, "Please wait");
                            sign.setLine(1, "for a moderator");
                            sign.setLine(2, "to light this");
                            sign.setLine(3, "portal. Thanks");

                            plugin.getServer().dispatchCommand(player, plugin.config.portals.MODREQ_COMMAND);
                        }

                        sign.update();
                        event.setCancelled(true);
                    }
                }
            }
        } else {
            //Entity isn't a player - don't allow portal
            event.setCancelled(true);
        }
    }
}

