package io.github.tekisho.pingponggame.view.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameObjectViewComponent extends Canvas {
    private final GraphicsContext graphicsContext;
    private final GameObjectType objectType;

    public static final double DEFAULT_BALL_RADIUS = 20;
    public static final double DEFAULT_RACKET_WIDTH = 10;
    public static final double DEFAULT_RACKET_HEIGHT = 80;

    public enum GameObjectType {
        RACKET,
        BALL
    }

    private GameObjectViewComponent(Builder builder) {
        this.graphicsContext = getGraphicsContext2D();

        this.objectType = builder.objectType;

        render(builder.x, builder.y, builder.w, builder.h, builder.fillColor);
    }

    // FIXME: No any significant reason to use Builder here (could be just converted to constructor).
    public static class Builder {
        private final GameObjectType objectType;

        private double x = 0;
        private double y = 0;

        private double w;
        private double h;

        private Color fillColor = Color.GRAY;

        public Builder(GameObjectType objectType) {
            this.objectType = objectType;
        }

        public Builder coordinates(double x, double y) {
            this.x = x;
            this.y = y;

            return this;
        }

        public Builder size(double w, double h) {
            this.w = w;
            this.h = h;

            return this;
        }

        public Builder fillColor(Color fillColor) {
            this.fillColor = fillColor;

            return this;
        }

        public GameObjectViewComponent build() {
            if (objectType == GameObjectType.RACKET) {
                w = DEFAULT_RACKET_WIDTH;
                h = DEFAULT_RACKET_HEIGHT;
            } else {
                w = DEFAULT_BALL_RADIUS;
                h = DEFAULT_BALL_RADIUS;
            }

            return new GameObjectViewComponent(this);
        }
    }

    // TODO: optimise rendering by skipping redundant operations (if data has not changed).
    public void render(double x, double y, double width, double height, Color fillColor) {
        updateSize(width, height);
        updatePosition(x, y);
        drawObject(fillColor);
    }

    private void updateSize(double width, double height) {
        setWidth(width);
        setHeight(height);
    }
    private void updatePosition(double x, double y) {
        setTranslateX(x);
        setTranslateY(y);
    }
    private void drawObject(Color fillColor) {
        graphicsContext.clearRect(0, 0, getWidth(), getHeight());

        graphicsContext.setFill(fillColor);

        if (objectType == GameObjectType.RACKET)
            graphicsContext.fillRect(0, 0, getWidth(), getHeight());
        else
            graphicsContext.fillOval(0, 0, getWidth(), getHeight());
    }
}
