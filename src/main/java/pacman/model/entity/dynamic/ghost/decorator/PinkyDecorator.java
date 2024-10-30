package pacman.model.entity.dynamic.ghost.decorator;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Vector2D;

public class PinkyDecorator extends GhostDecorator {
    private static final Image PINKY_IMAGE = new Image("maze/ghosts/pinky.png");
    private static final int LOOK_AHEAD_TILES = 4;
    private static final int TILE_SIZE = 16;

    public PinkyDecorator(Ghost ghost, Vector2D scatterTarget) {
        super(ghost, scatterTarget);  // Top-left corner
    }

    @Override
    protected Vector2D calculateChaseTarget() {
        if (playerPosition == null) {
            return scatterTarget;
        }

        // Target 4 tiles ahead of Pacman's current position
        return new Vector2D(
                playerPosition.getX() + (LOOK_AHEAD_TILES * TILE_SIZE),
                playerPosition.getY() - (LOOK_AHEAD_TILES * TILE_SIZE)
        );
    }

    @Override
    protected Image getNormalImage() {
        return PINKY_IMAGE;
    }
}