package nu.nerd.hopperator;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;

public class HopperListener implements Listener {


    private Hopperator plugin;


    public HopperListener(Hopperator plugin) {
        this.plugin = plugin;
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onItemMove(InventoryMoveItemEvent event) {
        if (event.getInitiator().getType().equals(InventoryType.HOPPER)) {
            if (!plugin.getListenerFlag(event.getInitiator().getLocation().getWorld())) return;
            Location loc = event.getInitiator().getLocation();
            plugin.getCensus(loc.getWorld()).incrementEventCount(loc.getChunk());
        }
    }


}
