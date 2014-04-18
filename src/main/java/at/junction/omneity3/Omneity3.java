package at.junction.omneity3;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
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
        loadRecipes();
    }


    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (command.getName().equals("spawn")) {
            if (sender instanceof Player) {
                ((Player) sender).teleport(config.spawn.LOCATION);
                sender.sendMessage(ChatColor.RED + "Wooosh!");
            } else {
                sender.sendMessage(ChatColor.RED + "Only usable by players, sorry!");
            }
        } else if (command.getName().equalsIgnoreCase("o3-reload")) {
            config.reload();
        } else if (command.getName().equalsIgnoreCase("world")) {
            if (args.length == 0) return false;
            if (!(args.length == 4 || args.length == 1)) return false;
            if (!(sender instanceof Player)){
                sender.sendMessage("Only usable by players");
                return false;
            }
            World world = getServer().getWorld(args[0]);
            if (world == null){
                sender.sendMessage("This world doesn't exist. Try 'world', 'world_nether', or 'world_the_end'");
                return true;
            }

            if (args.length == 4) {
                ((Player)sender).teleport(new Location(world, Double.parseDouble(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3])));
            } else {
                ((Player)sender).teleport(world.getSpawnLocation());
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
