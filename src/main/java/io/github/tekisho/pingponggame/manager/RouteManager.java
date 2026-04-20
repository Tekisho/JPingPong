package io.github.tekisho.pingponggame.manager;

import io.github.tekisho.pingponggame.view.View;
import javafx.scene.Parent;

/**
 * Helper class, that manages view routing for supported stages.
 */
public final class RouteManager {
    private final StageManager stageManager;
    private final ViewManager viewManager;

    /**
     * Represents list of stages from which "routing" allowed.
     */
    public enum FromStage {
        SECONDARY
    }

    public RouteManager(StageManager stageManager, ViewManager viewManager) {
        this.stageManager = stageManager;
        this.viewManager = viewManager;
    }

    /**
     * Switches view of {@code fromStage} to {@code viewType} if possible.
     * @implNote Not allowed to change primary stage view.
     * @param fromStage target stage
     * @param toViewType type of view to be set
     */
    public void route(FromStage fromStage, ViewManager.ViewType toViewType) {
        View<?> toView = viewManager.getView(toViewType);
        stageManager.setStageRoot(StageManager.StageType.SECONDARY, (Parent) toView);
        String title = "unknown", iconPath = "";
        switch (toViewType) {
            case SETTINGS -> {
                title = "Settings";
                iconPath = "/io/github/tekisho/pingponggame/imgs/app-settings-icon-48.png";
            }
            case SAVE_LOAD -> {
                title = "Session Management";
                iconPath = "/io/github/tekisho/pingponggame/imgs/app-save-load-icon-48.png";
            }
        }
        stageManager.setStageTitle(StageManager.StageType.SECONDARY, title);
        stageManager.setStageIcon(StageManager.StageType.SECONDARY, iconPath);
        stageManager.showSecondaryStage();
    }
}
