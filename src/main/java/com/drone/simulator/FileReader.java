package com.drone.simulator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader {

    public List<Drone> drones = new ArrayList<>();
    public int mapWidth;
    public int mapHeight;

    public void readConfiguration(String filename) {
        int[] dimensions = parseMapDimensions(filename);

        mapWidth = dimensions[0];
        mapHeight = dimensions[1];

        drones = parseDroneData(filename);
    }

    public int[] parseMapDimensions(String filename) {
        int[] dimensions = new int[2];

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            if (scanner.hasNextInt()) {
                dimensions[0] = scanner.nextInt();
            }

            if (scanner.hasNextInt()) {
                dimensions[1] = scanner.nextInt();
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.err.println("Erreur : Fichier non trouvé " + filename);
        }

        return dimensions;
    }

    public List<Drone> parseDroneData(String filename) {
        List<Drone> droneList = new ArrayList<>();

        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            int width = scanner.nextInt();
            int height = scanner.nextInt();

            int numberOfDrones = scanner.nextInt();
            scanner.nextLine();

            for (int i = 0; i < numberOfDrones; i++) {
                if (!scanner.hasNextLine()) {
                    break;
                }

                String line = scanner.nextLine();

                if (line.trim().isEmpty()) {
                    i--;
                    continue;
                }

                String[] parts = line.split(" ");

                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                char orientation = parts[2].charAt(0);
                String commands = parts[3];

                Drone drone = new Drone(x, y, orientation, commands);
                droneList.add(drone);
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            System.err.println("Erreur : Fichier non trouvé " + filename);
        }

        return droneList;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public List<Drone> getDrones() {
        return drones;
    }
}