package com.maciejsusala;

public class Board {
    private char[][] board;

    public char[][] getBoard() {
        return board;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public void initializeBoard() {
        char[][] board = new char[7][7];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                board[i][j] = ' ';
            }
        }
        setBoard(board);
    }

    public void printBoard() {
        // Print column headers
        System.out.print("  ");
        for (int k = 0; k < 7; k++) {
            System.out.print("  " + (k + 1) + " ");
        }

        System.out.println();

        for (int i = 0; i < 7; i++) {
            // Print the top border of each cell
            System.out.print("  ");
            for (int j = 0; j < 7; j++) {
                System.out.print("+---");
            }
            System.out.println("+");

            // Print the row with cell content
            System.out.print(numberToLetter(i + 1) + " ");
            for (int j = 0; j < 7; j++) {
                // Print each cell with its content
                System.out.print("| " + board[i][j] + " ");
            }
            System.out.println("|");
        }

        // Print the bottom border of the game board
        System.out.print("  ");
        for (int j = 0; j < 7; j++) {
            System.out.print("+---");
        }
        System.out.println("+");
    }


    private char numberToLetter(int number) {
        return switch (number) {
            case 1 -> 'A';
            case 2 -> 'B';
            case 3 -> 'C';
            case 4 -> 'D';
            case 5 -> 'E';
            case 6 -> 'F';
            case 7 -> 'G';
            default -> 0;
        };
    }

    public int letterToNumber(String letter) {
        return switch (letter.toUpperCase()) {
            case "A" -> 0;
            case "B" -> 1;
            case "C" -> 2;
            case "D" -> 3;
            case "E" -> 4;
            case "F" -> 5;
            case "G" -> 6;
            default -> 10;
        };
    }
}


