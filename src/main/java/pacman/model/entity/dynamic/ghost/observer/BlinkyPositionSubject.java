package pacman.model.entity.dynamic.ghost.observer;

public interface BlinkyPositionSubject {
    void registerBlinkyObserver(BlinkyPositionObserver observer);
    void removeBlinkyObserver(BlinkyPositionObserver observer);
    void notifyBlinkyObservers();
}