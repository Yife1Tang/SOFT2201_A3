# Pac-Man Game Implementation - Assignment 3

## Overview
This is an extension of a Pac-Man game implementation that adds several new features including different ghost types, power pellets, and a frightened mode for ghosts. The game is built using JavaFX and follows object-oriented design principles.

## How to Run
1. Ensure you have the following environment:
   - JDK 17
   - Gradle 7.4.2
   - Unix-based System
2. Navigate to the project root directory
3. Run the following command:
   ```bash
   gradle clean build run
   ```

## Design Patterns Implemented

### 1. Decorator Pattern
- **Location**: `pacman.model.entity.dynamic.ghost.decorator`
- **Files**: 
  - `GhostDecorator.java`
  - `BlinkyDecorator.java`
  - `PinkyDecorator.java`
  - `InkyDecorator.java`
  - `ClydeDecorator.java`
- **Purpose**: Implements different ghost behaviors by decorating a base ghost with unique movement patterns.

### 2. State Pattern
- **Location**: `pacman.model.entity.dynamic.ghost.state`
- **Files**:
  - `GhostState.java`
  - `BaseGhostState.java`
  - `ChaseState.java`
  - `ScatterState.java`
  - `FrightenedState.java`
- **Purpose**: Manages different ghost states (Chase, Scatter, Frightened) and their associated behaviors.

### 3. Strategy Pattern
- **Location**: `pacman.model.entity.dynamic.ghost.strategy`
- **Files**:
  - `GhostMovementStrategy.java`
  - `ChaseMovementStrategy.java`
  - `FrightenedMovementStrategy.java`
  - `GhostMovementStrategyFactory.java`
- **Purpose**: Defines different movement strategies for ghosts based on their current mode.

## Key Features

### Ghost Types
- **Blinky (Shadow)**: Directly targets Pac-Man's position
- **Pinky (Speedy)**: Targets 4 tiles ahead of Pac-Man
- **Inky (Bashful)**: Uses complex targeting involving Blinky's position
- **Clyde (Pokey)**: Alternates between chasing Pac-Man and returning to corner

### Power Pellets
- Larger than normal pellets
- Awards 50 points when collected
- Triggers frightened mode for all ghosts
- Represented by 'z' in the map file

### Ghost Modes
- **Scatter**: Each ghost moves to their designated corner
- **Chase**: Each ghost uses their unique targeting strategy
- **Frightened**: Ghosts move randomly and can be eaten by Pac-Man
  - 1st ghost: 200 points
  - 2nd ghost: 400 points
  - 3rd ghost: 800 points
  - 4th ghost: 1600 points

## Controls
- Use arrow keys to control Pac-Man's movement
- Movement commands are queued and executed when possible

## Additional Notes
- The game uses the configuration from `config.json` for game settings
- Map layout is defined in `new-map.txt`
- All sprite resources are located in `src/main/resources`
