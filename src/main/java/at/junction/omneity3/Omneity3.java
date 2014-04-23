package at.junction.omneity3;

import at.junction.omneity3.listeners.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Omneity3 extends JavaPlugin {
    public Configuration config;

    @Override
    public void onEnable() {
        File cfile = new File(getDataFolder(), "config.yml");
        if (!cfile.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        config = new Configuration(this);
        config.load();
        getServer().getPluginManager().registerEvents(new Omneity3Listener(this), this);
        getServer().getPluginManager().registerEvents(new PortalListener(this), this);
        getServer().getPluginManager().registerEvents(new FirstJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new WarpZoneListener(this), this);
        getServer().getPluginManager().registerEvents(new SpawnListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityMagnetListener(this), this);
        loadRecipes();
    }


    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        switch (command.getName()) {
            case "spawn":
            case "SPAWN":
                if (sender instanceof Player) {
                    ((Player) sender).teleport(config.spawn.LOCATION);
                    sender.sendMessage(ChatColor.RED + "Wooosh!");
                } else {
                    sender.sendMessage(ChatColor.RED + "Only usable by players, sorry!");
                }

                break;
            case "o3-reload":
                config.reload();
                break;
            case "world":
            case "WORLD":
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
            case "COORDS":
                if (sender instanceof Player) {
                    Location loc = ((Player) sender).getLocation();
                    sender.sendMessage(String.format("%sx: %d %sy: %d %sz: %d", ChatColor.RED, loc.getBlockX(), ChatColor.BLUE, loc.getBlockY(), ChatColor.AQUA, loc.getBlockZ()));
                } else {
                    sender.sendMessage(String.format("%sThis command is only usable by players", ChatColor.RED));
                }
                break;
            case "item":
            case "ITEM":
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
            case "STAFFCHEST":
                getServer().dispatchCommand(sender, "cmodify g:base_assistance");
                break;
            case "unstaff":
            case "UNSTAFF":
                getServer().dispatchCommand(sender, "cmodify -g:base_assistance");
                break;

            case "trace":
            case "TRACE":
                if (args.length < 1 || args.length > 2) {
                    return false;
                } else if (args.length == 1) {
                    getServer().dispatchCommand(sender, String.format("lb lookup player %s sum blocks block 1 block 56 block 14 block 129", args[0]));
                } else if (args.length == 2) {
                    getServer().dispatchCommand(sender, String.format("lb lookup player %s sum blocks block 1 block 56 block 14 block 129 since %s days", args[0], args[1]));
                }
                break;
            case "tppos":
            case "TPPOS":
                if (args.length == 4){
                    getServer().dispatchCommand(sender, String.format("world %s %s %s %s", args[0], args[1], args[2], args[3]));
                } else if (args.length == 3){
                    getServer().dispatchCommand(sender, String.format("tp %s %s %s", args[0], args[1], args[2]));
                } else {
                    return false;
                }
                break;
            case "entity-magnet":

                if (args.length != 1){
                    return false;
                } else {
                    if (sender instanceof Player){

                        Player player = (Player) sender;
                        if (args[0].equals("clear")){
                            if (player.hasMetadata("entity-magnet")){
                                player.removeMetadata("entity-magnet", this);
                            }
                        }
                        EntityType entityType = EntityType.valueOf(args[0]);
                        player.setMetadata("entity-magnet", new FixedMetadataValue(this, entityType));
                    }

                }
        }
        return true;
    }

    public void loadRecipes() {
        String[] bardingLayout = {"  A", "ASA", "AAA"};

        //diamondBarding
        ShapedRecipe diamondBarding = new ShapedRecipe(new ItemStack(Material.DIAMOND_BARDING));
        diamondBarding.shape(bardingLayout);
        diamondBarding.setIngredient('A', Material.DIAMOND);
        diamondBarding.setIngredient('S', Material.SADDLE);
        getServer().addRecipe(diamondBarding);

        //goldBarding
        ShapedRecipe goldBarding = new ShapedRecipe(new ItemStack(Material.GOLD_BARDING));
        goldBarding.shape(bardingLayout);
        goldBarding.setIngredient('A', Material.GOLD_INGOT);
        goldBarding.setIngredient('S', Material.SADDLE);
        getServer().addRecipe(goldBarding);

        //ironBarding
        ShapedRecipe ironBarding = new ShapedRecipe(new ItemStack(Material.IRON_BARDING));
        ironBarding.shape(bardingLayout);
        ironBarding.setIngredient('A', Material.IRON_INGOT);
        ironBarding.setIngredient('S', Material.SADDLE);
        getServer().addRecipe(ironBarding);
    }
}

