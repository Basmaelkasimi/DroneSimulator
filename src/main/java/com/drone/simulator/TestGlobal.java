package com.drone.simulator;

public class TestGlobal {

    public static void main(String[] args) {

        System.out.println("===== TEST GLOBAL DRONE SIMULATOR =====");
        System.out.println("Dossier actuel : " + System.getProperty("user.dir"));

        FileReader fileReader = new FileReader();
        fileReader.readConfiguration("drones.txt");

        if (fileReader.mapWidth <= 0 || fileReader.mapHeight <= 0) {
            System.out.println("Erreur : carte non chargée.");
            return;
        }

        if (fileReader.drones == null || fileReader.drones.isEmpty()) {
            System.out.println("Erreur : aucun drone chargé.");
            return;
        }

        Map map = new Map(fileReader.mapWidth, fileReader.mapHeight);
        map.addObstacles();

        System.out.println("Carte chargée : "
                + map.getWidth() + "x" + map.getHeight());

        System.out.println("Nombre de drones : "
                + fileReader.drones.size());

        System.out.println("Carte initiale :");
        map.displayMap(fileReader.drones);

        System.out.println("Début simulation...");
        System.out.println("================================");

        for (int i = 0; i < fileReader.drones.size(); i++) {
            Drone drone = fileReader.drones.get(i);

            System.out.println("Drone " + i);
            System.out.println("Position initiale : " + drone.getPosition());
            System.out.println("Commandes : " + drone.getCommands());

            for (char command : drone.getCommands().toCharArray()) {
                drone.move(command, map);
                System.out.println("Commande " + command
                        + " -> Position : "
                        + drone.getPosition());
            }

            System.out.println("--------------------------------");
        }

        System.out.println("Carte finale :");
        map.displayMap(fileReader.drones);

        System.out.println("Positions finales :");
        for (int i = 0; i < fileReader.drones.size(); i++) {
            System.out.println("Drone " + i + " : "
                    + fileReader.drones.get(i).getPosition());
        }

        System.out.println("===== FIN TEST GLOBAL =====");
    }
}