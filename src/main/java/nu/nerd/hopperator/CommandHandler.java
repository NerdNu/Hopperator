package nu.nerd.hopperator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;


public class CommandHandler implements CommandExecutor {


    private Hopperator plugin;


    public CommandHandler(Hopperator plugin) {
        this.plugin = plugin;
        plugin.getCommand("hopperator").setExecutor(this);
    }


    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("hopperator")) {

            if (args.length < 1) {
                sender.sendMessage(ChatColor.DARK_AQUA + "You must specify a subcommand: sample, report, tp");
            }

            else if (args[0].equalsIgnoreCase("sample")) {
                if (args.length < 2 || args[1].length() < 1 || Bukkit.getWorld(args[1]) == null) {
                    sender.sendMessage(ChatColor.RED + "Usage: /hopperator sample <worldname>");
                    return true;
                }
                final World world = Bukkit.getWorld(args[1]);
                final Census census = new Census(world);
                plugin.getCensusMap().put(world.getName(), census);
                plugin.getListenFlags().put(world.getName(), true);
                sender.sendMessage(ChatColor.DARK_AQUA + "Monitoring hopper activity. Check back in 30 seconds.");
                new BukkitRunnable() {
                    public void run() {
                        plugin.getListenFlags().put(world.getName(), false);
                        sender.sendMessage(ChatColor.DARK_AQUA + "Hopper sample complete.");
                        printCensus(sender, census, 1);
                    }
                }.runTaskLater(plugin, 600);
            }

            else if (args[0].equalsIgnoreCase("report")) {
                if (args.length < 2 || args[1].length() < 1 || Bukkit.getWorld(args[1]) == null) {
                    sender.sendMessage(ChatColor.RED + "Usage: /hopperator report <world> [page]");
                    return true;
                }
                World world = Bukkit.getWorld(args[1]);
                if (!plugin.getCensusMap().containsKey(world.getName())) {
                    sender.sendMessage(ChatColor.RED + "You must run a sample first.");
                    return true;
                }
                int page = 1;
                if (args.length == 3) {
                    try {
                        page = Integer.parseInt(args[2]);
                    } catch (NumberFormatException ex) {
                        page = 1;
                    }
                }
                Census census = plugin.getCensus(world);
                printCensus(sender, census, page);
            }

            else if (args[0].equalsIgnoreCase("tp")) {
                if (args.length < 3 || args[1].length() < 1 || args[2].length() < 1) {
                    sender.sendMessage(ChatColor.RED + "Usage: /hopperator tp <x> <z>");
                    return true;
                }
                int x;
                int z;
                try {
                    x = Integer.parseInt(args[1]);
                    z = Integer.parseInt(args[2]);
                } catch (NumberFormatException ex) {
                    sender.sendMessage(ChatColor.RED + "Invalid coordinates.");
                    return true;
                }
                Player player = (Player) sender;
                Location loc = player.getWorld().getChunkAt(x, z).getBlock(8, 64, 8).getLocation();
                for (int y = 64; y < 255; y++) {
                    loc.setY(y);
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        break;
                    }
                }
                player.teleport(loc);
            }

            return true;

        }
        return false;
    }


    public void printCensus(CommandSender sender, Census census, int page) {
        List<ChunkData> data = census.getSortedChunkDataList();
        int perPage = 10;
        int pages = (data.size() + perPage - 1) / perPage; //integer division
        int offset = (page - 1) * perPage;

        sender.sendMessage(ChatColor.DARK_AQUA + String.format("--- Hopper Activity by Chunk (%d/%d) ---", page, pages));

        for (int i = offset; i < offset+perPage && i < data.size(); i++) {
            ChunkData chunkData = data.get(i);
            sender.sendMessage(ChatColor.YELLOW + String.format("%d. [%d, %d]   Activity: %d", i+1,
                    chunkData.getX(),
                    chunkData.getZ(),
                    chunkData.getEventCount()
            ));
        }

        sender.sendMessage("View additional pages with /hopperator report <world> [page]");
    }


}
