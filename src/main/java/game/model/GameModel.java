package game.model;

import csse2002.block.world.Position;
import csse2002.block.world.WorldMap;

/**
 * Concrete implementation of the game's model.
 * Only a thin wrapper around the assignment 2 classes.
 */
public class GameModel extends ModifiableBlockWorldModel {
    /** Current world map. */
    private WorldMap worldMap;
    /** Current builder's position. */
    private Position currentPosition;

    /**
     * Constructs a new {@link GameModel} with no world map or position set.
     */
    public GameModel() {}

    /**
     * Gets the current world map.
     * @return World map.
     */
    @Override
    public WorldMap getWorldMap() {
        return worldMap;
    }

    /**
     * Sets the current world map to the given map and updates the current
     * position to the map's starting position.
     * @param worldMap New world map.
     */
    @Override
    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
        setCurrentPosition(worldMap.getStartPosition());
    }

    /**
     * Gets the builder's current position.
     * @return Position.
     */
    @Override
    public Position getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Sets the builder's current position.
     * @param currentPosition New current position.
     */
    @Override
    public void setCurrentPosition(Position currentPosition) {
        this.currentPosition = currentPosition;
    }

}

