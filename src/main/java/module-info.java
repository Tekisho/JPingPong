module io.github.tekisho.pingponggame {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires com.pixelduke.fxthemes;

    opens io.github.tekisho.pingponggame.view to javafx.fxml;

    exports io.github.tekisho.pingponggame;
    exports io.github.tekisho.pingponggame.controller;
    exports io.github.tekisho.pingponggame.manager;
    exports io.github.tekisho.pingponggame.model;
    exports io.github.tekisho.pingponggame.view;
    exports io.github.tekisho.pingponggame.view.delegate;
    exports io.github.tekisho.pingponggame.view.component;
}