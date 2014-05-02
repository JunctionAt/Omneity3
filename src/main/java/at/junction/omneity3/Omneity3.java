package at.junction.omneity3;

import at.junction.omneity3.listeners.*;
import at.junction.omneity3.recipes.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

public class Omneity3 extends JavaPlugin  {
    public Configuration config;
    ArrayList<EntityType> blockedEntities;
    public DataOutputStream bungeePluginChannel;
    @Override
    public void onEnable() {
        File cfile = new File(getDataFolder(), "config.yml");
        if (!cfile.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        getLogger().info("Registering Deserialization Classes");
        ConfigurationSerialization.registerClass(WarpZone.class);
        ConfigurationSerialization.registerClass(Furnace.class);
        ConfigurationSerialization.registerClass(Shaped.class);
        ConfigurationSerialization.registerClass(Shapeless.class);
        getLogger().info("Done Registering Deserialization Classes");

        config = new Configuration(this);
        config.load();

        getServer().getPluginManager().registerEvents(new Omneity3Listener(this), this);
        getServer().getPluginManager().registerEvents(new PortalListener(this), this);
        getServer().getPluginManager().registerEvents(new FirstJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new WarpZoneListener(this), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new petKillListener(this), this);
        loadRecipes();

        blockedEntities = new ArrayList<>();
        blockedEntities.add(EntityType.ITEM_FRAME);
        blockedEntities.add(EntityType.FALLING_BLOCK);
        blockedEntities.add(EntityType.PLAYER);
        blockedEntities.add(EntityType.MINECART);
        blockedEntities.add(EntityType.BOAT);
        blockedEntities.add(EntityType.COMPLEX_PART);
        blockedEntities.add(EntityType.UNKNOWN);
        blockedEntities.add(EntityType.DROPPED_ITEM);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        bungeePluginChannel = new DataOutputStream(new ByteArrayOutputStream());

    }


    @Override
    public void onDisable() {
        try {
            bungeePluginChannel.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        switch (command.getName().toLowerCase()) {
            case "spawn":
                if (sender instanceof Player) {
                    ((Player) sender).teleport(config.spawn.LOCATION);
                    sender.sendMessage(ChatColor.RED + "Wooosh!");
                } else {
                    sender.sendMessage(ChatColor.RED + "Only usable by players, sorry!");
                }

                break;
            case "o3-reload":
                config.reload();
                sender.sendMessage(String.format("%sDone.", ChatColor.RED));
                break;
            case "world":
                if (args.length == 0) return false;
                if (!(args.length == 4 || args.length == 1)) return false;
                if (!(sender instanceof Player)) {
                    sender.sendMessage("Only usable by players");
                    return false;
                }
                World world = getServer().getWorld(args[0]);
                if (world == null) {
                    sender.sendMessage("This world doesn't exist. Try 'world', 'world_nether', or 'world_the_end'");
                    return true;
                }

                if (args.length == 4) {
                    ((Player) sender).teleport(new Location(world, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])));
                } else {
                    ((Player) sender).teleport(world.getSpawnLocation());
                }
                break;
            case "coords":
                if (sender instanceof Player) {
                    Location loc = ((Player) sender).getLocation();
                    sender.sendMessage(String.format("%sx: %d %sy: %d %sz: %d", ChatColor.RED, loc.getBlockX(), ChatColor.BLUE, loc.getBlockY(), ChatColor.AQUA, loc.getBlockZ()));
                } else {
                    sender.sendMessage(String.format("%sThis command is only usable by players", ChatColor.RED));
                }
                break;
            case "item":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (args.length < 1 || args.length > 2)
                        return false;
                    String blockID = "";
                    String blockData = "";
                    String amount = "64";
                    if (args[0].contains(":")) {
                        String[] temp = args[0].split(":");
                        blockID = temp[0];
                        blockData = temp[1];
                    } else {
                        blockID = args[0];
                    }

                    if (args.length == 2) {
                        amount = args[1];
                    }

                    getServer().dispatchCommand(player, String.format("give %s %s %s %s", player.getName(), blockID, amount, blockData));

                } else {
                    sender.sendMessage(String.format("%sThis command is only usable by players", ChatColor.RED));
                }
                break;
            case "staffchest":
                getServer().dispatchCommand(sender, "cmodify g:base_assistance");
                break;
            case "unstaff":
                getServer().dispatchCommand(sender, "cmodify -g:base_assistance");
                break;
            case "trace":
                if (args.length < 1 || args.length > 2) {
                    return false;
                } else if (args.length == 1) {
                    getServer().dispatchCommand(sender, String.format("lb lookup player %s sum blocks block 1 block 56 block 14 block 129", args[0]));
                } else if (args.length == 2) {
                    getServer().dispatchCommand(sender, String.format("lb lookup player %s sum blocks block 1 block 56 block 14 block 129 since %s days", args[0], args[1]));
                }
                break;
            case "tppos":
                if (args.length == 4) {
                    getServer().dispatchCommand(sender, String.format("world %s %s %s %s", args[0], args[1], args[2], args[3]));
                } else if (args.length == 3) {
                    getServer().dispatchCommand(sender, String.format("tp %s %s %s", args[0], args[1], args[2]));
                } else {
                    return false;
                }
                break;
            case "entity-magnet":
                if (args.length != 2) {
                    return false;
                } else {
                    if (sender instanceof Player) {

                        Player player = (Player) sender;
                        EntityType entityType = null;
                        try {
                            entityType = EntityType.valueOf(args[0].toUpperCase());
                        } catch (Exception e) {
                            player.sendMessage("Entity type does not exist");
                            return true;
                        }
                        double range = 0;
                        if (range > 50) {
                            sender.sendMessage("You don't have enough power! Entity magnet only works up to 50 blocks away.");
                            return true;
                        }

                        if (blockedEntities.contains(entityType)) {
                            sender.sendMessage("Magnet doesn't work on this block :(");
                            return true;
                        }

                        try {
                            range = Double.parseDouble(args[1]) / 2;
                        } catch (Exception e) {
                            player.sendMessage("Invalid range");
                            return true;
                        }

                        for (Entity e : player.getNearbyEntities(range, range, range)) {
                            if (e.getType().equals(entityType)) {
                                e.teleport(player);
                            }
                        }

                    }

                }
                break;
            case "swap-player":
                if (args.length != 2){
                    return false;
                }
                Player p = getServer().getPlayer(args[0]);
                bungeePluginChannel = new DataOutputStream(new ByteArrayOutputStream());
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b);
                try {
                    out.writeUTF("Connect");
                    out.writeUTF(config.bungeeSwap.SERVER);
                    p.sendPluginMessage(this, "BungeeCord", b.toByteArray());
                } catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }

    public void loadRecipes() {
        System.out.println(String.format("%s %s %s", config.recipes.FURNACES.size(), config.recipes.SHAPED.size(), config.recipes.SHAPELESS.size()));
        for (Furnace furnace : config.recipes.FURNACES.values()){
            FurnaceRecipe furnaceRecipe = new FurnaceRecipe(new ItemStack(furnace.result), furnace.source);
            getServer().addRecipe(furnaceRecipe);
        }
        for (Shaped shaped : config.recipes.SHAPED.values()){
            ShapedRecipe shapedRecipe = new ShapedRecipe(shaped.result);
            shapedRecipe.shape(shaped.shape);
            for (Character key : shaped.ingredientMap.keySet()){
                shapedRecipe.setIngredient(key, shaped.ingredientMap.get(key));
            }
            getServer().addRecipe(shapedRecipe);
        }

        for (Shapeless shapeless : config.recipes.SHAPELESS.values()){
            ShapelessRecipe shapelessRecipe = new ShapelessRecipe(shapeless.result);
            for (Material m : shapeless.ingredients){
                shapelessRecipe.addIngredient(m);
            }
            getServer().addRecipe(shapelessRecipe);
        }
    }
}

