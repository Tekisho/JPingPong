package io.github.tekisho.pingponggame.view.component;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents game object shown on the view (i.e., view component of the game object model).
 */
public class GameObjectViewComponent extends Canvas {
    private final GraphicsContext graphicsContext;
    private final GameObjectType objectType;

    private static final double DEFAULT_BALL_RADIUS = 20;
    private static final double DEFAULT_RACKET_WIDTH = 10;
    private static final double DEFAULT_RACKET_HEIGHT = 80;

    public enum GameObjectType {
        RACKET,
        BALL
    }

    private double x;
    private double y;

    private double width;
    private double height;

    private Color fillColor;

    /**
     * Constructs game object view component using builder and renders it immediately.
     * @apiNote Used within builder.
     * @implNote If coordinates are not specified, initially located on the left-upper corner (0,0) of its container.
     * @param builder builder of game object view component
     */
    private GameObjectViewComponent(Builder builder) {
        this.graphicsContext = getGraphicsContext2D();
        this.objectType = builder.objectType;

        this.x = builder.x;
        this.y = builder.y;
        this.width = builder.w;
        this.height = builder.h;
        this.fillColor = builder.fillColor;

        render(this.x, this.y, this.width, this.height, this.fillColor);
    }

    // TODO: Consider refactor. No significant reason to keep builder here (could be just converted to constructor)
    public static class Builder {
        private final GameObjectType objectType;

        private double x;
        private double y;

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

    /**
     * Renders game object view component considering coordinates, size, and fill color.
     * @param x OX coordinate of the object
     * @param y OY coordinate of the object
     * @param width width of the object
     * @param height height of the object
     * @param fillColor color used to fill object shape
     */
    public void render(double x, double y, double width, double height, Color fillColor) {
        if ((this.width != width || this.height != height) || this.fillColor != fillColor) {
            updateSize(width, height);
            drawObject(fillColor);
        }
        updatePosition(x, y);
    }

    /**
     * Updates size of the object.
     * @param width new width
     * @param height new height
     */
    private void updateSize(double width, double height) {
        this.width = width;
        this.height = height;

        setWidth(this.width);
        setHeight(this.height);
    }

    /**
     * Updates coordinates of the object.
     * @param x OX coordinate
     * @param y OY coordinate
     */
    private void updatePosition(double x, double y) {
        this.x = x;
        this.y = y;

        // FIXME: Find way to remove visual artifacts during movement (related to floating-point coordinates)
        setTranslateX(this.x);
        setTranslateY(this.y);
    }

    /**
     * Re-draws object considering its color.
     * @param fillColor new color of the object shape
     */
    private void drawObject(Color fillColor) {
        this.fillColor = fillColor;

        graphicsContext.clearRect(0, 0, getWidth(), getHeight());
        graphicsContext.setFill(this.fillColor);

        if (objectType == GameObjectType.RACKET) {
            graphicsContext.fillRect(0, 0, getWidth(), getHeight());
        } else {
            graphicsContext.fillOval(0, 0, getWidth(), getHeight());
        }
    }
}
