package io.github.tekisho.pingponggame;

import io.github.tekisho.pingponggame.controller.ApplicationController;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        ApplicationController.getInstance().initApplication(primaryStage);
    }

    @Override
    public void stop() {
        ApplicationController.getInstance().saveGameSessionState();
    }

    public static void main(String[] args) {
        launch();
    }
}
