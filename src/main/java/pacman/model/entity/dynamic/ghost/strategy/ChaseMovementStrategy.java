package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;

import java.util.*;

public class ChaseMovementStrategy implements GhostMovementStrategy {
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

        if (targetPosition == null) {
            return currentDirection;
        }

        // Find direction that minimizes distance to target
        Direction bestDirection = null;
        double shortestDistance = Double.MAX_VALUE;

        for (Direction direction : validDirections) {
            Vector2D nextPos = calculateNextPosition(currentPosition, direction);
            double distance = Vector2D.calculateEuclideanDistance(nextPos, targetPosition);

            if (distance < shortestDistance) {
                shortestDistance = distance;
                bestDirection = direction;
            }
        }

        return bestDirection != null ? bestDirection : currentDirection;
    }

    private Vector2D calculateNextPosition(Vector2D position, Direction direction) {
        return switch (direction) {
            case UP -> new Vector2D(position.getX(), position.getY() - 1);
            case DOWN -> new Vector2D(position.getX(), position.getY() + 1);
            case LEFT -> new Vector2D(position.getX() - 1, position.getY());
            case RIGHT -> new Vector2D(position.getX() + 1, position.getY());
        };
    }
}