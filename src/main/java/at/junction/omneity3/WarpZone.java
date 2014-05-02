package at.junction.omneity3;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class WarpZone implements ConfigurationSerializable {

    private Location minFrom;
    private Location maxFrom;

    private List<Block> from;
    private Location to;

    public WarpZone(Map<String, Object> map){
        String[] low = ((String)map.get("minFrom")).split("|");
        String[] high = ((String)map.get("maxFrom")).split("|");
        String[] tempTo = ((String)map.get("to")).split("|");

        minFrom = new Location(Bukkit.getServer().getWorld(low[0]),
                Integer.parseInt(low[1]), Integer.parseInt(low[2]), Integer.parseInt(low[3]),
                Float.parseFloat(low[4]), Float.parseFloat(low[5]));
        maxFrom = new Location(Bukkit.getServer().getWorld(high[0]),
                Integer.parseInt(high[1]), Integer.parseInt(high[2]), Integer.parseInt(high[3]),
                Float.parseFloat(high[4]), Float.parseFloat(high[5]));

        to = new Location(Bukkit.getServer().getWorld(tempTo[0]),
                Integer.parseInt(tempTo[1]), Integer.parseInt(tempTo[2]), Integer.parseInt(tempTo[3]),
                Float.parseFloat(tempTo[4]), Float.parseFloat(tempTo[5]));

        from = new ArrayList<>();
        for (int x=minFrom.getBlockX(); x<maxFrom.getBlockX(); x++){
            for (int y=minFrom.getBlockY(); y<maxFrom.getBlockY(); y++){
                for (int z=minFrom.getBlockZ(); z<maxFrom.getBlockZ(); z++){
                    from.add(new Location(minFrom.getWorld(), x, y, z).getBlock());
                }
            }
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("minFrom", String.format("%s|%s|%s|%s", minFrom.getWorld().getName(), minFrom.getBlockX(), minFrom.getBlockY(), minFrom.getBlockZ()));
        map.put("maxFrom", String.format("%s|%s|%s|%s", maxFrom.getWorld().getName(), maxFrom.getBlockX(), maxFrom.getBlockY(), maxFrom.getBlockZ()));
        map.put("to", String.format("%s|%s|%s|%s|%s|%s", to.getWorld().getName(), to.getBlockX(), to.getBlockY(), to.getBlockZ(), to.getPitch(), to.getYaw()));
        return map;
    }

    public static WarpZone valueOf(Map<String, Object> map){
        return new WarpZone(map);
    }

    public static WarpZone deserialize(Map<String, Object> map){
        return new WarpZone(map);
    }

    public List<Block> getFrom(){
        return from;
    }

    public Location getTo(){
        return to;
    }

}


