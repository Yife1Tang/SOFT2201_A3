package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.decorator.*;
import pacman.model.entity.dynamic.physics.*;

public class GhostFactory implements RenderableFactory {
    private static final Image FRIGHTENED_IMAGE = new Image("maze/ghosts/frightened.png");

    // Corner positions (28x36 grid, 16px per tile)
    private static final Vector2D TOP_RIGHT = new Vector2D(27 * 16, 0);       // Blinky's corner
    private static final Vector2D TOP_LEFT = new Vector2D(0, 0);              // Pinky's corner
    private static final Vector2D BOTTOM_RIGHT = new Vector2D(27 * 16, 35 * 16); // Inky's corner
    private static final Vector2D BOTTOM_LEFT = new Vector2D(0, 35 * 16);        // Clyde's corner

    private static Ghost blinky = null; // Keep reference to Blinky for Inky's behavior

    @Override
    public Renderable createRenderable(Vector2D position) {
        // Default to creating Blinky if no type specified
        return createRenderable(RenderableType.BLINKY, position);
    }

    @Override
    public Renderable createRenderable(char renderableType, Vector2D position) {
        try {
            // Add the offset like in the scaffold
            position = position.add(new Vector2D(4, -4));

            Image normalImage = getNormalImage(renderableType);
            BoundingBox boundingBox = new BoundingBoxImpl(
                    position,
                    normalImage.getHeight(),  // Use image height
                    normalImage.getWidth()    // Use image width
            );

            KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                    .setPosition(position)
                    .setDirection(Direction.LEFT)
                    .build();

            // Create base ghost
            Ghost baseGhost = new GhostImpl(normalImage,
                    FRIGHTENED_IMAGE,
                    boundingBox,
                    kinematicState);

            // Decorate based on ghost type
            switch (renderableType) {
                case RenderableType.BLINKY:
                    blinky = new BlinkyDecorator(baseGhost, TOP_RIGHT);
                    return blinky;
                case RenderableType.PINKY:
                    return new PinkyDecorator(baseGhost, TOP_LEFT);
                case RenderableType.INKY:
                    if (blinky == null) {
                        throw new IllegalStateException("Blinky must be created before Inky");
                    }
                    return new InkyDecorator(baseGhost, BOTTOM_RIGHT, blinky);
                case RenderableType.CLYDE:
                    return new ClydeDecorator(baseGhost, BOTTOM_LEFT);
                default:
                    throw new IllegalArgumentException("Invalid ghost type: " + renderableType);
            }

        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid ghost configuration | %s", e));
        }
    }

    private Image getNormalImage(char renderableType) {
        switch (renderableType) {
            case RenderableType.BLINKY:
                return new Image("maze/ghosts/blinky.png");
            case RenderableType.PINKY:
                return new Image("maze/ghosts/pinky.png");
            case RenderableType.INKY:
                return new Image("maze/ghosts/inky.png");
            case RenderableType.CLYDE:
                return new Image("maze/ghosts/clyde.png");
            default:
                throw new IllegalArgumentException("Invalid ghost type: " + renderableType);
        }
    }
}