package com.drone.simulator;

public class Drone {

    private int x;
    private int y;
    private char orientation; // N, S, E, O
    private String commands;

    public Drone(int x, int y, char orientation, String commands) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.commands = commands;
    }

    public void move(char command, Map map) {
        switch (command) {
            case 'L':
                turnLeft();
                break;
            case 'R':
                turnRight();
                break;
            case 'M':
                moveForward(map);
                break;
            case 'B':
                moveBackward(map);
                break;
            default:
                System.out.println("Commande invalide : " + command);
        }
    }

    public void turnLeft() {
        switch (orientation) {
            case 'N':
                orientation = 'O';
                break;
            case 'O':
                orientation = 'S';
                break;
            case 'S':
                orientation = 'E';
                break;
            case 'E':
                orientation = 'N';
                break;
        }
    }

    public void turnRight() {
        switch (orientation) {
            case 'N':
                orientation = 'E';
                break;
            case 'E':
                orientation = 'S';
                break;
            case 'S':
                orientation = 'O';
                break;
            case 'O':
                orientation = 'N';
                break;
        }
    }

    public void moveForward(Map map) {
        int newX = x;
        int newY = y;

        switch (orientation) {
            case 'N':
                newY++;
                break;
            case 'S':
                newY--;
                break;
            case 'E':
                newX++;
                break;
            case 'O':
                newX--;
                break;
        }

        newX = wrapCoordinate(newX, map.getWidth());
        newY = wrapCoordinate(newY, map.getHeight());

        if (!map.isBlocked(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    public void moveBackward(Map map) {
        int newX = x;
        int newY = y;

        switch (orientation) {
            case 'N':
                newY--;
                break;
            case 'S':
                newY++;
                break;
            case 'E':
                newX--;
                break;
            case 'O':
                newX++;
                break;
        }

        newX = wrapCoordinate(newX, map.getWidth());
        newY = wrapCoordinate(newY, map.getHeight());

        if (!map.isBlocked(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    private int wrapCoordinate(int coordinate, int max) {
        return ((coordinate % max) + max) % max;
    }

    public String getPosition() {
        return x + " " + y + " " + orientation;
    }

    public char getDisplaySymbol() {
        switch (orientation) {
            case 'N':
                return '^';
            case 'S':
                return 'v';
            case 'E':
                return '>';
            case 'O':
                return '<';
            default:
                return '?';
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public char getOrientation() {
        return orientation;
    }

    public String getCommands() {
        return commands;
    }
}