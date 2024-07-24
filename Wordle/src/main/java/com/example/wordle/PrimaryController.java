package com.example.wordle;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class PrimaryController {

    private final SecondaryController secondaryController = SecondaryController.getInstance();

    @FXML
    public GridPane gridPane;
    @FXML
    public GridPane keyboardRow1;
    @FXML
    public GridPane keyboardRow2;
    @FXML
    public GridPane keyboardRow3;
    @FXML
    public HBox titleHBox;
    @FXML
    public ImageView restartIcon;
    @FXML
    private Button saveButton;
    @FXML
    private Button loadButton;

    public void initialize() {
        saveButton.setOnAction(this::saveGame);
        loadButton.setOnAction(this::loadGame);
    }

    public void createGrid() {
        secondaryController.createGrid(gridPane);
    }

    public void createKeyboard() {
        secondaryController.createKeyboard(keyboardRow1, keyboardRow2, keyboardRow3, gridPane);
    }

    public void gridRequestFocus() {
        gridPane.requestFocus();
    }

    public void recordMove(char letter, int row, int col) {
        secondaryController.recordMove(letter, row, col);
    }

    @FXML
    protected void onKeyPressed(KeyEvent keyEvent) {
        secondaryController.onKeyPressed(gridPane, keyboardRow1, keyboardRow2, keyboardRow3, keyEvent);
    }

    public void getRandomWord() {
        secondaryController.getRandomWord();
    }

    public void createTitleHBox() {
        secondaryController.createTitleHBox(titleHBox);
    }

    public void restart() {
        secondaryController.restart(restartIcon, gridPane, keyboardRow1, keyboardRow2, keyboardRow3);
    }

    @FXML
    public void saveGame(ActionEvent event) {
        LoadSave.saveGame(secondaryController.getMoveLog(), "saved_game.ser");
    }

    @FXML
    public void loadGame(ActionEvent event) {
        List<Log> moveLog = LoadSave.loadGame("saved_game.ser");
        if (moveLog != null) {
            secondaryController.setMoveLog(moveLog);
        } else {
        }
    }

}
