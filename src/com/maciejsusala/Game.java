package com.maciejsusala;

import java.util.Random;
import java.util.Scanner;

public class Game {
    private final Scanner scanner = new Scanner(System.in);

    private int numberOfPlayers;

    Board board = new Board();
    WarShips warShips = new WarShips();
    Player player1;
    Player player2;
    Player activePlayer = player1;

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer;
    }

    public void askForNumberOfPlayers() {
        int numberOfPlayers;
        System.out.println("Kapitanie, czy będziesz samotnym wilkiem na morzu, \nczy też wybierzesz rywala do wspólnej bitwy? \n(Wpisz 1 lub 2):");
        while (true) {
            if (scanner.hasNextInt()) {
                numberOfPlayers = scanner.nextInt();
                scanner.nextLine();

                if (numberOfPlayers == 1 || numberOfPlayers == 2) {
                    break;
                } else {
                    System.out.println("Wprowadź poprawną liczbę graczy (1 lub 2).");
                }
            } else {
                System.out.println("Wprowadź poprawną liczbę graczy (1 lub 2).");
                scanner.nextLine(); // Konsumuj błędne dane wejściowe
            }
        }

        player1 = new Player();
        player1.setName(askPlayerForName(1));

        if (numberOfPlayers == 2) {
            player2 = new Player();
            player2.setName(askPlayerForName(2));
        }

        setNumberOfPlayers(numberOfPlayers);
        setActivePlayer(player1);
    }

    public String askPlayerForName(int i) {
        if (i == 1) {
            System.out.println("Podaj swoje imie Kapitanie");
            return scanner.nextLine();
        }
        if (i == 2) {
            System.out.println("Podaj imie swojego Oficera");
            return scanner.nextLine();
        }
        return "Błąd przy wyborze ilości graczy";
    }

    public void makeMove() {
        System.out.print("Pokład artyleryjski prosi o współrzędne strzału (np A1, D6)");

        while (true) {
            String userInput = scanner.next();
            if (validateUserInput(userInput)) {
                int inputRow = board.letterToNumber(userInput.substring(0, 1).toUpperCase());
                int inputCol = Character.getNumericValue(userInput.charAt(1)) - 1;

                if (isValidCoordinates(inputRow, inputCol)) {
                    processMove(inputRow, inputCol);
                    break;
                } else {
                    System.out.println("Podaj poprawne współrzędne (A-G dla liter, 1-7 dla cyfr): ");
                }
            } else {
                System.out.println("Podaj poprawny format współrzędnych (np. A1, B2): ");
            }
        }
    }

    private boolean validateUserInput(String userInput) {
        return userInput.length() == 2 && Character.isLetter(userInput.charAt(0)) && Character.isDigit(userInput.charAt(1));
    }

    private boolean isValidCoordinates(int inputRow, int inputCol) {
        return inputRow >= 0 && inputRow <= 6 && inputCol >= 0 && inputCol <= 6;
    }

    private void processMove(int inputRow, int inputCol) {
        if (board.getBoard()[inputRow][inputCol] == ' ') {
            if (!warShips.getWarShipPositions()[inputRow][inputCol]) {
                handleMiss(inputRow, inputCol);
            } else {
                handleHit(inputRow, inputCol);
            }
        } else {
            System.out.println("Zgubiłeś się na morzu? Tu już strzelałeś! Tracisz swoją turę");
        }
    }

    private void handleMiss(int inputRow, int inputCol) {
        System.out.println();
        showMissedShotMessage();
        putCharOnBoard('*', inputRow, inputCol);
        activePlayer.changeScore(-1);
    }

    private void handleHit(int inputRow, int inputCol) {
        boolean isSink = isSink(inputRow, inputCol);
        if (isSink) {
            showSinkMessage();
            board.getBoard()[inputRow][inputCol] = '0';
            changeHitsToSink(inputRow, inputCol);
            activePlayer.changeScore(+3);
        } else {
            showHitMessage();
            putCharOnBoard('X', inputRow, inputCol);
            activePlayer.changeScore(+1);
        }
        warShips.getWarShipPositions()[inputRow][inputCol] = false;
    }


    private void putCharOnBoard(char symbol, int row, int col) {
        board.getBoard()[row][col] = symbol;
    }

    private boolean isSink(int row, int col) {
        boolean isSink = true;

        if (row > 0) {
            if (warShips.getWarShipPositions()[row - 1][col]
                    && board.getBoard()[row - 1][col] != 'X') {
                isSink = false;
            }
            if (row > 1 && board.getBoard()[row - 1][col] == 'X'
                    && warShips.getWarShipPositions()[row - 2][col]) {
                isSink = false;
            }
        }

        if (row < 6) {
            if (warShips.getWarShipPositions()[row + 1][col]
                    && board.getBoard()[row + 1][col] != 'X') {
                isSink = false;
            }
            if (row < 5 && board.getBoard()[row + 1][col] == 'X'
                    && warShips.getWarShipPositions()[row + 2][col]) {
                isSink = false;
            }
        }
        if (col > 0) {
            if (warShips.getWarShipPositions()[row][col - 1]
                    && board.getBoard()[row][col - 1] != 'X') {
                isSink = false;
            }
            if (col > 1 && board.getBoard()[row][col - 1] == 'X'
                    && warShips.getWarShipPositions()[row][col - 2]) {
                isSink = false;
            }
        }
        if (col < 6) {
            if (warShips.getWarShipPositions()[row][col + 1]
                    && board.getBoard()[row][col + 1] != 'X') {
                isSink = false;
            }
            if (col < 5 && board.getBoard()[row][col + 1] == 'X'
                    && warShips.getWarShipPositions()[row][col + 2]) {
                isSink = false;
            }
        }
        return isSink;
    }

    private void changeHitsToSink(int row, int col) {

        shootAroundSunk(row, col);

        int currentRow = row;
        while (currentRow > 0 && board.getBoard()[currentRow - 1][col] == 'X') {
            shootAroundSunk(currentRow - 1, col);
            board.getBoard()[currentRow - 1][col] = '0';
            currentRow--;

        }

        currentRow = row;
        while (currentRow < 6 && board.getBoard()[currentRow + 1][col] == 'X') {
            shootAroundSunk(currentRow + 1, col);
            board.getBoard()[currentRow + 1][col] = '0';
            currentRow++;

        }

        int currentCol = col;
        while (currentCol > 0 && board.getBoard()[row][currentCol - 1] == 'X') {
            shootAroundSunk(row, currentCol - 1);
            board.getBoard()[row][currentCol - 1] = '0';
            currentCol--;

        }

        currentCol = col;
        while (currentCol < 6 && board.getBoard()[row][currentCol + 1] == 'X') {
            shootAroundSunk(row, currentCol + 1);
            board.getBoard()[row][currentCol + 1] = '0';
            currentCol++;

        }
    }

    private boolean isValidCell(int row, int col) {
        return row >= 0 && row < 7 && col >= 0 && col < 7;
    }

    private void shootAroundSunk(int row, int col) {
        if (isValidCell(row - 1, col) && board.getBoard()[row - 1][col] == ' ') {
            board.getBoard()[row - 1][col] = '*';
        }

        if (isValidCell(row + 1, col) && board.getBoard()[row + 1][col] == ' ') {
            board.getBoard()[row + 1][col] = '*';
        }

        if (isValidCell(row, col - 1) && board.getBoard()[row][col - 1] == ' ') {
            board.getBoard()[row][col - 1] = '*';
        }

        if (isValidCell(row, col + 1) && board.getBoard()[row][col + 1] == ' ') {
            board.getBoard()[row][col + 1] = '*';
        }
    }

    public void showScore() {
        System.out.printf("Wynik gracza %s to %d.%n", activePlayer.getName(), activePlayer.getScore());
        System.out.println();
    }

    public boolean isBoardEmpty() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (warShips.getWarShipPositions()[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printActivePlayer() {
        if (activePlayer == player1) {
            System.out.println("Strzela Kapitan " + activePlayer.getName());
        } else if (activePlayer == player2) {
            System.out.println("Strzela Oficer " + activePlayer.getName());
        }
    }

    public void changePlayer() {
        if (numberOfPlayers == 2) {
            if (activePlayer == player1) {
                setActivePlayer(player2);
            } else {
                setActivePlayer(player1);
            }
        }
    }

    public void showResults() {
        if (numberOfPlayers == 2) {
            if (player1.getScore() > player2.getScore()) {
                System.out.println("Wygrywa " + player1.getName() + " wynikiem " + player1.getScore() + " do " + player2.getScore());
            } else if (player1.getScore() == player2.getScore()) {
                System.out.println("Remis " + player1.getScore() + " do " + player2.getScore());
            } else {
                System.out.println("Wygrywa  " + player2.getName() + "  wynikiem " + player2.getScore() + " do " + player1.getScore());
            }
        }
    }

    private void showMissedShotMessage() {
        String[] messages = {
                "Fale śmiało tańczą wokół strzału. Ale niestety, pudło!",
                "Dźwięk strzału rozchodzi się w ciszy morza. Ale i tak nie trafiłeś!",
                "Powiew morskiego wiatru niestety nie niesie zapachu zwycięstwa. Pudło!",
                "W oddali słychać krzyki mew, ale niestety twoje strzały nie trafiają celu.",
                "Morska pianka tańczy wokół twoich strzałów, ale to nie jest miejsce statku wroga.",
                "Słona bryza morza niestety nie niesie zapachu zwycięstwa. Pudło!",
                "Głębokie fale pochłaniają dźwięk strzału. Niestety, to było pudło.",
                "Echo strzału powraca z pustką. Bez trafienia, kapitanie!",
                "Twoje strzały przegrywają z szumem fal. To było pudło!",
                "Morze milczy po twoim strzale. Niestety, nie trafiłeś!",
                "Dźwięk strzału topi się w szumie morskich fal. Pudło!",
                "Słona bryza morza zawiadamia: pudło! Może następnym razem...",
                "Twoje strzały nie znajdują celu, jak kamień w morzu niewiedzy.",
                "Morze zastyga w oczekiwaniu, ale strzały nie przynoszą triumfu. Pudło!",
                "Wiatr morski niesie opowieść o nieudanych strzałach. Pudło!"
        };
        String randomMessage = getRandomMessage(messages);
        System.out.println(randomMessage);
    }

    private void showSinkMessage() {
        String[] messages = {
                "W morzu odnajdujesz ślad zatopionego wroga. Zatopiony!!",
                "Dźwięk rozbijającego się statku rozbrzmiewa nad morzem. Zatopiłeś wrogi okręt!",
                "Morska fala tańczy wokół wraku wroga. Zatopiony!",
                "Twoje strzały były celne, a teraz wrogi okręt spoczywa na dnie. Zatopiony!",
                "Statkowy wróg uległ potędze twojego flotyllowego ataku. Zatopiony!",
                "Z morza wyłania się tylko cień zatopionego wroga. Świetna robota!",
                "Wiatr morski niesie wieść o zatopieniu wroga. Wrogi okręt jest na dnie!",
                "Twoje strzały przyniosły triumf. Wróg poszedł na dno. Zatopiony!",
                "Morze wita zatopiony wrak wroga. To było epickie zatopienie!",
                "Dźwięk rozbijającego się kadłuba to symfonia twojego zwycięstwa. Zatopiony!",
                "Morska głębia pochłania ostatnie westchnienie zatopionego wroga. Znakomicie!",
                "Morskie prądy otaczają zatopiony wrak. To zatopienie na twoim koncie!",
                "Wiatr niesie wieść o zatopieniu. Twój wróg jest teraz pod wodą!",
                "Pod morską powierzchnią ukrywa się sekret twojego triumfu - Zatopiony!",
                "Wrak wroga spoczywa na dnie morskich głębin. Zatopiony!"
        };
        String randomMessage = getRandomMessage(messages);
        System.out.println(randomMessage);
    }

    private void showHitMessage() {
        String[] messages = {
                "Twoje strzały trafiają cel. To pierwszy trafiony okręt!",
                "Dźwięk uderzenia rozbrzmiewa nad morzem. Trafiony!",
                "Morska fala unosi się wokół trafionego okrętu. Świetna precyzja!",
                "Wiatr niesie wieść o twoim celu. To trafienie!",
                "Strzał morskiego wilka trafia wrogi okręt. Doskonałe trafienie!",
                "Słona bryza morza jest świadkiem twego triumfu. Trafiony!",
                "Twoje strzały są niezawodne. Okręt wroga jest trafiony!",
                "Dźwięk strzału topi się w szumie zwycięstwa. Trafienie!",
                "Morze tańczy wokół celnie trafionego wroga. Doskonale!",
                "Wiatr niesie wieść o twojej celności. Okręt wroga jest trafiony!",
                "Strzały morskiego wilka nie do przeoczenia. To trafienie!",
                "Twoje strzały są jak strzały Neptuna - celne i nieubłagane. Trafienie!",
                "W morskich tonacjach słychać odgłos trafionego wroga. Świetna robota!",
                "Twoje strzały rysują ślad triumfu. Okręt wroga jest trafiony!",
                "Morska głębia oddaje honor twojej celności. Trafiony w dziesiątkę!"
        };
        String randomMessage = getRandomMessage(messages);
        System.out.println(randomMessage);
    }

    private static String getRandomMessage(String[] messages) {
        Random random = new Random();
        int randomIndex = random.nextInt(messages.length);
        return messages[randomIndex];
    }

    public void printGameInstructions() {
        if (numberOfPlayers == 1) {
            System.out.println("Ayee Dowódco! Witaj w grze w statki!");
            System.out.println("Twoje flotylli morskiej powierzone zostało zadanie zatopienia wrogich okrętów,");
            System.out.println("które ukryły się na tym tajemniczym akwenie.");
            System.out.println("\nTwoje zadanie:");
            System.out.println("- Na planszy znajdują się 2 potężne statki trójmasztowe, 3 groźne dwumasztowce,");
            System.out.println("  oraz 3 sprytne jednomasztowce.");
            System.out.println("- Każde zatopienie wroga statku przyniesie Ci 3 punkty.");
            System.out.println("- Precyzyjne trafienie na pokład wroga nagrodzone zostanie 1 punktem.");
            System.out.println("- Niestety, za pudło stracisz 1 punkt.");
            System.out.println("\nCeluj uważnie, i pokaż wrogowi, że morskie wody znają Twoje imię!");
            System.out.println("Powodzenia w tej morskiej bitwie!");
        }
        if (numberOfPlayers == 2) {
            System.out.println("Witajcie Dowódcy na tym tajemniczym morzu!");
            System.out.println("Przed Wami otwiera się pole bitwy, na którym obaj zmierzącie się ze zdradliwymi wrogimi okrętami.");
            System.out.println("\nWasze zadanie:");
            System.out.println("- Na morzu krążą 2 potężne statki trójmasztowe, 3 groźne dwumasztowce oraz 3 sprytne jednomasztowce.");
            System.out.println("- Każde zatopienie wroga statku przynosi Wam 3 punkty.");
            System.out.println("- Precyzyjne trafienie na pokład wroga nagrodzone zostanie 1 punktem.");
            System.out.println("- Uważajcie! Za pudło tracicie 1 punkt.");
            System.out.println("\nRozpocznijcie bitwę i udowodnijcie, kto jest lepszym dowódcą na tych wodach!");
            System.out.println("Powodzenia w tej morskiej rywalizacji!");
        }
    }
}
