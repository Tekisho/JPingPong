package io.github.tekisho.pingponggame.view.component;

import javafx.scene.paint.Color;

// TODO: Consider refactor (i.e., changing way render method is used)
/**
 * Represents objects that could be rendered.
 */
public interface Renderable {
    /**
     * Renders object considering coordinates, size, and fill color.
     * @param x OX coordinate of the object
     * @param y OY coordinate of the object
     * @param width width of the object
     * @param height height of the object
     * @param fillColor color used to fill object shape
     */
    void render(double x, double y, double width, double height, Color fillColor);
}
