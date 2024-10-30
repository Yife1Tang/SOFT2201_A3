package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

import java.util.*;


public class FrightenedMovementStrategy implements GhostMovementStrategy {
    private final Random random = new Random();

    @Override
    public Direction chooseDirection(
            Set<Direction> possibleDirections,
            Vector2D currentPosition,
            Vector2D targetPosition,
            Direction currentDirection,
            int currentDirectionCount,
            int minimumDirectionCount) {

        if (possibleDirections.isEmpty()) {
            return currentDirection;
        }

        // Maintain current direction if minimum count not reached
        if (currentDirection != null && currentDirectionCount < minimumDirectionCount) {
            return currentDirection;
        }

        Set<Direction> validDirections = new HashSet<>(possibleDirections);
        if (currentDirection != null && validDirections.size() > 1) {
            validDirections.remove(currentDirection.opposite());
        }

        if (validDirections.isEmpty()) {
            return currentDirection;
        }

        // Choose random direction from valid options
        List<Direction> directions = new ArrayList<>(validDirections);
        return directions.get(random.nextInt(directions.size()));
    }
}