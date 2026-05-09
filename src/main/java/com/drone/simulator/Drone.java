package main.java.com.drone.simulator;

public class Drone {
    private int x, y;
    private char orientation; // N, S, E, O
    private String commands;

    public Drone(int x, int y, char orientation, String commands) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.commands = commands;
    }

    // Getters nécessaires pour la Personne 1
    public int getX() { return x; }
    public int getY() { return y; }
    public char getOrientation() { return orientation; }
    public String getCommands() { return commands; }

    // Ces méthodes seront codées par la Personne 2
    public void executeCommand(char cmd, Map map) { }
}