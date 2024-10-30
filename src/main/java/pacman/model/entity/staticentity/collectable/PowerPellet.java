package pacman.model.entity.staticentity.collectable;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.staticentity.StaticEntityImpl;

public class PowerPellet extends StaticEntityImpl implements Collectable {
    private static final int POINTS = 50;
    private boolean isCollectable;

    public PowerPellet(BoundingBox boundingBox, Layer layer, Image image) {
        super(boundingBox, layer, image);
        this.isCollectable = true;
    }

    @Override
    public void collect() {
        this.isCollectable = false;
        setLayer(Layer.INVISIBLE);
    }

    @Override
    public void reset() {
        this.isCollectable = true;
        setLayer(Layer.BACKGROUND);
    }

    @Override
    public boolean isCollectable() {
        return this.isCollectable;
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public int getPoints() {
        return POINTS;
    }
}