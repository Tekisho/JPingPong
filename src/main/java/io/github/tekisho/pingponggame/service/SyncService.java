package io.github.tekisho.pingponggame.service;

import io.github.tekisho.pingponggame.manager.DatabaseManager;
import io.github.tekisho.pingponggame.model.GameModel;
import io.github.tekisho.pingponggame.model.dao.GameDAO;
import io.github.tekisho.pingponggame.model.dto.GameDTO;

import java.util.Optional;

public class SyncService {
    private final DatabaseManager databaseManager;
    private final SerializationService serializationService;
    private final GameDAO gameDAO;

    public SyncService(DatabaseManager databaseManager, SerializationService serializationService) {
        this.databaseManager = databaseManager;
        this.serializationService = serializationService;
        
        this.gameDAO = new GameDAO(databaseManager);
    }

    public void saveSessionState() {
        gameDAO.save(serializationService.getGameDTO());
    }

    public Optional<GameModel> restoreLastSessionState() {
       Optional<GameDTO> restoredDTO = gameDAO.getLast();

       if (restoredDTO.isPresent()) {
           return restoredDTO.map(gameDTO -> new GameModel.Builder()
                   .scoreLimit(gameDTO.gameEndScore())
                   .playerOne(gameDTO.playerOneDTO())
                   .playerTwo(gameDTO.playerTwoDTO())
                   .build());
       }
       return Optional.empty();
    }
}
