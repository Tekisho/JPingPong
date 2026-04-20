package io.github.tekisho.pingponggame.service;

import io.github.tekisho.pingponggame.model.GameModel;
import io.github.tekisho.pingponggame.model.PlayerModel;
import io.github.tekisho.pingponggame.model.dto.GameDTO;
import io.github.tekisho.pingponggame.model.dto.PlayerDTO;

public final class SerializationService {
    private final GameModel gameModel;

    public SerializationService(final GameModel gameModel) {
        this.gameModel = gameModel;
    }

    public GameDTO getGameDTO() {
        return new GameDTO(
                gameModel.getGameEndScore(),
                getFirstPlayerDTO(),
                getSecondPlayerDTO()
        );
    }

    private PlayerDTO getPlayerDTO(PlayerModel playerModel) {
        return new PlayerDTO(
                playerModel.getName(),
                playerModel.getScore()
        );
    }
    public PlayerDTO getFirstPlayerDTO() {
        return getPlayerDTO(gameModel.getPlayerOneModel());
    }
    public PlayerDTO getSecondPlayerDTO() {
        return getPlayerDTO(gameModel.getPlayerTwoModel());
    }
}
