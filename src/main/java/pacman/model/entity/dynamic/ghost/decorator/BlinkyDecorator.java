package pacman.model.entity.dynamic.ghost.decorator;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Vector2D;

public class BlinkyDecorator extends GhostDecorator {
    private static final Image BLINKY_IMAGE = new Image("maze/ghosts/blinky.png");

    public BlinkyDecorator(Ghost ghost, Vector2D scatterTarget) {
        super(ghost, scatterTarget);  // Top-right corner
    }

    @Override
    protected Vector2D calculateChaseTarget() {
        // Blinky directly targets Pacman's current position
        return playerPosition;
    }

    @Override
    protected Image getNormalImage() {
        return BLINKY_IMAGE;
    }
}