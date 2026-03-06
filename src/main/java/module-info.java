module io.github.tekisho.pingponggame {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    opens io.github.tekisho.pingponggame.view to javafx.fxml;
    opens io.github.tekisho.pingponggame to javafx.fxml;
    exports io.github.tekisho.pingponggame;
}