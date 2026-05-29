package com.drone.simulator.concurrent;

public class Drone {
    private final int id;
    private int x;
    private int y;
    private char orientation;
    private final String commands;

    public Drone(int id, int x, int y, char orientation, String commands) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.commands = commands;
    }

    public synchronized void executeCommand(char command, SimulatorMap map) {
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

    private void turnLeft() {
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
            default:
                break;
        }
    }

    private void turnRight() {
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
            default:
                break;
        }
    }

    private void moveForward(SimulatorMap map) {
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
            default:
                break;
        }

        updatePosition(newX, newY, map);
    }

    private void moveBackward(SimulatorMap map) {
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
            default:
                break;
        }

        updatePosition(newX, newY, map);
    }

    private void updatePosition(int newX, int newY, SimulatorMap map) {
        int wrappedX = map.wrapCoordinate(newX, map.getWidth());
        int wrappedY = map.wrapCoordinate(newY, map.getHeight());

        if (!map.isBlocked(wrappedX, wrappedY)) {
            x = wrappedX;
            y = wrappedY;
        }
    }

    public synchronized String getPosition() {
        return x + " " + y + " " + orientation;
    }

    public synchronized char getDisplaySymbol() {
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

    public int getId() {
        return id;
    }

    public synchronized int getX() {
        return x;
    }

    public synchronized int getY() {
        return y;
    }

    public synchronized char getOrientation() {
        return orientation;
    }

    public String getCommands() {
        return commands;
    }
}