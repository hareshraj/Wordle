package com.example.wordle;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import static com.example.wordle.App.words;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class SecondaryController {

    private static SecondaryController INSTANCE = new SecondaryController();

    private final String[][] keyboard = {
        {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"},
        {"A", "S", "D", "F", "G", "H", "J", "K", "L"},
        {"↵", "Z", "X", "C", "V", "B", "N", "M", "←"}
    };

    private int currentRow = 1;
    private int currentCol = 1;
    private String answer;
    private int numberOfGuesses;
    private List<Log> moveLog = new ArrayList<>();
    private boolean[][] lockedLetters;

    private SecondaryController() {
    }

    public static SecondaryController getInstance() {
        return INSTANCE;
    }

    public void createTitleHBox(HBox titleHBox) {
        Label titleLabel = new Label("WORDLE");
        titleLabel.setStyle("-fx-font-size: 30px;");
        titleHBox.getChildren().add(titleLabel);
    }

    public void createGrid(GridPane gridPane) {
        for (int i = 1; i <= 6; i++) {
            for (int j = 1; j <= 5; j++) {
                Label label = new Label();
                label.getStyleClass().add("default-tile");
                gridPane.add(label, j, i);
            }
        }
    }

    public void createKeyboard(GridPane keyboardRow1, GridPane keyboardRow2, GridPane keyboardRow3, GridPane gridPane) {
        for (int i = 0; i < keyboard[0].length; i++) {
            Label label = new Label();
            label.getStyleClass().add("keyboardTile");
            label.setText(keyboard[0][i]);
            label.setOnMouseClicked(e -> onLetterClicked(gridPane, label.getText()));
            keyboardRow1.add(label, i, 1);
        }
        for (int i = 0; i < keyboard[1].length; i++) {
            Label label = new Label();
            label.getStyleClass().add("keyboardTile");
            label.setText(keyboard[1][i]);
            label.setOnMouseClicked(e -> onLetterClicked(gridPane, label.getText()));
            keyboardRow2.add(label, i, 2);
        }
        for (int i = 0; i < keyboard[2].length; i++) {
            Label label = new Label();
            if (i == 0 || i == keyboard[2].length - 1)
                label.getStyleClass().add("keyboardTile");
            else
                label.getStyleClass().add("keyboardTile");
            label.setText(keyboard[2][i]);
            if (keyboard[2][i].equals("↵")) {
                label.setOnMouseClicked(e -> onEnterClicked(gridPane, keyboardRow1, keyboardRow2, keyboardRow3));
            } else if (keyboard[2][i].equals("←")) {
                label.setOnMouseClicked(e -> onBackspaceClicked(gridPane));
            } else {
                label.setOnMouseClicked(e -> onLetterClicked(gridPane, label.getText()));
            }
            keyboardRow3.add(label, i, 3);
        }
    }

    private Label getLabel(GridPane gridPane, int searchRow, int searchColumn) {
        for (Node child : gridPane.getChildren()) {
            if (child instanceof Label) {
                Integer row = GridPane.getRowIndex(child);
                Integer column = GridPane.getColumnIndex(child);
                if (row != null && column != null && row == searchRow && column == searchColumn) {
                    return (Label) child;
                }
            }
        }
        return null;
    }
    
    private Label getLabel(GridPane gridPane, String letter) {
        for (Node child : gridPane.getChildren()) {
            if (child instanceof Label) {
                Label label = (Label) child;
                if (letter.equalsIgnoreCase(label.getText())) {
                    return label;
                }
            }
        }
        return null;
    }
    
    private String getLabelText(GridPane gridPane, int searchRow, int searchColumn) {
        Label label = getLabel(gridPane, searchRow, searchColumn);
        return (label != null) ? label.getText().toLowerCase() : null;
    }
    
    private void setLabelText(GridPane gridPane, int searchRow, int searchColumn, String input) {
        Label label = getLabel(gridPane, searchRow, searchColumn);
        if (label != null) {
            label.setText(input.toUpperCase());
        }
    }

    private void updateRowColors(GridPane gridPane, int searchRow) {

        for (int i = 1; i <= 5; i++) {
            Label label = getLabel(gridPane, searchRow, i);
            String styleClass;
            if (label != null) {
                String currentCharacter = String.valueOf(label.getText().charAt(0)).toLowerCase();
                if (String.valueOf(answer.charAt(i - 1)).toLowerCase().equals(currentCharacter)) {
                    styleClass = "correct-letter";
                } else if (answer.contains(currentCharacter)) {
                    styleClass = "present-letter";
                } else {
                    styleClass = "wrong-letter";
                }
                label.getStyleClass().clear();
                label.getStyleClass().add(styleClass);
            }
        }
    }

    private void updateKeyboardColors(GridPane gridPane, GridPane keyboardRow1, GridPane keyboardRow2, GridPane keyboardRow3) {
        String currentWord = getWordFromCurrentRow(gridPane).toLowerCase();
        for (int i = 1; i <= 5; i++) {
            Label keyboardLabel = new Label();
            String styleClass;
            String currentCharacter = String.valueOf(currentWord.charAt(i - 1));
            String correctCharacter = String.valueOf(answer.charAt(i - 1));

            if (contains(keyboard[0], currentCharacter))
                keyboardLabel = getLabel(keyboardRow1, currentCharacter);
            else if (contains(keyboard[1], currentCharacter))
                keyboardLabel = getLabel(keyboardRow2, currentCharacter);
            else if (contains(keyboard[2], currentCharacter))
                keyboardLabel = getLabel(keyboardRow3, currentCharacter);

            if (currentCharacter.equals(correctCharacter))
                styleClass = "keyboardCorrect";
            else if (answer.contains("" + currentCharacter))
                styleClass = "keyboardPresent";
            else
                styleClass = "keyboardWrong";
            if (keyboardLabel != null) {
                keyboardLabel.getStyleClass().clear();
                keyboardLabel.getStyleClass().add(styleClass);
            }
        }
    }

    private String getWordFromCurrentRow(GridPane gridPane) {
        StringBuilder input = new StringBuilder();
        for (int j = 1; j <= 5; j++)
            input.append(getLabelText(gridPane, currentRow, j));
        return input.toString();
    }

    public void onKeyPressed(GridPane gridPane, GridPane keyboardRow1, GridPane keyboardRow2,
                             GridPane keyboardRow3, KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
            onBackspacePressed(gridPane);
        } else if (keyEvent.getCode().isLetterKey()) {
            onLetterPressed(gridPane, keyEvent);
        }
        if (keyEvent.getCode() == KeyCode.ENTER) {
            onEnterPressed(gridPane, keyboardRow1, keyboardRow2, keyboardRow3);
        }
    }

    private void onBackspacePressed(GridPane gridPane) {
        if ((currentCol == 5 || currentCol == 1)
                && !Objects.equals(getLabelText(gridPane, currentRow, currentCol), "")) {
            setLabelText(gridPane, currentRow, currentCol, "");
        } else if ((currentCol > 1 && currentCol < 5)
                || (currentCol == 5 && Objects.equals(getLabelText(gridPane, currentRow, currentCol), ""))) {
            currentCol--;
            setLabelText(gridPane, currentRow, currentCol, "");
        }
    }

    private void onLetterPressed(GridPane gridPane, KeyEvent keyEvent) {
        if (Objects.equals(getLabelText(gridPane, currentRow, currentCol), "")) {
            setLabelText(gridPane, currentRow, currentCol, keyEvent.getText());
            if (currentCol < 5)
                currentCol++;
        }
    }

    private void onEnterPressed(GridPane gridPane, GridPane keyboardRow1, GridPane keyboardRow2,
                                GridPane keyboardRow3) {
        if (currentRow <= 6 && currentCol == 5) {
            String guess = getWordFromCurrentRow(gridPane).toLowerCase();
            if (guess.equals(answer)) {
                updateRowColors(gridPane, currentRow);
                updateKeyboardColors(gridPane, keyboardRow1, keyboardRow2, keyboardRow3);
                ScoreWindow.display(true, answer, numberOfGuesses);
            } else if (isValidGuess(guess)) {
                numberOfGuesses++;
                updateRowColors(gridPane, currentRow);
                updateKeyboardColors(gridPane, keyboardRow1, keyboardRow2, keyboardRow3);
                if (currentRow == 6) {
                    ScoreWindow.display(false, answer, numberOfGuesses);
                    if (ScoreWindow.resetGame)
                        resetGame(gridPane, keyboardRow1, keyboardRow2, keyboardRow3);
                }
                currentRow++;
                currentCol = 1;
            } else {
                App.showToast();
            }
            if (ScoreWindow.resetGame) {
                resetGame(gridPane, keyboardRow1, keyboardRow2, keyboardRow3);
                ScoreWindow.resetGame = false;
            }
            if (ScoreWindow.quitApplication)
                App.quit();
        }
    }

    private void onLetterClicked(GridPane gridPane, String letter) {
        if (Objects.equals(getLabelText(gridPane, currentRow, currentCol), "")) {
            setLabelText(gridPane, currentRow, currentCol, letter);
            if (currentCol < 5)
                currentCol++;
        }
    }
    
    private void onBackspaceClicked(GridPane gridPane) {
        if ((currentCol == 5 || currentCol == 1)
                && !Objects.equals(getLabelText(gridPane, currentRow, currentCol), "")) {
            setLabelText(gridPane, currentRow, currentCol, "");
        } else if ((currentCol > 1 && currentCol < 5)
                || (currentCol == 5 && Objects.equals(getLabelText(gridPane, currentRow, currentCol), ""))) {
            currentCol--;
            setLabelText(gridPane, currentRow, currentCol, "");
        }
    }

    private void onEnterClicked(GridPane gridPane, GridPane keyboardRow1, GridPane keyboardRow2, GridPane keyboardRow3) {
        if (currentRow <= 6 && currentCol == 5) {
            String guess = getWordFromCurrentRow(gridPane).toLowerCase();
            if (guess.equals(answer)) {
                updateRowColors(gridPane, currentRow);
                updateKeyboardColors(gridPane, keyboardRow1, keyboardRow2, keyboardRow3);
                ScoreWindow.display(true, answer, numberOfGuesses);
            } else if (isValidGuess(guess)) {
                updateRowColors(gridPane, currentRow);
                updateKeyboardColors(gridPane, keyboardRow1, keyboardRow2, keyboardRow3);
                if (currentRow == 6) {
                    ScoreWindow.display(false, answer, numberOfGuesses);
                    if (ScoreWindow.resetGame)
                        resetGame(gridPane, keyboardRow1, keyboardRow2, keyboardRow3);
                }
                currentRow++;
                currentCol = 1;
            } else {
                App.showToast();
            }
            if (ScoreWindow.resetGame) {
                resetGame(gridPane, keyboardRow1, keyboardRow2, keyboardRow3);
                ScoreWindow.resetGame = false;
            }
            if (ScoreWindow.quitApplication)
                App.quit();
        }
    }

    public void recordMove(char letter, int row, int col) {
        moveLog.add(new Log(letter, row, col));
    }

    public List<Log> getMoveLog() {
        return moveLog;
    }

    public void setMoveLog(List<Log> moveLog) {
        this.moveLog = moveLog;
    }

    public void initializeLockedLetters() {
        lockedLetters = new boolean[6][5];
    }

    public boolean isLetterLocked(int row, int col) {
        return lockedLetters[row][col];
    }

    public void lockLetter(int row, int col) {
        lockedLetters[row][col] = true;
}   

    public void getRandomWord() {
        answer = words.get(new Random().nextInt(words.size()));
    }

    private boolean isValidGuess(String guess) {
        return binarySearch(words, guess);
    }

    public void resetGame(GridPane gridPane, GridPane keyboardRow1, GridPane keyboardRow2,
                          GridPane keyboardRow3) {
        getRandomWord();
        numberOfGuesses = 0;
        Label label;
        for (Node child : gridPane.getChildren())
            if (child instanceof Label) {
                label = (Label) child;
                label.getStyleClass().clear();
                label.setText("");
                label.getStyleClass().add("default-tile");
            }

        for (Node child : keyboardRow1.getChildren())
            if (child instanceof Label) {
                label = (Label) child;
                label.getStyleClass().clear();
                label.getStyleClass().add("keyboardTile");
            }

        for (Node child : keyboardRow2.getChildren())
            if (child instanceof Label) {
                label = (Label) child;
                label.getStyleClass().clear();
                label.getStyleClass().add("keyboardTile");
            }

        for (Node child : keyboardRow3.getChildren())
            if (child instanceof Label) {
                label = (Label) child;
                label.getStyleClass().clear();
                label.getStyleClass().add("keyboardTile");
            }

        currentCol = 1;
        currentRow = 1;
    }

    public void restart(ImageView restartIcon, GridPane gridPane, GridPane keyboardRow1,
                        GridPane keyboardRow2, GridPane keyboardRow3) {
        restartIcon.setOnMouseClicked(event -> {
            resetGame(gridPane, keyboardRow1, keyboardRow2, keyboardRow3);
        });
    }

    private boolean binarySearch(ArrayList<String> list, String string) {
        int low = 0, high = list.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int comparison = string.compareTo(list.get(mid));
            if (comparison == 0){
                return true;
            }
            if (comparison > 0){
                low = mid + 1;
            }
            else{
                high = mid - 1;
            }
        }
        return false;
    }

    private boolean contains(String[] array, String letter) {
        for (String string : array)
            if (string.equalsIgnoreCase(letter))
                return true;
        return false;
    }

}
