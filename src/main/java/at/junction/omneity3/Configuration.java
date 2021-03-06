package at.junction.omneity3;

import at.junction.omneity3.recipes.Furnace;
import at.junction.omneity3.recipes.Shaped;
import at.junction.omneity3.recipes.Shapeless;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {
    Omneity3 plugin;

    public class Spawn {
        public Location LOCATION;
        public boolean BED_SPAWN_ENABLED;
        public boolean ALWAYS_TP_SPAWN;

        public void load() {
            ConfigurationSection config = plugin.getConfig().getConfigurationSection("spawn");
            String[] temp = config.getString("location").split(",");
            LOCATION = new Location(
                    plugin.getServer().getWorld(temp[0]),
                    Double.parseDouble(temp[1]),
                    Double.parseDouble(temp[2]),
                    Double.parseDouble(temp[3]),
                    Float.parseFloat(temp[4]),
                    Float.parseFloat(temp[5])
            );

            BED_SPAWN_ENABLED = config.getBoolean("bedSpawn");
            ALWAYS_TP_SPAWN = config.getBoolean("always-tp-spawn");
        }
    }

    public class Recipes {

        public List<Furnace> FURNACES;
        public List<Shaped> SHAPED;
        public List<Shapeless> SHAPELESS;

        public void load() {
            FURNACES = (List<Furnace>)plugin.getConfig().getList("recipes.furnace");
            SHAPED = (List<Shaped>)plugin.getConfig().getList("recipes.shaped");
            SHAPELESS = (List<Shapeless>)plugin.getConfig().getList("recipes.shapeless");

            plugin.getLogger().info(String.format("%s FURNACES recipes loaded", FURNACES.size()));
            plugin.getLogger().info(String.format("%s SHAPED recipes loaded", SHAPED.size()));
            plugin.getLogger().info(String.format("%s SHAPELESS recipes loaded", SHAPELESS.size()));
        }
    }

    public class Portals {
        public boolean PORTALS_ENABLED;
        public boolean MODREQ_PORTALS;
        public boolean DISABLE_DESTINATION_BUILD;

        public void load() {
            ConfigurationSection config = plugin.getConfig().getConfigurationSection("portals");
            PORTALS_ENABLED = config.getBoolean("enabled");
            MODREQ_PORTALS = config.getBoolean("require-modreq");
            DISABLE_DESTINATION_BUILD = config.getBoolean("disable-destination-build");
        }
    }

    public class WarpZones {
        public Map<String, WarpZone> WARP_ZONES;

        public void load() {
            WARP_ZONES = new HashMap<>();
            //WARP_ZONES = (Map)plugin.getConfig().getConfigurationSection("warpzones").getValues(true);

        }
    }

    public class FirstJoin {
        private ConfigurationSection config = plugin.getConfig().getConfigurationSection("firstjoin");
        public boolean ENABLED;
        public List<ItemStack> ITEMS;
        public String FIRST_JOIN_MESSAGE;
        public boolean FIRST_JOIN_ENABLED;

        public void load(){
            ITEMS = new ArrayList<>();
            ENABLED = config.getBoolean("enabled");

            for (String s : config.getStringList("items")){
                String [] temp = s.split("x");
                Material mat = Material.valueOf(temp[1]);
                int count = Integer.parseInt(temp[0]);
                ITEMS.add(new ItemStack(mat, count));
            }
            FIRST_JOIN_MESSAGE = config.getString("firstJoinMessage");
            FIRST_JOIN_ENABLED = config.getBoolean("firstJoinMessageEnabled");
        }
    }

    public class PetKill {
        public boolean DISABLED;

        public void load(){
            DISABLED = plugin.getConfig().getBoolean("pet-kill-disabled");
        }
    }


    public Spawn spawn;
    public Recipes recipes;
    public Portals portals;
    public WarpZones warpZones;
    public FirstJoin firstJoin;
    public PetKill petKill;


    public Configuration(Omneity3 plugin) {
        this.plugin = plugin;
        spawn = new Spawn();
        recipes = new Recipes();
        portals = new Portals();
        warpZones = new WarpZones();
        firstJoin = new FirstJoin();
        petKill = new PetKill();
    }

    public void load() {
        spawn.load();
        recipes.load();
        portals.load();
        warpZones.load();
        firstJoin.load();
        petKill.load();
    }

    public void reload() {
        plugin.reloadConfig();
        spawn.load();
        recipes.load();
        portals.load();
        warpZones.load();
        firstJoin.load();
        petKill.load();
    }
}
