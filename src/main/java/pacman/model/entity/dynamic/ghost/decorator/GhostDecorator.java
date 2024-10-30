package pacman.model.entity.dynamic.ghost.decorator;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.level.Level;

import java.util.Map;
import java.util.Set;

public abstract class GhostDecorator implements Ghost {
    protected final Ghost decoratedGhost;
    protected Vector2D playerPosition;
    protected final Vector2D scatterTarget;

    public GhostDecorator(Ghost ghost, Vector2D scatterTarget) {
        this.decoratedGhost = ghost;
        this.scatterTarget = scatterTarget;
    }

    protected abstract Vector2D calculateChaseTarget();
    protected abstract Image getNormalImage();

    protected Vector2D getScatterTarget() {
        return scatterTarget;
    }

    @Override
    public void update() {
        decoratedGhost.update();
    }

    @Override
    public void update(Vector2D position) {
        this.playerPosition = position;
        decoratedGhost.update(position);
    }


    public Vector2D getChaseTarget() {
        return calculateChaseTarget();
    }

    // Delegate all other methods to decorated ghost
    @Override
    public void setSpeeds(Map<GhostMode, Double> speeds) {
        decoratedGhost.setSpeeds(speeds);
    }

    @Override
    public void setGhostMode(GhostMode ghostMode) {
        decoratedGhost.setGhostMode(ghostMode);
    }

    @Override
    public void die() {
        decoratedGhost.die();
    }

    @Override
    public boolean isDead() {
        return decoratedGhost.isDead();
    }

    @Override
    public double getSpeed() {
        return decoratedGhost.getSpeed();
    }

    @Override
    public Vector2D getPosition() {
        return decoratedGhost.getPosition();
    }

    @Override
    public void setPosition(Vector2D position) {
        decoratedGhost.setPosition(position);
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return decoratedGhost.getPositionBeforeLastUpdate();
    }

    @Override
    public void setPossibleDirections(Set<Direction> directions) {
        decoratedGhost.setPossibleDirections(directions);
    }

    @Override
    public Direction getDirection() {
        return decoratedGhost.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return decoratedGhost.getCenter();
    }

    @Override
    public Layer getLayer() {
        return decoratedGhost.getLayer();
    }

    @Override
    public BoundingBox getBoundingBox() {
        return decoratedGhost.getBoundingBox();
    }

    @Override
    public double getHeight() {
        return decoratedGhost.getHeight();
    }

    @Override
    public double getWidth() {
        return decoratedGhost.getWidth();
    }

    @Override
    public void reset() {
        decoratedGhost.reset();
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return decoratedGhost.collidesWith(renderable);
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        decoratedGhost.collideWith(level, renderable);
    }

    @Override
    public Image getImage() {
        return decoratedGhost.getImage();
    }
}