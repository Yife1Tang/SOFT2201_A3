package pacman.model.entity.dynamic.ghost.decorator;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Vector2D;

public class ClydeDecorator extends GhostDecorator {
    private static final Image CLYDE_IMAGE = new Image("maze/ghosts/clyde.png");
    private static final double CHASE_THRESHOLD = 8 * 16; // 8 tiles in pixels

    public ClydeDecorator(Ghost ghost, Vector2D scatterTarget) {
        super(ghost, scatterTarget);  // Bottom-left corner
    }

    @Override
    protected Vector2D calculateChaseTarget() {
        if (playerPosition == null) {
            return scatterTarget;
        }

        // Calculate distance to Pac-Man
        double distanceToPacman = Vector2D.calculateEuclideanDistance(
                decoratedGhost.getPosition(),
                playerPosition
        );

        // If more than 8 tiles away, chase Pac-Man; otherwise, go to scatter target
        return distanceToPacman > CHASE_THRESHOLD ? playerPosition : scatterTarget;
    }

    @Override
    protected Image getNormalImage() {
        return CLYDE_IMAGE;
    }
}