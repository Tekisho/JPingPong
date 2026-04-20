package io.github.tekisho.pingponggame.manager;

import io.github.tekisho.pingponggame.view.View;

import java.util.EnumMap;

/**
 * Helper class, that manages views.
 */
public final class ViewManager {
    private final EnumMap<ViewType, View<?>> views = new EnumMap<>(ViewType.class);

    /**
     * Represents list of view types, that are ALLOWED
     */
    public enum ViewType {
        GAME,
        SETTINGS,
        SAVE_LOAD
    }

    public void registerView(ViewType sceneType, View<?> view) {
        views.put(sceneType, view);
    }
    public View<?> getView(ViewType sceneType) {
        return views.get(sceneType);
    }
}
