package at.junction.omneity3;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Omneity3 extends JavaPlugin{
    public Configuration config;
    @Override
    public void onEnable(){
        config = new Configuration(this);
        config.load();
        getServer().getPluginManager().registerEvents(new Omneity3Listener(this), this);
    }

    @Override
    public void onDisable(){

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        return true;
    }
}
