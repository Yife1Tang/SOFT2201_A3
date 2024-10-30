package pacman.model.entity.dynamic.ghost.strategy;

import pacman.model.entity.dynamic.ghost.GhostMode;

public class GhostMovementStrategyFactory {
    private static final GhostMovementStrategyFactory instance = new GhostMovementStrategyFactory();

    private final GhostMovementStrategy chaseStrategy;
    private final GhostMovementStrategy frightenedStrategy;

    private GhostMovementStrategyFactory() {
        this.chaseStrategy = new ChaseMovementStrategy();
        this.frightenedStrategy = new FrightenedMovementStrategy();
    }

    public static GhostMovementStrategyFactory getInstance() {
        return instance;
    }

    public GhostMovementStrategy getStrategy(GhostMode mode) {
        return switch (mode) {
            case FRIGHTENED -> frightenedStrategy;
            case CHASE, SCATTER -> chaseStrategy;
        };
    }
}