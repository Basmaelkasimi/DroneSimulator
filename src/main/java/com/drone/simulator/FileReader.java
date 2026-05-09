package com.drone.simulator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    public int[] parseMapDimensions(String filename) {

        int[] dimensions = new int[2];

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new java.io.FileReader(filename)
                    );

            String firstLine = reader.readLine();

            String[] parts = firstLine.split(" ");

            dimensions[0] = Integer.parseInt(parts[0]);
            dimensions[1] = Integer.parseInt(parts[1]);

            reader.close();

        } catch (IOException e) {

            System.out.println("Erreur lecture dimensions : "
                    + e.getMessage());
        }

        return dimensions;
    }

    public List<Drone> parseDroneData(String filename) {

        List<Drone> drones =
                new ArrayList<>();

        try {

            BufferedReader reader =
                    new BufferedReader(
                            new java.io.FileReader(filename)
                    );

            // sauter première ligne (dimensions)
            reader.readLine();

            // lire nombre drones
            int numberOfDrones =
                    Integer.parseInt(reader.readLine());

            for (int i = 0;
                 i < numberOfDrones;
                 i++) {

                String line =
                        reader.readLine();

                String[] parts =
                        line.split(" ");

                int x =
                        Integer.parseInt(parts[0]);

                int y =
                        Integer.parseInt(parts[1]);

                char orientation =
                        parts[2].charAt(0);

                String commands =
                        parts[3];

                Drone drone =
                        new Drone(
                                x,
                                y,
                                orientation,
                                commands
                        );

                drones.add(drone);
            }

            reader.close();

        } catch (IOException e) {

            System.out.println("Erreur lecture drones : "
                    + e.getMessage());
        }

        return drones;
    }
}