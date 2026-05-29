package com.drone.simulator.concurrent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class SimulatorConcurrent {
    private final List<Drone> drones = Collections.synchronizedList(new ArrayList<>());
    private final ReentrantLock displayLock = new ReentrantLock();
    private final Condition displayCondition = displayLock.newCondition();
    private final List<DroneWorker> droneWorkers = new ArrayList<>();

    private SimulatorMap map;
    private CountDownLatch finishedDrones;
    private DisplayWorker displayWorker;

    public void initializeThreads() {
        readConfiguration("drones.txt");
        finishedDrones = new CountDownLatch(drones.size());
        displayWorker = new DisplayWorker(map, drones, displayLock, displayCondition);

        synchronized (drones) {
            for (Drone drone : drones) {
                droneWorkers.add(new DroneWorker(
                        drone,
                        map,
                        displayLock,
                        displayCondition,
                        finishedDrones));
            }
        }
    }

    public void startSimulation() {
        System.out.println("Resultats du Simulateur Concurrent");
        System.out.println("==================================");
        System.out.println("Carte : " + map.getWidth() + "x" + map.getHeight());
        System.out.println("Nombre de drones : " + drones.size());

        displayWorker.start();

        for (DroneWorker worker : droneWorkers) {
            worker.start();
        }
    }

    public void waitForCompletion() {
        try {
            finishedDrones.await();
            displayWorker.stopDisplay();
            displayWorker.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void displayResults() {
        System.out.println();
        System.out.println("Positions finales des drones:");

        synchronized (drones) {
            for (Drone drone : drones) {
                System.out.println("Drone " + drone.getId() + " : " + drone.getPosition());
            }
        }
    }

    private void readConfiguration(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            if (!scanner.hasNextInt()) {
                throw new IllegalArgumentException("Fichier de configuration invalide.");
            }

            int width = scanner.nextInt();
            int height = scanner.nextInt();
            scanner.nextLine();

            map = new SimulatorMap(width, height);
            map.addObstacles();

            int droneId = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+");
                if (parts.length < 4) {
                    throw new IllegalArgumentException("Ligne drone invalide : " + line);
                }

                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                char orientation = parts[2].charAt(0);
                String commands = parts[3];

                drones.add(new Drone(droneId, x, y, orientation, commands));
                droneId++;
            }
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Fichier non trouve : " + filename, e);
        }
    }

    public static void main(String[] args) {
        SimulatorConcurrent simulator = new SimulatorConcurrent();
        simulator.initializeThreads();
        simulator.startSimulation();
        simulator.waitForCompletion();
        simulator.displayResults();
    }
}
