package io.github.tekisho.pingponggame.model;

// Clients responsible for calling Notify at the right time; pull model.
public interface Subject {
    void attachObserver(Observer observer);

    void detachObserver(Observer observer);

    void notifyAllObservers();
}
