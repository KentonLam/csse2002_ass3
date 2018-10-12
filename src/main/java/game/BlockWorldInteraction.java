package game;

import csse2002.block.world.Block;
import csse2002.block.world.Builder;
import csse2002.block.world.InvalidBlockException;
import csse2002.block.world.NoExitException;
import csse2002.block.world.Position;
import csse2002.block.world.Tile;
import csse2002.block.world.TooHighException;
import csse2002.block.world.TooLowException;
import csse2002.block.world.WorldMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

public class BlockWorldInteraction {
    private WorldMap worldMap;
    private Builder builder;
    private final List<TileChangedCallback> tileChangedCallbacks = new ArrayList<>();
    private final List<BuilderMovedCallback> builderMovedCallbacks = new ArrayList<>();
    private final Map<Position, Tile> positionTileMap = new HashMap<>();

    public BlockWorldInteraction() {

    }

    public BlockWorldInteraction(WorldMap worldMap) {
        setWorldMap(worldMap);
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
        this.builder = worldMap.getBuilder();

        loadPositions();
    }

    private void loadPositions() {
        // We need to have a copy of the position to tile mapping.
        Set<Position> seenPositions = new HashSet<>();
        Queue<Position> positionsToSearch = new LinkedList<>();
        positionsToSearch.add(worldMap.getStartPosition());
        while (!positionsToSearch.isEmpty()) {
            Position currentPos = positionsToSearch.remove();
            if (!seenPositions.add(currentPos)) {
                continue;
            }

            Tile currentTile = worldMap.getTile(currentPos);
            positionTileMap.put(currentPos, currentTile);
            for (Direction dir : Direction.values()) {
                if (currentTile.getExits().get(dir.name()) != null) {
                    positionsToSearch.add(
                            Utilities.addPos(currentPos, dir.asPosition()));
                }
            }
        }
    }

    public WorldMap getWorldMap() {
        return this.worldMap;
    }

    public void move(Direction direction) throws NoExitException {
        Tile newTile = builder.getCurrentTile()
                .getExits().get(direction.name());
        builder.moveTo(newTile);
    }

    public void dig() throws InvalidBlockException, TooLowException {
        builder.digOnCurrentTile();
    }

    public void moveBlock(Direction direction)
            throws NoExitException, InvalidBlockException, TooHighException {
        builder.getCurrentTile().moveBlock(direction.name());
    }

    public int countBlocks(BlockTypes type) {
        int count = 0;
        for (Block block : builder.getInventory()) {
            if (type.blockClass.isAssignableFrom(block.getClass())) {
                count++;
            }
        }
        return count;
    }

    public void dropBlock(BlockTypes type)
            throws NoSuchElementException, TooHighException, InvalidBlockException {
        List<Block> inventory = builder.getInventory();
        for (int i = 0; i < inventory.size(); i++) {
            if (type.blockClass.isAssignableFrom(inventory.get(i).getClass())) {
                builder.dropFromInventory(i);
                return;
            }
        }
        throw new NoSuchElementException(
                "No block of type "+type.name()+" in builder's inventory.");
    }
}
