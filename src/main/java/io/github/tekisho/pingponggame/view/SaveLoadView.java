package io.github.tekisho.pingponggame.view;

import io.github.tekisho.pingponggame.view.delegate.SaveLoadDelegate;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class SaveLoadView extends HBox implements View<SaveLoadDelegate> {
    private SaveLoadDelegate delegate;

    @FXML
    private Button saveCurrentButton;
    @FXML
    private Button loadLastButton;

    public SaveLoadView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/io/github/tekisho/pingponggame/fxml/save-load-view.fxml"));
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
    public void setDelegate(SaveLoadDelegate delegate) {
        if (this.delegate != null) {
            throw new RuntimeException("Game delegate already exist!");
        }

        this.delegate = delegate;
        setupEventHandlers();
    }

    @Override
    public void setupEventHandlers() {
        saveCurrentButton.setOnMouseClicked(mouseEvent -> delegate.handleSaveCurrent());
        loadLastButton.setOnMouseClicked(mouseEvent -> delegate.handleLoadLast());
    }
}
