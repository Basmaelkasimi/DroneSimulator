package com.drone.simulator.client;

import com.drone.simulator.Drone;
import java.util.List;

public interface ConfigurationProvider {

    int getMapWidth();

    int getMapHeight();

    List<Drone> getDrones();

    String getDroneCommands(Drone drone);
}