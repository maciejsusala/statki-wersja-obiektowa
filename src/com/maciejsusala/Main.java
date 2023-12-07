package com.maciejsusala;

public class Main {
    public static void main(String[] args) {
        Game game = new Game();

        System.out.println();

        game.askForNumberOfPlayers();

        game.printGameInstructions();

        game.board.initializeBoard();

        game.warShips.assignWarShipPositions();

        while(!game.isBoardEmpty()) {

            game.printActivePlayer();

            game.board.printBoard();

            game.makeMove();

            game.showScore();

            game.changePlayer();
        }
        game.showResults();
    }
}
