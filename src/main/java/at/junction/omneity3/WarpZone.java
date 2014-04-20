package at.junction.omneity3;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.List;

public class WarpZone {

    public List<Block> from;
    public Location to;

    public WarpZone(List<Block> from, Location to) {
        this.from = from;
        this.to = to;
    }
}


