package io.github.tekisho.pingponggame.manager;

import javafx.application.Platform;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Helper class, responsible for local (sqlite) database connection.
 */
public final class DatabaseManager {
    private static final String DATABASE_PATH = "src/main/resources/io/github/tekisho/pingponggame/sync/gstate.db";
    private static final String CONNECTION_STRING = "jdbc:sqlite:" + DATABASE_PATH;

    public DatabaseManager() {
        init();
    }

    /**
     * Connects to the local database and returns {@link Connection} object.
     * @return {@link Connection}
     * @throws SQLException if DB related error occurs
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING);
    }

    /**
     * Initializes local db if it did not exist before that.
     * @implNote Does not consider actual integrity of the {@code .db} file, just its existence.
     */
    private void init() {
        if (isDatabaseExists()) return;
        setupDatabase();
    }

    /**
     * Checks if database file exists.
     * @return true if {@code .db} file exists, false - otherwise.
     */
    private boolean isDatabaseExists() {
        return Files.exists(Path.of(DATABASE_PATH));
    }

    /**
     * Setups {@code .db} by creating all necessary tables to save session state.
     */
    private void setupDatabase() {
        final String createString =
                "CREATE TABLE GameSession (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP," +
                        "score_limit INTEGER NOT NULL," +
                        "player_one_name VARCHAR(255) NOT NULL," +
                        "player_one_score INTEGER NOT NULL," +
                        "player_two_name VARCHAR(255) NOT NULL," +
                        "player_two_score INTEGER NOT NULL" +
                ");";
        try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
            statement.execute(createString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
