package com.drone.simulator;
import java.util.List;

public class Map {
    private int width;
    private int height;
    private char[][] grid;
    private static final char OBSTACLE = '#';
    private static final char EMPTY = '.';

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                grid[i][j] = EMPTY;
            }
        }
    }

    // Génère des obstacles aléatoires (environ 10% de la carte)
    public void addObstacles() {
        int numberOfObstacles = (width * height) / 10;
        for (int i = 0; i < numberOfObstacles; i++) {
            int rx = (int) (Math.random() * width);
            int ry = (int) (Math.random() * height);
            grid[ry][rx] = OBSTACLE;
        }
    }

    public boolean isBlocked(int x, int y) {
        // Sécurité pour ne pas sortir du tableau avant le wrapping
        if (x < 0 || x >= width || y < 0 || y >= height) return false;
        return grid[y][x] == OBSTACLE;
    }

    // Formule de wrapping (Page 14 du PDF)
    public int wrapCoordinate(int coord, int max) {
        return ((coord % max) + max) % max;
    }

    // Affiche la carte avec les drones
    public void displayMap(List<Drone> drones) {
        for (int y = height - 1; y >= 0; y--) { // Inversé pour que y=0 soit en bas
            for (int x = 0; x < width; x++) {
                char toPrint = grid[y][x];

                // Vérifier si un drone est sur cette case
                for (Drone d : drones) {
                    if (d.getX() == x && d.getY() == y) {
                        toPrint = getDroneSymbol(d.getOrientation());
                    }
                }
                System.out.print(toPrint + " ");
            }
            System.out.println();
        }
        System.out.println("================================");
    }

    private char getDroneSymbol(char orientation) {
        switch (orientation) {
            case 'N': return '^';
            case 'S': return 'v';
            case 'E': return '>';
            case 'O': return '<';
            default: return 'D';
        }
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
}