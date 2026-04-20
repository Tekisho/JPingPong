package io.github.tekisho.pingponggame.model.dao;

import io.github.tekisho.pingponggame.manager.DatabaseManager;
import io.github.tekisho.pingponggame.model.dto.GameDTO;
import io.github.tekisho.pingponggame.model.dto.PlayerDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameDAO implements Dao<GameDTO> {
    private final DatabaseManager databaseManager;
    private List<GameDTO> gameList = new ArrayList<>();

    public GameDAO(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;
        gameList = getAll();
    }

    @Override
    public Optional<GameDTO> get(long id) {
        // TODO
        return Optional.empty();
    }

    public Optional<GameDTO> getLast() {
        try (Connection connection = databaseManager.getConnection()) {
            String selectString = "SELECT * FROM GameSession ORDER BY created_at DESC LIMIT 1;";
            try (ResultSet resultSet = connection.createStatement().executeQuery(selectString)) {
                if (resultSet.next())
                    return Optional.of(new GameDTO(
                            resultSet.getInt(3),
                            new PlayerDTO(resultSet.getString(4), resultSet.getInt(5)),
                            new PlayerDTO(resultSet.getString(6), resultSet.getInt(7))
                    ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<GameDTO> getAll() {
        // TODO
        return List.of();
    }

    @Override
    public void save(GameDTO gameDTO) {
        try (Connection connection = databaseManager.getConnection()) {
            String insertString = "INSERT INTO GameSession(" +
                    "score_limit, player_one_name, player_one_score, player_two_name, player_two_score)" +
                    "VALUES(?, ?, ?, ?, ?);";
            try (PreparedStatement statement = connection.prepareStatement(insertString)) {
                statement.setInt(1, gameDTO.gameEndScore());

                statement.setString(2, gameDTO.playerOneDTO().name());
                statement.setInt(3, gameDTO.playerOneDTO().score());

                statement.setString(4, gameDTO.playerTwoDTO().name());
                statement.setInt(5, gameDTO.playerTwoDTO().score());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GameDTO gameObjectDTO) {
        // TODO
    }

    @Override
    public void delete(GameDTO gameObjectDTO) {
        // TODO
    }
}
