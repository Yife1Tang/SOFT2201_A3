package pacman.model.entity.dynamic.ghost.observer;

import pacman.model.entity.dynamic.physics.Vector2D;

public interface BlinkyPositionObserver {
    void updateBlinkyPosition(Vector2D position);
}