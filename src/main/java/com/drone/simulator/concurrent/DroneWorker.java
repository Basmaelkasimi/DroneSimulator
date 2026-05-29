package com.drone.simulator.concurrent;

import com.drone.simulator.Drone;
import com.drone.simulator.Map;

public class DroneWorker extends Thread {

    private Drone drone;
    private Map map;
    private DisplayWorker displayWorker;

    public DroneWorker(Drone drone, Map map, DisplayWorker displayWorker) {
        this.drone = drone;
        this.map = map;
        this.displayWorker = displayWorker;
    }

    public void executeMovements() {
        for (char command : drone.getCommands().toCharArray()) {
            drone.move(command, map);
            synchronizeWithDisplay();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void synchronizeWithDisplay() {
        displayWorker.displayMap();
    }

    @Override
    public void run() {
        executeMovements();
    }
}