package io.github.tekisho.pingponggame.manager;

import com.pixelduke.window.ThemeWindowManager;
import com.pixelduke.window.ThemeWindowManagerFactory;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Helper class, that manages all stages (primary, secondary).
 */
public final class StageManager {
    private Stage primaryStage;
    private Stage secondaryStage;

    private final ThemeWindowManager themeWindowManager = ThemeWindowManagerFactory.create();

    private static final boolean DEFAULT_STAGE_THEME = true; // refers to title bar; dark theme - true, light theme - false
    private static final double DEFAULT_STAGE_OPACITY = 0.995;

    /**
     * Thrown when trying to initialize stages more than once.
     */
    static class StageAlreadyInitializedException extends RuntimeException {
        public StageAlreadyInitializedException(String message) {
            super(message);
        }
    }

    /**
     * Initialize stages, accepting {@code primaryStage} as argument.
     * @param primaryStage main stage
     * @throws StageAlreadyInitializedException if stages already exists (i.e., were previously initialized)
     */
    public void initStages(Stage primaryStage) {
        if (this.primaryStage != null || this.secondaryStage != null)
            throw new StageAlreadyInitializedException("Stages are already initialized!");

        this.primaryStage = primaryStage;
        this.secondaryStage = new Stage();
    }

    /**
     *
     * @param initialPrimaryView
     * @param initialSecondaryView
     */
    public void setupStages(Parent initialPrimaryView, Parent initialSecondaryView) {
        setupPrimaryStage(initialPrimaryView);
        setupSecondaryStage(initialSecondaryView);
    }

    /**
     * Setting up stage with a core parameters.
     * @param stage specific stage to be set up
     * @param initialView initial view being set to the stage
     * @param title title of the stage (shown on title bar)
     * @param iconPath icon linked with stage (show on title bar)
     */
    private void setupStage(Stage stage, Parent initialView, String title, String iconPath) {
        stage.setScene(new Scene(initialView));

        stage.getIcons().add(new Image(getClass().getResourceAsStream(iconPath)));
        stage.setTitle(title);

        stage.setOnShown(windowEvent -> themeWindowManager.setDarkModeForWindowFrame(stage, DEFAULT_STAGE_THEME));
        stage.setOpacity(DEFAULT_STAGE_OPACITY);
    }

    /**
     * Setups primary stage by calling {@link #setupStage(Stage, Parent, String, String)}. Always shown after setup,
     * minimum width and height will be equal to the initial size of the stage.
     * @param initialView initial view being set to the primary stage
     */
    private void setupPrimaryStage(Parent initialView) {
        setupStage(primaryStage, initialView, "JPingPong", "/io/github/tekisho/pingponggame/imgs/app-icon-48.png");

        // Stage width & height calculated only AFTER it has been show (by default if scene is set its bounds are determined by the specified size of scene)
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
    }

    /**
     *  Setups secondary stage by calling {@link #setupStage(Stage, Parent, String, String)}.
     * @param initialView initial view being set to the secondary stage
     * @see #setupStage(Stage, Parent, String, String)
     */
    private void setupSecondaryStage(Parent initialView) {
        setupStage(secondaryStage, initialView, "Settings", "/io/github/tekisho/pingponggame/imgs/app-settings-icon-48.png");

        secondaryStage.initOwner(primaryStage);
        secondaryStage.initModality(Modality.APPLICATION_MODAL);

        secondaryStage.setResizable(false);
    }

    /**
     * Represents all existing stage types.
     */
    public enum StageType {
        PRIMARY,
        SECONDARY
    }

    /**
     * Sets new root to stage of type {@link StageType}.
     * @param stageType type of the stage
     * @param root new scene
     */
    public void setStageRoot(StageType stageType, Parent root) {
        switch (stageType) {
            case PRIMARY -> primaryStage.getScene().setRoot(root);
            case SECONDARY -> secondaryStage.getScene().setRoot(root);
        }
    }

    /**
     * Sets title to stage of type {@link StageType}.
     * @param stageType type of the stage
     * @param title stage name
     */
    public void setStageTitle(StageType stageType, String title) {
        switch (stageType) {
            case PRIMARY -> primaryStage.setTitle(title);
            case SECONDARY -> secondaryStage.setTitle(title);
        }
    }

    /**
     * Sets icon to the stage of type {@link StageType}
     * @param stageType type of the stage
     * @param iconPath path to icon
     */
    public void setStageIcon(StageType stageType, String iconPath) {
        switch (stageType) {
            case PRIMARY -> {
                primaryStage.getIcons().clear();
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(iconPath)));
            }
            case SECONDARY -> {
                secondaryStage.getIcons().clear();
                secondaryStage.getIcons().add(new Image(getClass().getResourceAsStream(iconPath)));
            }
        }
    }

    public void showSecondaryStage() {
        secondaryStage.show();
    }
}
