package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import java.util.Set;

public interface GhostMovementStrategy {
    Direction chooseDirection(
            Set<Direction> possibleDirections,
            Vector2D currentPosition,
            Vector2D targetPosition,
            Direction currentDirection,
            int currentDirectionCount,
            int minimumDirectionCount
    );
}