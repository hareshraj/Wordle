package com.example.wordle;

import java.io.Serializable;

public class Log implements Serializable {
    private char letter;
    private int row;
    private int col;

    public Log(char letter, int row, int col) {
        this.letter = letter;
        this.row = row;
        this.col = col;
    }

    public char getLetter() {
        return letter;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
