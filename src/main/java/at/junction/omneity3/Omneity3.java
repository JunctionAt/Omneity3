package at.junction.omneity3;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.io.File;

public class Omneity3 extends JavaPlugin{
    public Configuration config;
    @Override
    public void onEnable(){
        File cfile = new File(getDataFolder(), "config.yml");
        if (!cfile.exists()) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        config = new Configuration(this);
        config.load();
        getServer().getPluginManager().registerEvents(new Omneity3Listener(this), this);
        //Load New Recipes

    }


    @Override
    public void onDisable(){

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (command.getName().equals("spawn")){
            if (sender instanceof Player){
                ((Player)sender).teleport(config.spawn.LOCATION);
                sender.sendMessage(ChatColor.RED + "Wooosh!");
            } else {
                sender.sendMessage(ChatColor.RED + "Only usable by players, sorry!");
            }
        }
        return true;
    }
}
