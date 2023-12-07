package com.maciejsusala;

import java.util.Random;

public class WarShips {
    private boolean[][] warShipPositions;

    private static final Random random = new Random();

    public WarShips() {
    }

    public boolean[][] getWarShipPositions() {
        return warShipPositions;
    }

    public void setWarShipPositions(boolean[][] warShipPositions) {
        this.warShipPositions = warShipPositions;
    }

    public void assignWarShipPositions() {
        boolean[][] warShipsPositions = generateNewMap(7);
        setWarShipPositions(warShipsPositions);
    }

    public boolean[][] generateNewMap(int size) {
        boolean[][] map = new boolean[size][size];

        putShipOnMap(3, map);
        putShipOnMap(3, map);

        putShipOnMap(2, map);
        putShipOnMap(2, map);
        putShipOnMap(2, map);

        putShipOnMap(1, map);
        putShipOnMap(1, map);
        putShipOnMap(1, map);

        return map;
    }


    private void putShipOnMap(int shipLength, boolean[][] map) {
        boolean shipPlaced = false;
        while (!shipPlaced) {
            int xCoordinates = generateNumber();
            int yCoordinates = generateNumber();
            int direction = random.nextInt(2); // 1 right, 0 -> down

            boolean canPutShipOnMap = true;

            canPutShipOnMap = checkIfFitOnMap(direction, xCoordinates, yCoordinates, shipLength);

            if (canPutShipOnMap) {
                canPutShipOnMap = checkForCollisions(direction, xCoordinates, yCoordinates, shipLength, map);
            }

            if (canPutShipOnMap) {
                shipPlaced = placeShipOnMap(direction, xCoordinates, yCoordinates, shipLength, map);
            }
        }
    }

    private int generateNumber() {
        return random.nextInt(7);
    }

    private boolean checkIfFitOnMap(int direction, int xCoordinates, int yCoordinates, int shipLength) {
        if (direction == 0 && xCoordinates + shipLength > 6) {
            return false;
        }
        if (direction == 1 && yCoordinates + shipLength > 6) {
            return false;
        }
        return true;
    }

    private boolean checkForCollisions(int direction, int xCoordinates, int yCoordinates, int shipLength, boolean[][] map) {
        if (direction == 0) {
            for (int i = xCoordinates; i < xCoordinates + shipLength; i++) {
                if (i > 0 && map[i - 1][yCoordinates]) {
                    return false;
                }
                if (i < 6 && map[i + 1][yCoordinates]) {
                    return false;
                }
                if (yCoordinates > 0 && map[i][yCoordinates - 1]) {
                    return false;
                }
                if (yCoordinates < 6 && map[i][yCoordinates + 1]) {
                    return false;
                }
            }
        } else if (direction == 1) {
            for (int i = yCoordinates; i < yCoordinates + shipLength; i++) {
                if (i > 0 && map[xCoordinates][i - 1]) {
                    return false;
                }
                if (i < 6 && map[xCoordinates][i + 1]) {
                    return false;
                }
                if (xCoordinates > 0 && map[xCoordinates - 1][i]) {
                    return false;
                }
                if (xCoordinates < 6 && map[xCoordinates + 1][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean placeShipOnMap(int direction, int xCoordinates, int yCoordinates, int shipLength, boolean[][] map) {
        if (direction == 0) {
            for (int i = xCoordinates; i < xCoordinates + shipLength; i++) {
                map[i][yCoordinates] = true;
            }
        } else if (direction == 1) {
            for (int i = yCoordinates; i < yCoordinates + shipLength; i++) {
                map[xCoordinates][i] = true;
            }
        }
        return true;
    }

    public void printWarShipsPositions(boolean[][] warShipsPositions) {
        for (int i = 0; i < warShipsPositions.length; i++) {
            for (int j = 0; j < warShipsPositions[i].length; j++) {
                System.out.print(warShipsPositions[i][j] ? "1 " : "0 ");
            }
            System.out.println(); // Move to the next line after each row
        }
    }
}
