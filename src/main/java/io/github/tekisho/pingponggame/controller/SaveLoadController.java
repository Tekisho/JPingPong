package io.github.tekisho.pingponggame.controller;

import io.github.tekisho.pingponggame.model.GameModel;
import io.github.tekisho.pingponggame.service.SyncService;
import io.github.tekisho.pingponggame.view.SaveLoadView;
import io.github.tekisho.pingponggame.view.delegate.SaveLoadDelegate;

import java.util.Optional;


public class SaveLoadController implements SaveLoadDelegate {
    private final SyncService syncService;
    private final SaveLoadView saveLoadView;
    private GameModel gameModel;

    public SaveLoadController(SyncService syncService, SaveLoadView saveLoadView, GameModel gameModel) {
        this.syncService = syncService;
        this.saveLoadView = saveLoadView;
        this.gameModel = gameModel;

        saveLoadView.setDelegate(this);
    }

    @Override
    public void handleSaveCurrent() {
        syncService.saveSessionState();
    }

    @Override
    public void handleLoadLast() {
        syncService.restoreLastSessionState().ifPresent(gameModel::restoreFrom);
    }
}
