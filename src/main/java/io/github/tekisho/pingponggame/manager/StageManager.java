package io.github.tekisho.pingponggame.manager;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

// TODO: Use JMetro or JNA (Java Native Acess) to turn title bar darkmode.
public class StageManager {
    private Stage primaryStage;
    private Stage secondaryStage;

    private static final double DEFAULT_STAGE_OPACITY = 0.95;

    public void initStages(Stage primaryStage) {
        if (this.primaryStage != null)
            throw new RuntimeException("Stages are already initialized!");

        this.primaryStage = primaryStage;
        secondaryStage = new Stage();
    }

    public void setupStages(Parent initialPrimaryView, Parent initialSecondaryView) {
        setupPrimaryStage(initialPrimaryView);
        setupSecondaryStage(initialSecondaryView);
    }
    private void setupStage(Stage stage, Parent initialView, String title, String iconPath) {
        stage.setScene(new Scene(initialView));
        stage.getIcons().add(new Image(getClass().getResourceAsStream(iconPath)));
        stage.setTitle(title);

        stage.setOpacity(DEFAULT_STAGE_OPACITY);
    }
    private void setupPrimaryStage(Parent initialView) {
        setupStage(primaryStage, initialView, "JPingPong", "/io/github/tekisho/pingponggame/imgs/app-icon-48.png");

        // Stage width & height calculated only AFTER it has been show (by default if scene is set its bounds are determined by the specified size of scene).
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }
    private void setupSecondaryStage(Parent initialView) {
        setupStage(secondaryStage, initialView, "Settings", "/io/github/tekisho/pingponggame/imgs/app-settings-icon-48.png");

        secondaryStage.initOwner(primaryStage);
        secondaryStage.initModality(Modality.APPLICATION_MODAL);

        secondaryStage.setResizable(false);
    }

    public void showSecondaryStage() {
        secondaryStage.show();
    }
}
