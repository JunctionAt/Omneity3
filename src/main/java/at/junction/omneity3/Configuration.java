package at.junction.omneity3;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Configuration {
    Omneity3 plugin;

    public class Spawn {
        public Location LOCATION;
        public boolean BED_SPAWN_ENABLED;

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
        public List<WarpZone> warpZones;

        public void load() {
            warpZones = new ArrayList<>();

            ConfigurationSection config = plugin.getConfig().getConfigurationSection("warpzones");
            if (config.getBoolean("enabled")) {
                List<Map<?, ?>> zones = config.getMapList("zones");
                for (Map<?, ?> zone : zones) {
                    Map<?, ?> area = (Map<?, ?>) zone.get("area");
                    List<Integer> min = (List<Integer>) area.get("min");
                    List<Integer> max = (List<Integer>) area.get("max");
                    World fromWorld = plugin.getServer().getWorld((String) area.get("world"));


                    Map<?, ?> to = (Map<?, ?>) zone.get("to");
                    List<Integer> toLoc = (List<Integer>) to.get("location");
                    List<Integer> orientation = (List<Integer>) to.get("orientation");
                    World toWorld = plugin.getServer().getWorld((String) to.get("world"));
                    Location toLocation = new Location(toWorld, toLoc.get(0), toLoc.get(1), toLoc.get(2), orientation.get(1), orientation.get(2));

                    List<Block> tempLocations = new ArrayList<>();
                    for (int x = min.get(0); x < max.get(0); x++) {
                        for (int y = min.get(1); y < max.get(1); y++) {
                            for (int z = min.get(2); z < max.get(2); z++) {
                                Location loc = new Location(fromWorld, x, y, z);
                                tempLocations.add(loc.getBlock());
                            }
                        }

                    }
                    warpZones.add(new WarpZone(tempLocations, toLocation));
                }

            }
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

    public class BungeeSwap {
        private ConfigurationSection config = plugin.getConfig().getConfigurationSection("bugnee-swap");
        public boolean ENABLED;
        public String SERVER;

        public void load(){
            ENABLED = config.getBoolean("enabled");
            SERVER = config.getString("server");
        }
    }

    public Spawn spawn;
    public Recipes recipes;
    public Portals portals;
    public WarpZones warpZones;
    public FirstJoin firstJoin;
    public BungeeSwap bungeeSwap;


    public Configuration(Omneity3 plugin) {
        this.plugin = plugin;
        spawn = new Spawn();
        recipes = new Recipes();
        portals = new Portals();
        warpZones = new WarpZones();
        firstJoin = new FirstJoin();
        bungeeSwap = new BungeeSwap();
    }

    public void load() {
        spawn.load();
        recipes.load();
        portals.load();
        warpZones.load();
        firstJoin.load();
        bungeeSwap.load();
    }

    public void reload() {
        plugin.reloadConfig();
        spawn.load();
        portals.load();
        warpZones.load();
        firstJoin.load();
    }
}
