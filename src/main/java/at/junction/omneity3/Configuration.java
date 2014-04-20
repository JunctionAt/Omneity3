package at.junction.omneity3;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class Configuration {
    Omneity3 plugin;

    public class Spawn {
        public Location LOCATION;
        public boolean BED_SPAWN_ENABLED;
        public String FIRST_JOIN_MESSAGE;
        public boolean FIRST_JOIN_ENABLED;

        public void load() {
            ConfigurationSection config = plugin.getConfig().getConfigurationSection("spawn");
            String[] temp = config.getString("location").split(",");
            spawn.LOCATION = new Location(
                    plugin.getServer().getWorld(temp[0]),
                    Double.parseDouble(temp[1]),
                    Double.parseDouble(temp[2]),
                    Double.parseDouble(temp[3]),
                    Float.parseFloat(temp[4]),
                    Float.parseFloat(temp[5])
            );

            spawn.BED_SPAWN_ENABLED = config.getBoolean("bedSpawn");
            spawn.FIRST_JOIN_MESSAGE = config.getString("firstJoinMessage");
            spawn.FIRST_JOIN_ENABLED = config.getBoolean("firstJoinMessageEnabled");

        }
    }

    public class Recipes {
        public void load() {
            //Realistically, this should be reading from YAML
            //I think YAML is retarded
            //It needs to go die in a fire
            //Bukkit's YAML lib cannot deal with paths through yaml that contain an array
            //As that is something this requires to work
            //I said fuck it, and wrote this comment instead.
            //Then hard-coded recipes in Omneity3
        }
    }

    public class Portals {
        boolean PORTALS_ENABLED;
        boolean MODREQ_PORTALS;
        boolean DISABLE_DESTINATION_BUILD;
        public void load() {
            ConfigurationSection config = plugin.getConfig().getConfigurationSection("portals");
            PORTALS_ENABLED = config.getBoolean("enabled");
            MODREQ_PORTALS = config.getBoolean("require-modreq");
            DISABLE_DESTINATION_BUILD = config.getBoolean("disable-destination-build");
        }
    }


    Spawn spawn;
    Recipes recipes;
    Portals portals;

    public Configuration(Omneity3 plugin) {
        this.plugin = plugin;
        spawn = new Spawn();
        recipes = new Recipes();
        portals = new Portals();
    }

    public void load() {
        spawn.load();
        recipes.load();
        portals.load();
    }

    public void reload() {
        spawn.load();
        portals.load();
    }
}
