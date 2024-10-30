package pacman.model.entity.dynamic.ghost;

/***
 * Represents the different modes of ghosts, which determines how ghosts choose their target locations
 */
public enum GhostMode {
    SCATTER,
    CHASE,
    FRIGHTENED;

    /**
     * Ghosts alternate between SCATTER and CHASE mode normally
     * Note: This does not handle FRIGHTENED mode as it's triggered by power pellets
     */
    public static GhostMode getNextGhostMode(GhostMode ghostMode) {
        return switch (ghostMode) {
            case SCATTER -> CHASE;
            case CHASE -> SCATTER;
            case FRIGHTENED -> SCATTER; // After FRIGHTENED ends, go to SCATTER
        };
    }
}
