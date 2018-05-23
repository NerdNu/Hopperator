package nu.nerd.hopperator;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;


public class Hopperator extends JavaPlugin {


    public static Hopperator instance;
    private Map<String, Boolean> listenFlags;
    private Map<String, Census> censusMap;


    public void onEnable() {
        Hopperator.instance = this;
        listenFlags = new HashMap<>();
        censusMap = new HashMap<>();
        new HopperListener(this);
        new CommandHandler(this);
    }


    public Map<String, Boolean> getListenFlags() {
        return listenFlags;
    }


    public Boolean getListenerFlag(World world) {
        if (getListenFlags().containsKey(world.getName())) {
            return getListenFlags().get(world.getName());
        }
        return false;
    }


    public Map<String, Census> getCensusMap() {
        return censusMap;
    }


    public Census getCensus(World world) {
        if (!censusMap.containsKey(world.getName())) {
            censusMap.put(world.getName(), new Census(world));
        }
        return censusMap.get(world.getName());
    }


}
