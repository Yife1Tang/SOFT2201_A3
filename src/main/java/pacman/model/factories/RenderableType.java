package pacman.model.factories;

public interface RenderableType {
    char HORIZONTAL_WALL = '1';
    char VERTICAL_WALL = '2';
    char UP_LEFT_WALL = '3';
    char UP_RIGHT_WALL = '4';
    char DOWN_LEFT_WALL = '5';
    char DOWN_RIGHT_WALL = '6';
    char PELLET = '7';
    char POWER_PELLET = 'z';
    char PACMAN = 'p';

    // Ghost types with their map file representations
    char BLINKY = 'b';  // Shadow/Blinky
    char PINKY = 's';   // Speedy/Pinky
    char INKY = 'i';    // Bashful/Inky
    char CLYDE = 'c';   // Pokey/Clyde
}