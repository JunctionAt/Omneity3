package at.junction.omneity3.recipes;

import at.junction.omneity3.Omneity3;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.Map;

public class Furnace implements ConfigurationSerializable {
    public MaterialData source;
    public MaterialData result;

    public Furnace(Map<String, Object> map){
        source = Omneity3.toMat((String) map.get("source"));
        result = Omneity3.toMat((String)map.get("result"));
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
