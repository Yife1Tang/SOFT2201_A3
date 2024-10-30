package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.physics.Direction;
import pacman.model.entity.dynamic.physics.Vector2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

public abstract class BaseGhostState implements GhostState {
    protected final Vector2D scatterTarget;
    protected final double stateSpeed;
    protected long startTick;
    protected final long duration;
    protected boolean isTimerActive = false;

    protected BaseGhostState(Vector2D scatterTarget, double stateSpeed, long duration) {
        this.scatterTarget = scatterTarget;
        this.stateSpeed = stateSpeed;
        this.duration = duration;
        this.startTick = 0;
    }

    @Override
    public Direction selectDirection(Set<Direction> possibleDirections, Vector2D currentPosition, Vector2D targetLocation) {
        if (possibleDirections.isEmpty()) {
            return null;
        }

        Map<Direction, Double> distances = new HashMap<>();
        for (Direction direction : possibleDirections) {
            Vector2D potentialPosition = calculateNextPosition(currentPosition, direction);
            distances.put(direction, Vector2D.calculateEuclideanDistance(potentialPosition, targetLocation));
        }

        return Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    protected Vector2D calculateNextPosition(Vector2D currentPosition, Direction direction) {
        return switch (direction) {
            case UP -> new Vector2D(currentPosition.getX(), currentPosition.getY() - 1);
            case DOWN -> new Vector2D(currentPosition.getX(), currentPosition.getY() + 1);
            case LEFT -> new Vector2D(currentPosition.getX() - 1, currentPosition.getY());
            case RIGHT -> new Vector2D(currentPosition.getX() + 1, currentPosition.getY());
        };
    }

    @Override
    public void startTimer(long currentTick) {
        this.startTick = currentTick;
        this.isTimerActive = true;
    }

    @Override
    public void resetTimer() {
        this.startTick = 0;
        this.isTimerActive = false;
    }

    @Override
    public void update(long currentTick) {
        // Check if timer has expired
        if (isTimerActive && (currentTick - startTick >= duration)) {
            isTimerActive = false;
        }
    }

    @Override
    public boolean shouldTransition() {
        return !isTimerActive;
    }

    @Override
    public double getSpeed() {
        return stateSpeed;
    }

    @Override
    public boolean isFrightened() {
        return false;
    }
}