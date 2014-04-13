package at.junction.omneity3;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.Map;

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
        public ArrayList<ShapedRecipe> SHAPED_RECIPES;
        public ArrayList<ShapelessRecipe> SHAPLESS_RECIPES;
        public ArrayList<FurnaceRecipe> FURNACE_RECIPES;

        public void load() {
            ConfigurationSection config = plugin.getConfig().getConfigurationSection("recipes");

            ConfigurationSection shaped = config.getConfigurationSection("shaped");
            System.out.println(shaped);
            for (String shapedItem : shaped.getKeys(false)){
                System.out.println(shapedItem);
                ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.getMaterial(shapedItem), shaped.getInt(shapedItem + ".amount")));
                recipe.shape((String[])shaped.getStringList(shapedItem + ".shape").toArray());
                for (String item: shaped.getConfigurationSection(shapedItem+".items").getKeys(false)){
                    for (char c : item.toCharArray()){
                        recipe.setIngredient(c, Material.getMaterial(shaped.getString(shapedItem + ".items." + c)));
                    }
                }
            }

        }


    }


    Spawn spawn;
    Recipes recipes;

    public Configuration(Omneity3 plugin) {
        this.plugin = plugin;
        spawn = new Spawn();
        recipes = new Recipes();
    }

    public void load() {
        spawn.load();
        recipes.load();
    }

    public void reload() {

    }
}
