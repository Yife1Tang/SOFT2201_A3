package pacman.model.entity.dynamic.ghost.decorator;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.physics.Vector2D;

public class InkyDecorator extends GhostDecorator {
    private static final Image INKY_IMAGE = new Image("maze/ghosts/inky.png");
    private static final int LOOK_AHEAD_TILES = 2;
    private static final int TILE_SIZE = 16;
    private final Ghost blinky; // Reference to Blinky for position calculation

    public InkyDecorator(Ghost ghost, Vector2D scatterTarget, Ghost blinky) {
        super(ghost, scatterTarget);  // Bottom-right corner
        this.blinky = blinky;
    }

    @Override
    protected Vector2D calculateChaseTarget() {
        if (playerPosition == null || blinky == null) {
            return scatterTarget;
        }

        // Get position two tiles ahead of Pacman
        Vector2D intermediatePoint = new Vector2D(
                playerPosition.getX() + (LOOK_AHEAD_TILES * TILE_SIZE),
                playerPosition.getY() - (LOOK_AHEAD_TILES * TILE_SIZE)
        );

        // Get vector from Blinky to intermediate point
        Vector2D blinkyPos = blinky.getPosition();
        double vectorX = intermediatePoint.getX() - blinkyPos.getX();
        double vectorY = intermediatePoint.getY() - blinkyPos.getY();

        // Double the vector to get Inky's target
        return new Vector2D(
                blinkyPos.getX() + (vectorX * 2),
                blinkyPos.getY() + (vectorY * 2)
        );
    }

    @Override
    protected Image getNormalImage() {
        return INKY_IMAGE;
    }
}