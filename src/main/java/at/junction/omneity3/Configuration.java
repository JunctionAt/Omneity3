package at.junction.omneity3;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
            SHAPED_RECIPES = new ArrayList<ShapedRecipe>();
            if (config.isSet("shaped")) {
                System.out.println("Shaped recipes listmap found");
                List<Map<?, ?>> recipeList = config.getMapList("shaped");
                for (Map<?, ?> map : recipeList) {
                    String item = (String) map.get((Object) "item");
                    //Material item = Material.valueOf((String) map.get((Object) "item"));
                    Integer amount = (Integer) map.get((Object) "amount");
                    List<String> shape = (List<String>) map.get((Object) "shape");
                    Map<String, String> items = (Map<String, String>) map.get((Object) "items");


                    ShapedRecipe recipe = new ShapedRecipe(new ItemStack(Material.valueOf(item), amount));
                    recipe.shape((String[]) shape.toArray());

                    for (String c : items.keySet()) {
                        recipe.setIngredient(c.toCharArray()[0], Material.valueOf(items.get(c)));
                    }
                    SHAPED_RECIPES.add(recipe);
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
