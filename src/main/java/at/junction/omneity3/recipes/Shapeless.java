package at.junction.omneity3.recipes;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Shapeless implements ConfigurationSerializable {
    public List<Material> ingredients;
    public ItemStack result;
    public Shapeless(Map<String, Object> map){
        List<String> temp = ((List<String>)map.get("ingredients"));
        ingredients = new ArrayList<>();
        for (String t : temp){
            ingredients.add(Material.valueOf(t));
        }
        result = new ItemStack(Material.valueOf((String)map.get("result")), (int)map.get("count"));
    }


        @Override
    public Map<String, Object> serialize(){
        //Not going to actually serialize, as can't be set in game. Java just needs this method.

        return null;
    }

    public static Shapeless valueOf(Map<String, Object> map){
        return new Shapeless(map);
    }

    public static Shapeless deserialize(Map<String, Object> map){
        return new Shapeless(map);
    }

}
