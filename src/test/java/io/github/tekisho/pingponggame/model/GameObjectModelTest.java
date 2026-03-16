package io.github.tekisho.pingponggame.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GameObjectModelTest {
    final double heightBoundary = 800;
    final double widthBoundary = 600;

    final GameObjectModel racketModel = new RacketModel();
    final GameObjectModel ballModel = new BallModel();

    @DisplayName("Collision detection test")
    @Test
    void collisionDetectionTest() {
        ballModel.updatePosition(20, 0);
        assertNull(ballModel.isCollidingWith(racketModel));

        ballModel.updatePosition(19, 0);
        assertEquals(racketModel, ballModel.isCollidingWith(racketModel));
    }
}
