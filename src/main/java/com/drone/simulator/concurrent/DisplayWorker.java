package com.drone.simulator.concurrent;

import com.drone.simulator.Drone;
import com.drone.simulator.Map;

import java.util.List;

public class DisplayWorker extends Thread {

    private Map map;
    private List<Drone> drones;
    private boolean running = true;

    public DisplayWorker(Map map, List<Drone> drones) {
        this.map = map;
        this.drones = drones;
    }

    public synchronized void displayMap() {
        clearScreen();
        map.displayMap(drones);
    }

    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void synchronizeWithDrones() {
        displayMap();
    }

    public void stopDisplay() {
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            synchronizeWithDrones();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}