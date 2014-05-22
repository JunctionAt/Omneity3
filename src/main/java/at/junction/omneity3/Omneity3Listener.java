package at.junction.omneity3;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
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
                if ((monster.getEquipment().getItemInHand() != null) ||
                        (monster.getType() == EntityType.SKELETON && monster.getEquipment().getItemInHand().getType() != Material.BOW)) {
                    remove = false;
                }
                if (remove){
                    if (monster.getVehicle() instanceof Chicken){
                        monster.getVehicle().remove();
                    }
                    monster.remove();
                }
            }
        }
    }


    @EventHandler
    void onPlayerInteractEntityEvent(PlayerInteractEntityEvent event){
        if (event.getPlayer().hasMetadata("give-pet")){
            if (event.getRightClicked() instanceof Tameable){
                Tameable pet = (Tameable) event.getRightClicked();
                if (pet.isTamed() && pet.getOwner() instanceof Player){
                    Player player = (Player)pet.getOwner();
                    if (player.equals(event.getPlayer())){
                        OfflinePlayer reciever = plugin.getServer().getOfflinePlayer((String)event.getPlayer().getMetadata("give-pet").get(0).value());
                        pet.setOwner(reciever);
                    } else {
                        event.getPlayer().sendMessage("This pet is not yours to give");
                    }
                } else {
                    event.getPlayer().sendMessage("This pet is not tamed");
                }
            } else {
                event.getPlayer().sendMessage("That entity can not be a pet");
            }
        }
    }

//    @EventHandler
//    void onEntityDeathEvent(EntityDeathEvent event){
//        if (event.getEntity().getKiller() != null && event.getEntity().getKiller().getName().equalsIgnoreCase("adamminer")){
//            event.getDrops().clear();
//            event.getDrops().add(new ItemStack(Material.STRING));
//            event.getDrops().add(new ItemStack(Material.STICK));
//        }
//    }

}

