package com.example.wordle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public final class Invalid {

    public static void makeText(Stage ownerStage) {
        Stage stage = new Stage();

        Text text = new Text("Not in word list");
        text.setFill(Color.WHITE);
        text.setStyle("-fx-font-size: 18px;");

        StackPane root = new StackPane(text);
        root.setStyle("-fx-background-color: black; " +
                      "-fx-padding: 10px; " +
                      "-fx-background-radius: 5px;"); 

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Timeline timeline = new Timeline(
            new KeyFrame(Duration.seconds(1), 
                event -> stage.close()
            )
        );
        timeline.play();
    }
}
