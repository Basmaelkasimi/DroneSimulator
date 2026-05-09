package com.drone.simulator;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileReader{

    public List<Drone> drones = new ArrayList<>();
    public int mapWidth;
    public int mapHeight;

    public void readConfiguration(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);

            if (scanner.hasNextLine()) {
                // Ligne 1 : Taille de la carte (ex: 20 20)
                mapWidth = scanner.nextInt();
                mapHeight = scanner.nextInt();
                scanner.nextLine(); // Consommer le retour ligne
            }

            // Lignes suivantes : Drones (ex: 0 5 N LMLMLM)
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(" ");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                char orientation = parts[2].charAt(0);
                String commands = parts[3];

                drones.add(new Drone(x, y, orientation, commands));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Erreur : Fichier non trouvé " + filename);
        }
    }
}