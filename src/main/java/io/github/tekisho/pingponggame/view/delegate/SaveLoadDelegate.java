package io.github.tekisho.pingponggame.view.delegate;

public interface SaveLoadDelegate extends ViewDelegate {
    void handleSaveCurrent();
    void handleLoadLast();
}
