package at.junction.omneity3.recipes;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

public class Furnace implements ConfigurationSerializable {
    public Material source;
    public Material result;

    public Furnace(Map<String, Object> map){
        source = Material.valueOf((String)map.get("source"));
        result = Material.valueOf((String)map.get("result"));
        System.out.println(source);
    }

    @Override
    public Map<String, Object> serialize(){
        Map<String, Object> map = new HashMap<>();
        map.put("source", source);
        map.put("result", result);
        return map;
    }

    public static Furnace valueOf(Map<String, Object> map){
        return new Furnace(map);
    }

    public static Furnace deserialize(Map<String, Object> map){
        return new Furnace(map);
    }
}
