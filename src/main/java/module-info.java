module io.github.tekisho.pingponggame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens io.github.tekisho.pingponggame.view to javafx.fxml;

    opens io.github.tekisho.pingponggame to javafx.fxml;
    exports io.github.tekisho.pingponggame;
}