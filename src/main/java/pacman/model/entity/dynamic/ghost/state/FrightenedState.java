package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class FrightenedState extends BaseGhostState {
    private final Random random = new Random();
    private final double frightenedSpeed;

    public FrightenedState(Vector2D scatterTarget, double frightenedSpeed, long frightenedDuration) {
        super(scatterTarget, frightenedSpeed, frightenedDuration);
        this.frightenedSpeed = frightenedSpeed;
    }

    @Override
    public Vector2D getTargetLocation(Vector2D currentPosition, Vector2D playerPosition) {
        // In frightened mode, target location isn't used for movement
        return currentPosition;
    }

    @Override
    public Direction selectDirection(Set<Direction> possibleDirections, Vector2D currentPosition, Vector2D targetLocation) {
        if (possibleDirections.isEmpty()) {
            return null;
        }

        // Convert set to list for random selection
        List<Direction> directions = new ArrayList<>(possibleDirections);
        return directions.get(random.nextInt(directions.size()));
    }

    @Override
    public double getSpeed() {
        return frightenedSpeed;
    }

    @Override
    public GhostState handleModeChange() {
        // When frightened mode ends, return to Scatter mode
        return new ScatterState(scatterTarget, stateSpeed, duration);
    }

    @Override
    public boolean isFrightened() {
        return true;
    }
}