package io.github.tekisho.pingponggame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameObjectModelTest {
    final double heightBoundary = 800;
    final double widthBoundary = 600;

    final GameObjectModel firstRacketModel = new RacketModel();
    final GameObjectModel secondRacketModel = new RacketModel();
    final GameObjectModel ballModel = new BallModel();

    @BeforeEach
    void resetPositionsOfGameObjects() {
        firstRacketModel.updatePosition(0, 0);
        secondRacketModel.updatePosition(0, 0);
        ballModel.updatePosition(0, 0);
    }

    @DisplayName("Collision detection test")
    @Test
    void collisionDetectionTest() {
        secondRacketModel.updatePosition(heightBoundary - secondRacketModel.getWidth(), 0);

        ballModel.updatePosition(firstRacketModel.getWidth(), 0);
        assertNull(ballModel.isCollidingWith(firstRacketModel, secondRacketModel));

        ballModel.updatePosition(firstRacketModel.getWidth() - 1, 0);
        assertEquals(firstRacketModel, ballModel.isCollidingWith(firstRacketModel, secondRacketModel));
    }

    @Test
    void resolveCollisionTest() {
        // TODO: Implement test for resolving overlapping (stuck) collision
    }
}
