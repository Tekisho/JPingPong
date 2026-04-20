module io.github.tekisho.pingponggame {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires com.pixelduke.fxthemes;
    requires java.sql;

    opens io.github.tekisho.pingponggame.view to javafx.fxml;
    exports io.github.tekisho.pingponggame;
}