package de;

import de.model.*;

import java.util.List;

public final class GameState {

    private final Level level;
    private final Snake snake;
    private final List<Pin> pins;
    private final Status status;
    private final Direction pendingDirection;

    public GameState(
        Level level, Snake snake, List<Pin> pins, Status status, Direction pendingDirection) {
        this.level = level;
        this.snake = snake;
        this.pins = pins;
        this.status = status;
        this.pendingDirection = pendingDirection;
    }

    public Level level() {
        return level;
    }

    public Snake snake() {
        return snake;
    }

    public List<Pin> pins() {
        return pins;
    }

    public Status status() {
        return status;
    }

    public Direction pendingDirection() {
        return pendingDirection;
    }

    public GameState tick() {
        if (!status.isRunning() || pendingDirection == Direction.NONE) {
            return this;
        }

        Position nextPos = snake.nextHead(pendingDirection);

        if (!level.isInside(nextPos)) {
            return new GameState(level, snake, pins, Status.LOST_OUT_OF_BOUNDS, Direction.NONE);
        }

        if (level.cellAt(nextPos) == CellType.WALL) {
            return new GameState(level, snake, pins, status, Direction.NONE);
        }

        if (snake.occupies(nextPos)) {
            return new GameState(level, snake, pins, Status.LOST_SELF_COLLISION, Direction.NONE);
        }

        for (Pin pin : pins) {
            if (samePosition(pin.position(), nextPos)) {
                if (pin.state().isSet() || pendingDirection != pin.activationDirection()) {
                    return new GameState(level, snake, pins, status, Direction.NONE);
                }
            }
        }

        List<Pin> updatedPins =
            pins.stream()
                .map(pin -> {
                    if (samePosition(pin.position(), nextPos)
                        && !pin.state().isSet()
                        && pendingDirection == pin.activationDirection()) {
                        return pin.withState(Pin.State.HIGH);
                    }
                    return pin;
                })
                .toList();

        boolean won = updatedPins.stream().allMatch(pin -> pin.state().isSet());

        if (won) {
            return new GameState(level, snake, updatedPins, Status.WON, Direction.NONE);
        }

        Snake newSnake = snake.grow(pendingDirection);

        return new GameState(level, newSnake, updatedPins, Status.RUNNING, pendingDirection);
    }

    private boolean samePosition(Position a, Position b) {
        return a.x() == b.x() && a.y() == b.y();
    }

  public enum Status {
    RUNNING,
    WON,
    LOST_SELF_COLLISION,
    LOST_OUT_OF_BOUNDS;

    public boolean isRunning() {
      return this == RUNNING;
    }
  }
}
