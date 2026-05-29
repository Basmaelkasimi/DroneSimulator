package com.drone.simulator.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DroneWorker extends Thread {
    private static final long MOVEMENT_DELAY_MS = 300;

    private final Drone drone;
    private final SimulatorMap map;
    private final ReentrantLock displayLock;
    private final Condition displayCondition;
    private final CountDownLatch finishedDrones;

    public DroneWorker(Drone drone,
                       SimulatorMap map,
                       ReentrantLock displayLock,
                       Condition displayCondition,
                       CountDownLatch finishedDrones) {
        this.drone = drone;
        this.map = map;
        this.displayLock = displayLock;
        this.displayCondition = displayCondition;
        this.finishedDrones = finishedDrones;
    }

    @Override
    public void run() {
        try {
            executeMovements();
        } finally {
            finishedDrones.countDown();
            synchronizeWithDisplay();
        }
    }

    private void executeMovements() {
        for (char command : drone.getCommands().toCharArray()) {
            drone.executeCommand(command, map);
            synchronizeWithDisplay();
            pauseBetweenMovements();
        }
    }

    private void synchronizeWithDisplay() {
        displayLock.lock();
        try {
            displayCondition.signalAll();
        } finally {
            displayLock.unlock();
        }
    }

    private void pauseBetweenMovements() {
        try {
            Thread.sleep(MOVEMENT_DELAY_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
