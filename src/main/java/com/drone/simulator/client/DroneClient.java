package com.drone.simulator.client;

import com.drone.simulator.Drone;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class DroneClient {

    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private ConfigurationProvider config;

    public void connectToServer(String host, int port) throws IOException {
        socket = new Socket(host, port);

        output = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream()), true);

        input = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

        System.out.println("Connecté au serveur");
    }

    public void sendConfiguration() {
        output.println(config.getMapWidth() + " " + config.getMapHeight());

        List<Drone> drones = config.getDrones();
        output.println(drones.size());

        sendDroneData(drones);
    }

    public void sendDroneData(List<Drone> drones) {
        for (Drone drone : drones) {
            String droneData =
                    drone.getX() + " "
                            + drone.getY() + " "
                            + drone.getOrientation() + " "
                            + drone.getCommands();

            output.println(droneData);
        }
    }

    public void receiveResults() throws IOException {
        String status = input.readLine();

        if ("SUCCESS".equals(status)) {
            System.out.println("Simulation réussie !");
            displayResults();
        } else {
            System.out.println("Erreur simulation côté serveur.");
        }
    }

    public void displayResults() throws IOException {
        List<Drone> drones = config.getDrones();

        for (int i = 0; i < drones.size(); i++) {
            String resultLine = input.readLine();

            String[] parts = resultLine.split(" ");

            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            char orientation = parts[2].charAt(0);

            System.out.println("Drone " + i + " : "
                    + x + " " + y + " " + orientation);
        }
    }

    public void closeConnection() throws IOException {
        socket.close();
    }

    public static void main(String[] args) throws IOException {
        DroneClient client = new DroneClient();

        client.config = new FileConfigurationProvider("drones.txt");

        client.connectToServer(HOST, PORT);

        client.sendConfiguration();

        client.receiveResults();

        client.closeConnection();
    }
}