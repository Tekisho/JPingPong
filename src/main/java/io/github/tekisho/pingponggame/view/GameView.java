package io.github.tekisho.pingponggame.view;

import io.github.tekisho.pingponggame.view.component.GameObjectViewComponent;
import io.github.tekisho.pingponggame.view.delegate.GameViewDelegate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameView extends StackPane implements Initializable {
    private GameViewDelegate delegate;

    @FXML
    private Label playerOneLabel;
    @FXML
    private Label playerTwoLabel;
    @FXML
    private Label gameScoreLabel;
    @FXML
    private Button settingsButton;

    @FXML
    private Pane gameSpace;

    @FXML
    private BorderPane gameEndScreen;
    @FXML
    private Label winnerLabel;
    @FXML
    private Button restartGameButton;

    private GameObjectViewComponent playerOneRacket;
    private GameObjectViewComponent playerTwoRacket;
    private GameObjectViewComponent ball;

    public GameView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/io/github/tekisho/pingponggame/fxml/game-view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error occurred during loading of game view.");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerOneRacket = new GameObjectViewComponent.Builder(GameObjectViewComponent.GameObjectType.RACKET)
                .build();
        playerOneRacket.setId("playerOneRacket");

        playerTwoRacket = new GameObjectViewComponent.Builder(GameObjectViewComponent.GameObjectType.RACKET)
                .build();
        playerTwoRacket.setId("playerTwoRacket");

        ball = new GameObjectViewComponent.Builder(GameObjectViewComponent.GameObjectType.BALL)
                .build();
        ball.setId("ball");

        gameSpace.getChildren().addAll(playerOneRacket, playerTwoRacket, ball);
    }

    public void setDelegate(GameViewDelegate delegate) {
        if (this.delegate != null) {
            throw new RuntimeException("Game delegate already exist!");
        }

        this.delegate = delegate;
        setupEventHandlers();
    }

    public void setupEventHandlers() {
        delegate.handleSetInitialSize();
        delegate.handleResetGameObjectPositions();

        // TODO: try to find a better way to bind width & height properties to not make redundant operations.
        gameSpace.widthProperty().addListener((observable, oldValue, newValue) -> delegate.handleSceneResize(newValue.doubleValue(), getHeight()));
        gameSpace.heightProperty().addListener((observable, oldValue, newValue) -> delegate.handleSceneResize(getWidth(), newValue.doubleValue()));

        settingsButton.setOnMouseClicked(mouseEvent -> delegate.handleSettingsButtonClick());

        restartGameButton.setOnMouseClicked(mouseEvent -> delegate.handleResetGame());
    }

    public void setPlayerOneName(String name) {
        playerOneLabel.setText(name);
    }
    public void setPlayerTwoName(String name) {
        playerTwoLabel.setText(name);
    }

    public void setScore(int scoreOne, int scoreTwo) {
        gameScoreLabel.setText("Score: " + scoreOne + "|" + scoreTwo);
    }

    public void renderPlayerOneRacket(double x, double y, double w, double h, Color fillColor) {
        playerOneRacket.render(x, y, w, h, fillColor);
    }
    public void renderPlayerTwoRacket(double x, double y, double w, double h, Color fillColor) {
        playerTwoRacket.render(x, y, w, h, fillColor);
    }

    public void renderBall(double x, double y, double w, double h, Color fillColor) {
        ball.render(x, y, w, h, fillColor);
    }

    public void toggleGameEndScreen() {
        gameEndScreen.setVisible(!gameEndScreen.isVisible());
    }
    public void updateWinner(String winnerName) {
        winnerLabel.setText(winnerName);
    }
}
