package com.drone.simulator.concurrent;

import java.util.List;

public class SimulatorMap {
    private static final char OBSTACLE = '#';
    private static final char EMPTY = '.';

    private final int width;
    private final int height;
    private final char[][] grid;

    public SimulatorMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new char[height][width];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = EMPTY;
            }
        }
    }

    public void addObstacles() {
        int numberOfObstacles = (width * height) / 10;

        for (int i = 0; i < numberOfObstacles; i++) {
            int rx = (int) (Math.random() * width);
            int ry = (int) (Math.random() * height);
            grid[ry][rx] = OBSTACLE;
        }
    }

    public boolean isBlocked(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }

        return grid[y][x] == OBSTACLE;
    }

    public int wrapCoordinate(int coordinate, int max) {
        return ((coordinate % max) + max) % max;
    }

    public void displayMap(List<Drone> drones) {
        char[][] displayGrid = new char[height][width];

        for (int y = 0; y < height; y++) {
            System.arraycopy(grid[y], 0, displayGrid[y], 0, width);
        }

        synchronized (drones) {
            for (Drone drone : drones) {
                displayGrid[drone.getY()][drone.getX()] = drone.getDisplaySymbol();
            }
        }

        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                System.out.print(displayGrid[y][x] + " ");
            }
            System.out.println();
        }

        System.out.println("================================");
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
