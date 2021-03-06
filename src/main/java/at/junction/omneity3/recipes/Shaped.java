package at.junction.omneity3.recipes;

import at.junction.omneity3.Omneity3;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shaped implements ConfigurationSerializable {
    public String[] shape;
    public Map<Character, MaterialData> ingredientMap;
    public ItemStack result;


    public Shaped(Map<String, Object> map){
        shape = ((String)map.get("shape")).split("-");
        List<String> ingredients = ((List<String>)map.get("ingredients"));
        ingredientMap = new HashMap<>();
        for (String i : ingredients){
            String[] temp = i.split("\\|");
            ingredientMap.put(temp[0].charAt(0), Omneity3.toMat(temp[1]));
        }

        result = Omneity3.toMat((String) map.get("result")).toItemStack((int)map.get("count"));
    }

    @Override
    public Map<String, Object> serialize(){
        //Not going to actually serialize, as can't be set in game. Java just needs this method.

        return null;
    }

    public static Shaped valueOf(Map<String, Object> map){
        return new Shaped(map);
    }

    public static Shaped deserialize(Map<String, Object> map){
        return new Shaped(map);
    }

}
