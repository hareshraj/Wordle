package com.example.wordle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class App extends Application {

    public static final ArrayList<String> words = new ArrayList<>();
    private static Stage stageReference;

    @Override
    public void start(Stage stage) throws IOException {
        initializeWordLists();
        stageReference = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main-view.fxml"));
        Parent root = fxmlLoader.load();

        PrimaryController primaryController = fxmlLoader.getController();
        primaryController.createGrid();
        primaryController.createKeyboard();
        primaryController.createTitleHBox();
        primaryController.getRandomWord();
        primaryController.restartIcon.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("restart.png"))));

        Scene scene = new Scene(root, 500, 715);
        scene.setOnKeyReleased(event -> primaryController.gridRequestFocus());
        stage.setScene(scene);
        stage.setTitle("Wordle");
        stage.show();

        primaryController.gridRequestFocus();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void showToast() {
        Invalid.makeText(stageReference);
    }

    public static void quit() {
        if (stageReference != null) {
            stageReference.close();
        } else {
            System.exit(0);
        }
    }

    public void initializeWordLists() {
        InputStream wordStream = getClass().getResourceAsStream("wordslist.txt");
        if (wordStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(wordStream))) {
                reader.lines().forEach(words::add);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else{
            quit();
        }
    }

}
