package com.example.wordle;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class ScoreWindow {

    private ScoreWindow() {
    }

    public static boolean resetGame = false;
    public static boolean quitApplication = false;
    private static int gamesPlayed = 0;
    private static double gamesWon = 0;
    private static int[] guessDistribution = new int[6];

    public static void display(boolean guessed, String winningWord, int guessesTaken) {
        Stage stage = new Stage();

        VBox root = new VBox(15);
        root.setStyle("-fx-background-color: white; -fx-padding: 20;");

        Label mainLabel = new Label();
        if (guessed) {
            gamesWon++;
            mainLabel.setText("You won! The word was:");
        } else {
            mainLabel.setText("The word was:");
        }
        gamesPlayed++;
        Label winningWordLabel = new Label(winningWord.toUpperCase());

        VBox buttonsVBox = new VBox(10);
        Button playAgainButton = new Button("PLAY AGAIN");
        playAgainButton.setOnAction(event -> {
            resetGame = true;
            stage.close();
        });
        Button quitButton = new Button("QUIT");
        quitButton.setOnAction(event -> {
            resetGame = false;
            quitApplication = true;
            stage.close();
        });
        buttonsVBox.getChildren().addAll(playAgainButton, quitButton);

        Label statsLabel = new Label("Statistics:");
        Label gamesPlayedLabel = new Label("Played: " + gamesPlayed);
        Label gamesWonLabel = new Label("Win Percentage: " + (gamesWon/gamesPlayed)*100 + "%");
        VBox guessDistributionBox = new VBox(5);
        guessDistributionBox.getChildren().clear();
        Label guessDistributionLabel = new Label("Guess Distribution:");
        guessDistributionBox.getChildren().add(guessDistributionLabel);

        if (guessed) {
            if (guessesTaken <= 6) {
                guessDistribution[guessesTaken - 1]++;
            }
        }

        for (int i = 0; i < guessDistribution.length; i++) {
            int count = guessDistribution[i];
            if (count > 0) {
                Label countLabel = new Label((i + 2) + " guesses: " + count);
                guessDistributionBox.getChildren().add(countLabel);
            }
        }

        root.getChildren().addAll(mainLabel, winningWordLabel, buttonsVBox, statsLabel, gamesPlayedLabel, gamesWonLabel, guessDistributionBox);
        Scene scene = new Scene(root, 300, 400);
        scene.getStylesheets()
                .add(Objects.requireNonNull(ScoreWindow.class.getResource("wordle.css"))
                        .toExternalForm());
        stage.setScene(scene);
        stage.showAndWait();
    }
}
