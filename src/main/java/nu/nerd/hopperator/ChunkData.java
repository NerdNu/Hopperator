package nu.nerd.hopperator;


import org.bukkit.Chunk;

public class ChunkData {

    private int x;
    private int z;

    private int eventCount;

    public ChunkData(Chunk chunk) {
        x = chunk.getX();
        z = chunk.getZ();
        eventCount = 0;
    }

    public void incrementEventCount() {
        eventCount++;
    }

    public int getEventCount() {
        return eventCount;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

}
