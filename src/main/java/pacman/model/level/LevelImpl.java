package pacman.model.level;

import org.json.simple.JSONObject;
import pacman.ConfigurationParseException;
import pacman.model.engine.observer.GameState;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.PhysicsEngine;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.staticentity.StaticEntity;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.entity.staticentity.collectable.PowerPellet;
import pacman.model.level.observer.LevelStateObserver;
import pacman.model.maze.Maze;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Concrete implement of Pac-Man level
 */
public class LevelImpl implements Level {

    private static final int START_LEVEL_TIME = 100;
    private static final int[] FRIGHTENED_GHOST_POINTS = {200, 400, 800, 1600};

    private final Maze maze;
    private final List<LevelStateObserver> observers;
    private List<Renderable> renderables;
    private Controllable player;
    private List<Ghost> ghosts;
    private int tickCount;
    private Map<GhostMode, Integer> modeLengths;
    private int numLives;
    private int points;
    private GameState gameState;
    private List<Renderable> collectables;
    private GhostMode currentGhostMode;
    private int consecutiveGhostsEaten;
    private boolean powerPelletActive;
    private int frightenedModeTimer;
    private int frightenedModeDuration;

    public LevelImpl(JSONObject levelConfiguration, Maze maze) {
        this.renderables = new ArrayList<>();
        this.maze = maze;
        this.tickCount = 0;
        this.observers = new ArrayList<>();
        this.modeLengths = new HashMap<>();
        this.gameState = GameState.READY;
        this.currentGhostMode = GhostMode.SCATTER;
        this.points = 0;
        this.consecutiveGhostsEaten = 0;
        this.powerPelletActive = false;

        initLevel(new LevelConfigurationReader(levelConfiguration));
    }

    private void initLevel(LevelConfigurationReader configReader) {
        // Fetch all renderables for the level
        this.renderables = maze.getRenderables();

        // Set up player
        if (!(maze.getControllable() instanceof Controllable)) {
            throw new ConfigurationParseException("Player entity is not controllable");
        }
        this.player = (Controllable) maze.getControllable();
        this.player.setSpeed(configReader.getPlayerSpeed());
        setNumLives(maze.getNumLives());

        // Set up ghosts
        this.ghosts = maze.getGhosts().stream()
                .map(element -> (Ghost) element)
                .collect(Collectors.toList());

        // Get mode configurations
        this.modeLengths = configReader.getGhostModeLengths();
        this.frightenedModeDuration = modeLengths.get(GhostMode.FRIGHTENED);
        Map<GhostMode, Double> ghostSpeeds = configReader.getGhostSpeeds();

        // Initialize ghosts with speeds and modes
        for (Ghost ghost : this.ghosts) {
            player.registerObserver(ghost);
            ghost.setSpeeds(ghostSpeeds);
            ghost.setGhostMode(this.currentGhostMode);
        }

        // Set up collectables
        this.collectables = new ArrayList<>(maze.getPellets());
    }

    @Override
    public List<Renderable> getRenderables() {
        return this.renderables;
    }

    private List<DynamicEntity> getDynamicEntities() {
        return renderables.stream()
                .filter(e -> e instanceof DynamicEntity)
                .map(e -> (DynamicEntity) e)
                .collect(Collectors.toList());
    }

    private List<StaticEntity> getStaticEntities() {
        return renderables.stream()
                .filter(e -> e instanceof StaticEntity)
                .map(e -> (StaticEntity) e)
                .collect(Collectors.toList());
    }

    @Override
    public void tick() {
        if (this.gameState != GameState.IN_PROGRESS) {
            if (tickCount >= START_LEVEL_TIME) {
                setGameState(GameState.IN_PROGRESS);
                tickCount = 0;
            }
        } else {
            // Handle frightened mode timer
            if (powerPelletActive) {
                frightenedModeTimer--;
                if (frightenedModeTimer <= 0) {
                    endFrightenedMode();
                }
            } else if (tickCount == modeLengths.get(currentGhostMode)) {
                // Update ghost mode
                this.currentGhostMode = GhostMode.getNextGhostMode(currentGhostMode);
                for (Ghost ghost : this.ghosts) {
                    if (!ghost.isDead()) {
                        ghost.setGhostMode(this.currentGhostMode);
                    }
                }
                tickCount = 0;
            }

            // Update Pac-Man animation
            if (tickCount % Pacman.PACMAN_IMAGE_SWAP_TICK_COUNT == 0) {
                this.player.switchImage();
            }

            // Update entities and handle collisions
            List<DynamicEntity> dynamicEntities = getDynamicEntities();
            updateEntities(dynamicEntities);
            handleCollisions(dynamicEntities);
        }
        tickCount++;
    }

    private void updateEntities(List<DynamicEntity> dynamicEntities) {
        for (DynamicEntity entity : dynamicEntities) {
            maze.updatePossibleDirections(entity);
            entity.update();
        }
    }

    private void handleCollisions(List<DynamicEntity> dynamicEntities) {
        for (int i = 0; i < dynamicEntities.size(); ++i) {
            DynamicEntity entityA = dynamicEntities.get(i);

            // Dynamic entity collisions
            for (int j = i + 1; j < dynamicEntities.size(); ++j) {
                DynamicEntity entityB = dynamicEntities.get(j);
                if (entityA.collidesWith(entityB) || entityB.collidesWith(entityA)) {
                    entityA.collideWith(this, entityB);
                    entityB.collideWith(this, entityA);
                }
            }

            // Static entity collisions
            for (StaticEntity staticEntity : getStaticEntities()) {
                if (entityA.collidesWith(staticEntity)) {
                    entityA.collideWith(this, staticEntity);
                    PhysicsEngine.resolveCollision(entityA, staticEntity);
                }
            }
        }
    }

    @Override
    public void collect(Collectable collectable) {
        if (collectable instanceof PowerPellet) {
            handlePowerPelletCollection();
        }

        this.points += collectable.getPoints();
        notifyObserversWithScoreChange(collectable.getPoints());
        this.collectables.remove(collectable);
    }

    private void handlePowerPelletCollection() {
        this.frightenedModeTimer = frightenedModeDuration;
        this.powerPelletActive = true;
        this.consecutiveGhostsEaten = 0;

        for (Ghost ghost : this.ghosts) {
            if (!ghost.isDead()) {
                ghost.setGhostMode(GhostMode.FRIGHTENED);
            }
        }
    }

    private void endFrightenedMode() {
        this.powerPelletActive = false;
        this.consecutiveGhostsEaten = 0;

        for (Ghost ghost : this.ghosts) {
            if (!ghost.isDead()) {
                ghost.setGhostMode(GhostMode.SCATTER);
            }
        }
    }

    @Override
    public void handleGhostEaten(Ghost ghost) {
        if (powerPelletActive) {
            int points = FRIGHTENED_GHOST_POINTS[consecutiveGhostsEaten];
            this.points += points;
            notifyObserversWithScoreChange(points);
            consecutiveGhostsEaten = Math.min(consecutiveGhostsEaten + 1, 3);
        }
    }

    @Override
    public boolean isPlayer(Renderable renderable) {
        return renderable == this.player;
    }

    @Override
    public boolean isCollectable(Renderable renderable) {
        return maze.getPellets().contains(renderable) &&
                ((Collectable) renderable).isCollectable();
    }

    @Override
    public void handleLoseLife() {
        if (gameState == GameState.IN_PROGRESS) {
            for (DynamicEntity entity : getDynamicEntities()) {
                entity.reset();
            }
            setNumLives(numLives - 1);
            setGameState(GameState.READY);
            tickCount = 0;
        }
    }

    @Override
    public void moveLeft() {
        player.left();
    }

    @Override
    public void moveRight() {
        player.right();
    }

    @Override
    public void moveUp() {
        player.up();
    }

    @Override
    public void moveDown() {
        player.down();
    }

    @Override
    public boolean isLevelFinished() {
        return collectables.isEmpty();
    }

    @Override
    public int getPoints() {
        return this.points;
    }

    @Override
    public int getNumLives() {
        return this.numLives;
    }

    private void setNumLives(int numLives) {
        this.numLives = numLives;
        notifyObserversWithNumLives();
    }

    private void setGameState(GameState gameState) {
        this.gameState = gameState;
        notifyObserversWithGameState();
    }

    @Override
    public void handleGameEnd() {
        this.renderables.removeAll(getDynamicEntities());
    }

    // Observer pattern methods
    @Override
    public void registerObserver(LevelStateObserver observer) {
        this.observers.add(observer);
        observer.updateNumLives(this.numLives);
        observer.updateGameState(this.gameState);
    }

    @Override
    public void removeObserver(LevelStateObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObserversWithNumLives() {
        for (LevelStateObserver observer : observers) {
            observer.updateNumLives(this.numLives);
        }
    }

    @Override
    public void notifyObserversWithGameState() {
        for (LevelStateObserver observer : observers) {
            observer.updateGameState(gameState);
        }
    }

    @Override
    public void notifyObserversWithScoreChange(int scoreChange) {
        for (LevelStateObserver observer : observers) {
            observer.updateScore(scoreChange);
        }
    }
}