package at.junction.omneity3;

import org.bukkit.Location;

public class Configuration {
    Omneity3 plugin;

    public class Spawn {
        public Location LOCATION;
        public boolean BED_SPAWN_ENABLED;
        public String FIRST_JOIN_MESSAGE;
        public boolean FIRST_JOIN_ENABLED;
    }

    Spawn spawn;
    public Location SPAWN_LOCATION;

    public Configuration(Omneity3 plugin){
        this.plugin = plugin;
        spawn = new Spawn();
    }

    public void load(){

    }

    public void reload(){

    }
}
