package io.github.tekisho.pingponggame.controller;

import io.github.tekisho.pingponggame.model.GameModel;
import io.github.tekisho.pingponggame.manager.StageManager;
import io.github.tekisho.pingponggame.view.GameView;
import io.github.tekisho.pingponggame.view.SettingsView;
import javafx.stage.Stage;

// TODO: add proper logging; add unit-tests (at least for StageManager .initStages & future GameModel gameloop related stuff).
public final class ApplicationController {
    private static ApplicationController instance;

    public static synchronized ApplicationController getInstance() {
        if (instance == null) {
            instance = new ApplicationController();
        }
        return instance;
    }

    private final StageManager stageManager;

    private final GameModel gameModel;
    private final GameView gameView;
    private final GameController gameController;

    private final SettingsView settingsView;
    private final SettingsController settingsController;


    private ApplicationController() {
        stageManager = new StageManager();

        gameModel = new GameModel();
        gameView = new GameView();
        gameController = new GameController(gameView, gameModel);
        gameController.setOpenSettingsRequest(this::openSecondaryStage);

        settingsView = new SettingsView();
        settingsController = new SettingsController(settingsView, gameModel);
    }

    public void initApplication(Stage primaryStage) {
        stageManager.initStages(primaryStage);
        stageManager.setupStages(gameView, settingsView);
    }

    public void openSecondaryStage() {
        stageManager.showSecondaryStage();
    }
}
