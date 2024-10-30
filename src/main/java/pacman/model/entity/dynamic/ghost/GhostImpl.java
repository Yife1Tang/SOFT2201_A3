package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.strategy.ChaseMovementStrategy;
import pacman.model.entity.dynamic.ghost.strategy.FrightenedMovementStrategy;
import pacman.model.entity.dynamic.ghost.strategy.GhostMovementStrategy;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.level.Level;
import pacman.model.maze.Maze;

import java.util.*;

public class GhostImpl implements Ghost {
    private static final int MINIMUM_DIRECTION_COUNT = 8;
    private static final long RESPAWN_DELAY_TICKS = 30;

    private final Layer layer = Layer.FOREGROUND;
    private final Image normalImage;
    private final Image frightenedImage;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private KinematicState kinematicState;

    protected GhostMode currentMode;
    protected final Map<GhostMode, Double> speeds;
    protected Set<Direction> possibleDirections;
    protected Direction currentDirection;
    protected int currentDirectionCount;
    protected Vector2D playerPosition;
    private boolean isDead;
    private long respawnDelay;
    private GhostMovementStrategy movementStrategy;
    public GhostImpl(Image normalImage, Image frightenedImage, BoundingBox boundingBox,
                     KinematicState kinematicState) {
        this.normalImage = normalImage;
        this.frightenedImage = frightenedImage;
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();

        this.currentMode = GhostMode.SCATTER;
        this.speeds = new HashMap<>();
        this.speeds.put(GhostMode.SCATTER, 1.5);
        this.speeds.put(GhostMode.CHASE, 1.5);
        this.speeds.put(GhostMode.FRIGHTENED, 0.75);

        this.possibleDirections = new HashSet<>();
        this.currentDirection = Direction.LEFT;
        this.currentDirectionCount = 0;
        this.isDead = false;
        this.movementStrategy = new ChaseMovementStrategy();
    }

    @Override
    public void update() {
        if (isDead) {
            handleRespawn();
            return;
        }

        updateDirection();
        kinematicState.update();
        boundingBox.setTopLeft(kinematicState.getPosition());
    }

    protected void updateDirection() {
        Vector2D targetPosition = currentMode == GhostMode.SCATTER ?
                getScatterTarget() : getChaseTarget();

        Direction newDirection = movementStrategy.chooseDirection(
                possibleDirections,
                getPosition(),
                targetPosition,
                currentDirection,
                currentDirectionCount,
                MINIMUM_DIRECTION_COUNT
        );

        if (this.currentDirection != newDirection) {
            this.currentDirectionCount = 0;
        }
        this.currentDirection = newDirection;

        if (currentDirection != null) {
            switch (currentDirection) {
                case LEFT -> kinematicState.left();
                case RIGHT -> kinematicState.right();
                case UP -> kinematicState.up();
                case DOWN -> kinematicState.down();
            }
        }
        currentDirectionCount++;
    }

    protected Direction selectDirection(Set<Direction> possibleDirections) {
        if (possibleDirections.isEmpty()) {
            return currentDirection;
        }

        if (currentDirection != null && currentDirectionCount < MINIMUM_DIRECTION_COUNT) {
            return currentDirection;
        }

        Set<Direction> validDirections = new HashSet<>(possibleDirections);
        if (currentDirection != null && validDirections.size() > 1) {
            validDirections.remove(currentDirection.opposite());
        }

        if (validDirections.isEmpty()) {
            return currentDirection;
        }

        if (currentMode == GhostMode.FRIGHTENED) {
            List<Direction> directions = new ArrayList<>(validDirections);
            return directions.get(new Random().nextInt(directions.size()));
        }

        Vector2D targetPosition = currentMode == GhostMode.SCATTER ?
                getScatterTarget() : getChaseTarget();

        if (targetPosition == null) {
            return currentDirection;
        }

        // Find direction that minimizes distance to target
        Direction bestDirection = null;
        double shortestDistance = Double.MAX_VALUE;

        for (Direction direction : validDirections) {
            Vector2D nextPos = kinematicState.getPotentialPosition(direction);
            double distance = Vector2D.calculateEuclideanDistance(nextPos, targetPosition);

            if (distance < shortestDistance) {
                shortestDistance = distance;
                bestDirection = direction;
            }
        }

        return bestDirection != null ? bestDirection : currentDirection;
    }

    protected Vector2D getScatterTarget() {
        return startingPosition;
    }

    protected Vector2D getChaseTarget() {
        return playerPosition;
    }

    private void handleRespawn() {
        respawnDelay++;
        if (respawnDelay >= RESPAWN_DELAY_TICKS) {
            reset();
            isDead = false;
            respawnDelay = 0;
            setGhostMode(GhostMode.SCATTER);
        }
    }

//    @Override
//    public void setGhostMode(GhostMode ghostMode) {
//        this.currentMode = ghostMode;
//        kinematicState.setSpeed(speeds.get(ghostMode));
//        currentDirectionCount = MINIMUM_DIRECTION_COUNT;
//    }

    @Override
    public void setGhostMode(GhostMode ghostMode) {
        this.currentMode = ghostMode;
        if (ghostMode == GhostMode.FRIGHTENED) {
            this.movementStrategy = new FrightenedMovementStrategy();
        } else {
            this.movementStrategy = new ChaseMovementStrategy();
        }
        kinematicState.setSpeed(speeds.get(ghostMode));
        currentDirectionCount = MINIMUM_DIRECTION_COUNT; // Reset direction count
    }

    @Override
    public void setSpeeds(Map<GhostMode, Double> speeds) {
        this.speeds.clear();
        this.speeds.putAll(speeds);
        kinematicState.setSpeed(speeds.get(currentMode));
    }

    @Override
    public double getSpeed() {
        return speeds.get(currentMode);
    }

    @Override
    public void update(Vector2D position) {
        this.playerPosition = position;
    }

    @Override
    public void die() {
        isDead = true;
        respawnDelay = 0;
    }

    @Override
    public boolean isDead() {
        return isDead;
    }

    @Override
    public Image getImage() {
        return currentMode == GhostMode.FRIGHTENED ? frightenedImage : normalImage;
    }

    @Override
    public Vector2D getPosition() {
        return kinematicState.getPosition();
    }

    @Override
    public void setPosition(Vector2D position) {
        kinematicState.setPosition(position);
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return kinematicState.getPreviousPosition();
    }

    @Override
    public Direction getDirection() {
        return currentDirection;
    }

    @Override
    public void setPossibleDirections(Set<Direction> directions) {
        this.possibleDirections = directions;
    }

    @Override
    public Vector2D getCenter() {
        return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());
    }

    @Override
    public Layer getLayer() {
        return layer;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    @Override
    public double getHeight() {
        return boundingBox.getHeight();
    }

    @Override
    public double getWidth() {
        return boundingBox.getWidth();
    }

    @Override
    public void reset() {
        kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(startingPosition)
                .setSpeed(speeds.get(GhostMode.SCATTER))
                .setDirection(Direction.LEFT)
                .build();
        boundingBox.setTopLeft(startingPosition);
        currentDirection = Direction.LEFT;
        currentDirectionCount = MINIMUM_DIRECTION_COUNT;
        isDead = false;
        respawnDelay = 0;
        setGhostMode(GhostMode.SCATTER);
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return boundingBox.collidesWith(kinematicState.getSpeed(),
                kinematicState.getDirection(),
                renderable.getBoundingBox());
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        if (level.isPlayer(renderable)) {
            if (currentMode == GhostMode.FRIGHTENED) {
                die();
                level.handleGhostEaten(this);
            } else if (!isDead) {
                level.handleLoseLife();
            }
        }
    }
}