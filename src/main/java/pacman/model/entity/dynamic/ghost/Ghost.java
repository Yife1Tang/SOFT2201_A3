package pacman.model.entity.dynamic.ghost;

import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.observer.PlayerPositionObserver;

import java.util.Map;

/**
 * Represents Ghost entity in Pac-Man Game
 */
public interface Ghost extends DynamicEntity, PlayerPositionObserver {
    void setSpeeds(Map<GhostMode, Double> speeds);
    void setGhostMode(GhostMode ghostMode);
    void die();
    boolean isDead();
    double getSpeed();

}