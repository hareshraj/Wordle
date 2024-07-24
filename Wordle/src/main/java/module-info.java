module com.example.wordle {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.wordle to javafx.fxml;
    exports com.example.wordle;
}