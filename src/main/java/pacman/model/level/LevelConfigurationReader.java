package pacman.model.level;

import org.json.simple.JSONObject;
import pacman.model.entity.dynamic.ghost.GhostMode;

import java.util.HashMap;
import java.util.Map;

public class LevelConfigurationReader {
    private static final int TICKS_PER_SECOND = 30; // Game runs at 30 ticks per second

    private final JSONObject levelConfiguration;

    public LevelConfigurationReader(JSONObject levelConfiguration) {
        this.levelConfiguration = levelConfiguration;
    }

    public double getPlayerSpeed() {
        return ((Number) levelConfiguration.get("pacmanSpeed")).doubleValue();
    }

    public Map<GhostMode, Integer> getGhostModeLengths() {
        Map<GhostMode, Integer> ghostModeLengths = new HashMap<>();
        JSONObject modeLengthsObject = (JSONObject) levelConfiguration.get("modeLengths");

        // Convert seconds to ticks
        ghostModeLengths.put(GhostMode.CHASE,
                ((Number) modeLengthsObject.get("chase")).intValue() * TICKS_PER_SECOND);
        ghostModeLengths.put(GhostMode.SCATTER,
                ((Number) modeLengthsObject.get("scatter")).intValue() * TICKS_PER_SECOND);
        ghostModeLengths.put(GhostMode.FRIGHTENED,
                ((Number) modeLengthsObject.get("frightened")).intValue() * TICKS_PER_SECOND);

        return ghostModeLengths;
    }

    public Map<GhostMode, Double> getGhostSpeeds() {
        Map<GhostMode, Double> ghostSpeeds = new HashMap<>();
        JSONObject ghostSpeed = (JSONObject) levelConfiguration.get("ghostSpeed");
        ghostSpeeds.put(GhostMode.CHASE, ((Number) ghostSpeed.get("chase")).doubleValue());
        ghostSpeeds.put(GhostMode.SCATTER, ((Number) ghostSpeed.get("scatter")).doubleValue());
        ghostSpeeds.put(GhostMode.FRIGHTENED, ((Number) ghostSpeed.get("frightened")).doubleValue());
        return ghostSpeeds;
    }
}