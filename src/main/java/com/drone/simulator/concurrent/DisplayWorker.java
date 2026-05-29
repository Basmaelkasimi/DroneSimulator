package com.drone.simulator.concurrent;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DisplayWorker extends Thread {
    private final SimulatorMap map;
    private final List<Drone> drones;
    private final ReentrantLock displayLock;
    private final Condition displayCondition;
    private volatile boolean running = true;

    public DisplayWorker(SimulatorMap map,
                         List<Drone> drones,
                         ReentrantLock displayLock,
                         Condition displayCondition) {
        this.map = map;
        this.drones = drones;
        this.displayLock = displayLock;
        this.displayCondition = displayCondition;
    }

    @Override
    public void run() {
        while (running) {
            synchronizeWithDrones();
            displayMap();
        }
    }

    public void stopDisplay() {
        running = false;
        displayLock.lock();
        try {
            displayCondition.signalAll();
        } finally {
            displayLock.unlock();
        }
    }

    private void synchronizeWithDrones() {
        displayLock.lock();
        try {
            displayCondition.await(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            running = false;
        } finally {
            displayLock.unlock();
        }
    }

    private void displayMap() {
        clearScreen();
        map.displayMap(drones);
    }

    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
