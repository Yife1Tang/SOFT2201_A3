package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.physics.Vector2D;

public class ChaseState extends BaseGhostState {
    public ChaseState(Vector2D scatterTarget, double speed, long duration) {
        super(scatterTarget, speed, duration);
    }

    @Override
    public Vector2D getTargetLocation(Vector2D currentPosition, Vector2D playerPosition) {
        return playerPosition; // Basic chase behavior - can be overridden by decorators
    }

    @Override
    public GhostState handleModeChange() {
        // Transition to Scatter mode
        return new ScatterState(scatterTarget, stateSpeed, duration);
    }
}