package io.github.tekisho.pingponggame.controller;

import io.github.tekisho.pingponggame.manager.DatabaseManager;
import io.github.tekisho.pingponggame.manager.RouteManager;
import io.github.tekisho.pingponggame.manager.ViewManager;
import io.github.tekisho.pingponggame.manager.StageManager;
import io.github.tekisho.pingponggame.model.GameModel;
import io.github.tekisho.pingponggame.service.SerializationService;
import io.github.tekisho.pingponggame.service.SyncService;
import io.github.tekisho.pingponggame.view.GameView;
import io.github.tekisho.pingponggame.view.SaveLoadView;
import io.github.tekisho.pingponggame.view.SettingsView;
import javafx.stage.Stage;

import java.util.Optional;

// TODO: add proper logging; add unit-tests (at least for StageManager .initStages & future GameModel game-loop related stuff).
/**
 * Represents main controller of the application, which in charge of proper launch.
 */
public final class ApplicationController {
    private static ApplicationController instance;

    public static synchronized ApplicationController getInstance() {
        if (instance == null) {
            instance = new ApplicationController();
        }
        return instance;
    }

    private final StageManager stageManager;
    private final DatabaseManager databaseManager;

    private final SerializationService serializationService;
    private final SyncService syncService;
    private GameModel gameModel;

    private final GameView gameView;
    private final GameController gameController;

    private final SettingsView settingsView;
    private final SettingsController settingsController;

    private final SaveLoadView saveLoadView;
    private final SaveLoadController saveLoadController;

    private final ViewManager viewManager;
    private final RouteManager routeManager;

    /**
     * Constructs application controller, and every other classes that are required to run correctly.
     */
    private ApplicationController() {
        stageManager = new StageManager();
        databaseManager = new DatabaseManager();

        gameModel = new GameModel();
        serializationService = new SerializationService(gameModel);
        syncService = new SyncService(databaseManager, serializationService);
        gameModel.setSyncService(syncService);

        gameView = new GameView();
        gameController = new GameController(gameView, gameModel);
        gameController.setOpenSettingsRequest(this::openSettingsScreen);
        gameController.setOpenSaveLoadRequest(this::openSaveLoadScreen);

        settingsView = new SettingsView();
        settingsController = new SettingsController(settingsView, gameModel);

        saveLoadView = new SaveLoadView();
        saveLoadController = new SaveLoadController(syncService, saveLoadView, gameModel);

        viewManager = new ViewManager();
        viewManager.registerView(ViewManager.ViewType.GAME, gameView);
        viewManager.registerView(ViewManager.ViewType.SETTINGS, settingsView);
        viewManager.registerView(ViewManager.ViewType.SAVE_LOAD, saveLoadView);

        routeManager = new RouteManager(stageManager, viewManager);
    }

    /**
     * Initializes application by using stage manager.
     * @param primaryStage main stage of the app
     */
    public void initApplication(Stage primaryStage) {
        stageManager.initStages(primaryStage);
        stageManager.setupStages(gameView, settingsView);
    }

    private void openSettingsScreen() {
        routeManager.route(RouteManager.FromStage.SECONDARY, ViewManager.ViewType.SETTINGS);
    }
    private void openSaveLoadScreen() {
        routeManager.route(RouteManager.FromStage.SECONDARY, ViewManager.ViewType.SAVE_LOAD);
    }

    public void loadGameSessionState() {
        Optional<GameModel> lastSession = syncService.restoreLastSessionState();
        lastSession.ifPresent(model -> gameModel = model);
    }

    public void saveGameSessionState() {
        syncService.saveSessionState();
    }
}
