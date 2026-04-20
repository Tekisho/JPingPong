package io.github.tekisho.pingponggame.view;

import io.github.tekisho.pingponggame.view.delegate.ViewDelegate;

public interface View <T extends ViewDelegate> {
    void setDelegate(T delegate);
    void setupEventHandlers();
}
