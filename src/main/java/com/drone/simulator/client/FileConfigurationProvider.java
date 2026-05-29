package com.drone.simulator.client;

import com.drone.simulator.Drone;
import com.drone.simulator.FileReader;

import java.util.List;

public class FileConfigurationProvider implements ConfigurationProvider {

    private int mapWidth;
    private int mapHeight;
    private List<Drone> drones;

    public FileConfigurationProvider(String filename) {
        readFromFile(filename);
    }

    public void readFromFile(String filename) {
        FileReader fileReader = new FileReader();

        fileReader.readConfiguration(filename);

        mapWidth = fileReader.getMapWidth();
        mapHeight = fileReader.getMapHeight();
        drones = fileReader.getDrones();

        System.out.println("Client - carte : " + mapWidth + " x " + mapHeight);
        System.out.println("Client - drones lus : " + drones.size());
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

    public String getDroneCommands(Drone drone) {
        return drone.getCommands();
    }
}