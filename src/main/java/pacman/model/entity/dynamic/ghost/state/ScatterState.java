package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.physics.Vector2D;

public class ScatterState extends BaseGhostState {
    public ScatterState(Vector2D scatterTarget, double speed, long duration) {
        super(scatterTarget, speed, duration);
    }

    @Override
    public Vector2D getTargetLocation(Vector2D currentPosition, Vector2D playerPosition) {
        return scatterTarget;
    }

    @Override
    public GhostState handleModeChange() {
        // Transition to Chase mode
        return new ChaseState(scatterTarget, stateSpeed, duration);
    }
}
