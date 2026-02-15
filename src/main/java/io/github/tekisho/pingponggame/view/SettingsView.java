package io.github.tekisho.pingponggame.view;

import io.github.tekisho.pingponggame.view.delegate.SettingsViewDelegate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingsView extends VBox implements Initializable {
    private SettingsViewDelegate delegate;

    @FXML
    private TextField playerOneNameTextField;
    @FXML
    private TextField playerTwoNameTextField;
    @FXML
    private TextField playerOneScoreTextField;
    @FXML
    private TextField playerTwoScoreTextField;
    @FXML
    private TextField gameEndScoreTextField;

    @FXML
    private TextField gameRacketWidthTextField;
    @FXML
    private TextField gameRacketHeightTextField;
    @FXML
    private TextField gameRacketVelocityTextField;

    @FXML
    private TextField gameBallRadiusTextField;
    @FXML
    private TextField gameBallVelocityTextField;

    @FXML
    private Button confirmChangesButton;

    public SettingsView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/io/github/tekisho/pingponggame/fxml/settings-view.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error occurred during loading of settings view");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupEventHandlers();
    }

    public void setDelegate(SettingsViewDelegate delegate) {
        this.delegate = delegate;
    }

    public void setupEventHandlers() {
        confirmChangesButton.setOnMouseClicked(mouseEvent -> delegate.handleConfirmChangesButtonClick());
    }

    // Players & Score
    public String getPlayerOneName() {
        return playerOneNameTextField.getText();
    }
    public void setPlayerOneName(String name) {
        playerOneNameTextField.setText(name);
    }

    public String getPlayerTwoName() {
        return playerTwoNameTextField.getText();
    }
    public void setPlayerTwoName(String name) {
        playerTwoNameTextField.setText(name);
    }

    public int getPlayerOneScore() {
        return Integer.parseInt(playerOneScoreTextField.getText());
    }
    public void setPlayerOneScore(int score) {
        playerOneScoreTextField.setText(String.valueOf(score));
    }

    public int getPlayerTwoScore() {
        return Integer.parseInt(playerTwoScoreTextField.getText());
    }
    public void setPlayerTwoScore(int score) {
        playerTwoScoreTextField.setText(String.valueOf(score));
    }

    public int getGameEndScore() {
        return Integer.parseInt(gameEndScoreTextField.getText());
    }
    public void setGameEndScore(int score) {
        gameEndScoreTextField.setText(String.valueOf(score));
    }

    // Rackets & Ball
    public double getGameRacketWidth() {
        return Double.parseDouble(gameRacketWidthTextField.getText());
    }
    public void setGameRacketWidth(double width) {
        gameRacketWidthTextField.setText(String.valueOf(width));
    }

    public double getGameRacketHeight() {
        return Double.parseDouble(gameRacketHeightTextField.getText());
    }
    public void setGameRacketHeight(double height) {
        gameRacketHeightTextField.setText(String.valueOf(height));
    }

    public double getGameRacketVelocity() {
        return Double.parseDouble(gameRacketVelocityTextField.getText());
    }
    public void setGameRacketVelocity(double velocity) {
        gameRacketVelocityTextField.setText(String.valueOf(velocity));
    }

    public double getGameBallRadius() {
        return Double.parseDouble(gameBallRadiusTextField.getText());
    }
    public void setGameBallRadius(double radius) {
        gameBallRadiusTextField.setText(String.valueOf(radius));
    }

    public double getGameBallVelocity() {
        return Double.parseDouble(gameBallVelocityTextField.getText());
    }
    public void setGameBallVelocity(double velocity) {
        gameBallVelocityTextField.setText(String.valueOf(velocity));
    }
}
