package nu.nerd.hopperator;

import javafx.util.Pair;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Census {


    private World world;
    private Map<Pair<Integer, Integer>, ChunkData> chunkMap;


    public Census(World world) {

        this.world = world;
        this.chunkMap = new HashMap<>();

        for (Chunk chunk: world.getLoadedChunks()) {
            addChunk(chunk);
        }

    }


    public Pair<Integer, Integer> chunkID(Chunk chunk) {
        return new Pair<>(chunk.getX(), chunk.getZ());
    }


    private void addChunk(Chunk chunk) {
        if (!chunkMap.containsKey(chunkID(chunk))) {
            chunkMap.put(chunkID(chunk), new ChunkData(chunk));
        }
    }


    public void incrementEventCount(Chunk chunk) {
        if (chunkMap.containsKey(chunkID(chunk))) {
            chunkMap.get(chunkID(chunk)).incrementEventCount();
        } else {
            addChunk(chunk);
            incrementEventCount(chunk);
        }
    }


    public World getWorld() {
        return world;
    }


    public List<ChunkData> getSortedChunkDataList() {
        List<ChunkData> data;
        data = chunkMap.values().stream()
                .sorted(Comparator.comparing(ChunkData::getEventCount).reversed())
                .filter(v -> v.getEventCount() > 0)
                .collect(Collectors.toList());
        return data;
    }


}
