package pacman.model.factories;

import javafx.scene.image.Image;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.collectable.PowerPellet;

/**
 * Concrete renderable factory for PowerPellet objects
 */
public class PowerPelletFactory implements RenderableFactory {
    private static final Image POWER_PELLET_IMAGE = new Image("maze/pellet.png");
    private final Renderable.Layer layer = Renderable.Layer.BACKGROUND;

    @Override
    public Renderable createRenderable(Vector2D position) {
        try {
            // Apply offset for centering as specified in the requirements
            Vector2D adjustedPosition = position.add(new Vector2D(-8, -8));

            BoundingBox boundingBox = new BoundingBoxImpl(
                    adjustedPosition,
                    POWER_PELLET_IMAGE.getHeight() * 2,
                    POWER_PELLET_IMAGE.getWidth() * 2
            );

            return new PowerPellet(
                    boundingBox,
                    layer,
                    POWER_PELLET_IMAGE
            );

        } catch (Exception e) {
            throw new ConfigurationParseException(
                    String.format("Invalid power pellet configuration | %s", e));
        }
    }
}