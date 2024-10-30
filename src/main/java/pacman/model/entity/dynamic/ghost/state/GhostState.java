package pacman.model.entity.dynamic.ghost.state;

import pacman.model.entity.dynamic.physics.Vector2D;
import java.util.Set;
import pacman.model.entity.dynamic.physics.Direction;

public interface GhostState {
    // Core behaviors
    Vector2D getTargetLocation(Vector2D currentPosition, Vector2D playerPosition);
    Direction selectDirection(Set<Direction> possibleDirections, Vector2D currentPosition, Vector2D targetLocation);
    double getSpeed();

    // State management
    void update(long currentTick);
    GhostState handleModeChange();
    boolean shouldTransition();

    // Timer management
    void startTimer(long currentTick);
    void resetTimer();

    // Mode identification
    boolean isFrightened();
}