package de;

import de.model.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;

import java.util.List;


public class GameStateTest {

    private Level createEmptyLevel() {

        CellType[][] cells = new CellType[5][5];

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                cells[x][y] = CellType.EMPTY;
            }
        }

        return new Level(
            5,
            5,
            cells,
            List.of(),
            new Position(2, 2)
        );
    }

    @Test
    public void snakeMovesRight() {

        Level level = createEmptyLevel();

        Snake snake =
            new Snake(List.of(new Position(2, 2)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.RIGHT
            );

        GameState next = state.tick();

        assertEquals(2, next.snake().head().x());
        assertEquals(2, next.snake().head().y());
    }

    @Test
    public void snakeMovesLeft() {

        Level level = createEmptyLevel();

        Snake snake =
            new Snake(List.of(new Position(2, 2)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.LEFT
            );

        GameState next = state.tick();

        assertEquals(2, next.snake().head().x());
        assertEquals(2, next.snake().head().y());
    }

    @Test
    public void snakeMovesUp() {

        Level level = createEmptyLevel();

        Snake snake =
            new Snake(List.of(new Position(2, 2)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.UP
            );

        GameState next = state.tick();

        assertEquals(2, next.snake().head().x());
        assertEquals(2, next.snake().head().y());
    }

    @Test
    public void snakeMovesDown() {

        Level level = createEmptyLevel();

        Snake snake =
            new Snake(List.of(new Position(2, 2)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.DOWN
            );

        GameState next = state.tick();

        assertEquals(2, next.snake().head().x());
        assertEquals(2, next.snake().head().y());
    }

    @Test
    public void snakeLosesOutOfBounds() {

        Level level = createEmptyLevel();

        Snake snake =
            new Snake(List.of(new Position(0, 0)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.LEFT
            );

        GameState next = state.tick();

        assertEquals(GameState.Status.LOST_OUT_OF_BOUNDS, next.status());
    }

    @Test
    public void snakeHitsWall() {

        CellType[][] cells = new CellType[5][5];

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                cells[x][y] = CellType.EMPTY;
            }
        }

        cells[3][2] = CellType.WALL;

        Level level =
            new Level(
                5,
                5,
                cells,
                List.of(),
                new Position(2, 2)
            );

        Snake snake =
            new Snake(List.of(new Position(2, 2)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.RIGHT
            );

        GameState next = state.tick();

        assertEquals(2, next.snake().head().x());
        assertEquals(2, next.snake().head().y());
    }

    @Ignore
    @Test
    public void snakeSelfCollision() {

        Snake snake =
            new Snake(List.of(
                new Position(2,2),
                new Position(3,2),
                new Position(3,3),
                new Position(2,3)
            ));

        Level level = createEmptyLevel();

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.DOWN
            );

        GameState next = state.tick();

       assertEquals(GameState.Status.LOST_SELF_COLLISION, next.status());
    }

    @Test
    public void pinGetsActivated() {

        Level level = createEmptyLevel();

        Pin pin =
            new Pin(
                new Position(3,2),
                Pin.State.LOW,
                Direction.RIGHT
            );

        Snake snake =
            new Snake(List.of(new Position(2,2)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(pin),
                GameState.Status.RUNNING,
                Direction.RIGHT
            );

        GameState next = state.tick();

       assertTrue(next.pins().get(0).state().isSet());
    }

    @Test
    public void gameIsWonWhenAllPinsActivated() {

        Level level = createEmptyLevel();

        Pin pin =
            new Pin(
                new Position(3,2),
                Pin.State.LOW,
                Direction.RIGHT
            );

        Snake snake =
            new Snake(List.of(new Position(2,2)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(pin),
                GameState.Status.RUNNING,
                Direction.RIGHT
            );

        GameState next = state.tick();

        assertEquals(GameState.Status.WON, next.status());
    }

    @Test
    public void noMovementWhenDirectionNone() {

        Level level = createEmptyLevel();

        Snake snake =
            new Snake(List.of(new Position(2,2)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.NONE
            );

        GameState next = state.tick();

        assertEquals(2, next.snake().head().x());
        assertEquals(2, next.snake().head().y());
    }

    @Test
    public void initialStateIsRunning() {
        Level level = createEmptyLevel();

        Snake snake = new Snake(List.of(new Position(2, 2)));

        GameState state =
            new GameState(
                level,
                snake,
                List.of(),
                GameState.Status.RUNNING,
                Direction.NONE
            );

        assertEquals(GameState.Status.RUNNING, state.status());
        assertEquals(2, state.snake().head().x());
        assertEquals(2, state.snake().head().y());
    }
}

