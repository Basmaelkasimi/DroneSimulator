package com.drone.simulator;

import java.util.List;

public class SimulatorBasic {

    private Map map;
    private List<Drone> drones;

    public void initializeSimulation() {
        FileReader fileReader = new FileReader();

        fileReader.readConfiguration("drones.txt");

        int width = fileReader.mapWidth;
        int height = fileReader.mapHeight;

        drones = fileReader.drones;

        map = new Map(width, height);

        map.addObstacles();
    }
    public void executeSimulation() {

        System.out.println("Résultats du Simulateur Basique");
        System.out.println("================================");
        System.out.println("Initialisation de la carte ("
                + map.getWidth() + "x" + map.getHeight() + ")...");

        for (int i = 0; i < drones.size(); i++) {

            Drone drone = drones.get(i);

            System.out.println("Drone " + i
                    + " : Position initiale : "
                    + drone.getPosition()
                    + " | Commandes : "
                    + drone.getCommands());

            System.out.println("===========================================");

            for (char command : drone.getCommands().toCharArray()) {

                drone.move(command, map);

                System.out.println("Position du drone "
                        + i + " : "
                        + drone.getPosition());
            }
        }
    }

    public void displayResults() {

        System.out.println("Positions finales des drones:");

        for (int i = 0; i < drones.size(); i++) {
            System.out.println("Drone " + i + " : "
                    + drones.get(i).getPosition());
        }
    }

    public static void main(String[] args) {
        SimulatorBasic simulator = new SimulatorBasic();

        simulator.initializeSimulation();
        simulator.executeSimulation();
        simulator.displayResults();
    }
}