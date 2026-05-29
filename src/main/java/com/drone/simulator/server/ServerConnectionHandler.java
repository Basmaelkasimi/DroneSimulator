package com.drone.simulator.server;

import com.drone.simulator.Drone;
import com.drone.simulator.Map;
import com.drone.simulator.concurrent.DroneWorker;
import com.drone.simulator.concurrent.DisplayWorker;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerConnectionHandler implements Runnable {

    private Socket socket;
    private Map map;
    private List<Drone> drones;

    public ServerConnectionHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        handleClientConnection();
    }

    public void handleClientConnection() {
        try (
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));

                PrintWriter output = new PrintWriter(
                        new OutputStreamWriter(socket.getOutputStream()), true)
        ) {

            receiveConfiguration(input);

            if (!validateData()) {
                System.out.println("Validation échouée");
                output.println("ERROR");
                return;
            }

            executeSimulation();

            sendResults(output);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveConfiguration(BufferedReader input) throws IOException {
        String mapDimensions = input.readLine();

        String[] dims = mapDimensions.split(" ");
        int width = Integer.parseInt(dims[0]);
        int height = Integer.parseInt(dims[1]);

        int numDrones = Integer.parseInt(input.readLine());

        System.out.println("Carte reçue : " + width + " x " + height);
        System.out.println("Nombre de drones reçu : " + numDrones);

        drones = new ArrayList<>();

        for (int i = 0; i < numDrones; i++) {
            String droneLine = input.readLine();
            Drone drone = parseClientData(droneLine);
            drones.add(drone);
            System.out.println("Drone reçu : " + drone.getPosition()
                    + " | Commandes : " + drone.getCommands());
        }

        map = new Map(width, height);
        map.addObstacles();
    }

    public Drone parseClientData(String line) {
        String[] parts = line.split(" ");

        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        char orientation = parts[2].charAt(0);
        String commands = parts[3];

        return new Drone(x, y, orientation, commands);
    }

    public boolean validateData() {

        if (map == null) {
            System.out.println("Erreur : map null");
            return false;
        }

        if (drones == null) {
            System.out.println("Erreur : drones null");
            return false;
        }

        if (drones.isEmpty()) {
            System.out.println("Erreur : liste drones vide");
            return false;
        }

        return true;
    }

    public void executeSimulation() {

        List<Thread> workers = new ArrayList<>();

        for (Drone drone : drones) {
            Thread worker = new Thread(() -> {
                for (char command : drone.getCommands().toCharArray()) {
                    drone.move(command, map);

                    synchronized (map) {
                        map.displayMap(drones);
                    }

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });

            workers.add(worker);
            worker.start();
        }

        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    public void sendResults(PrintWriter output) {
        output.println("SUCCESS");

        for (Drone drone : drones) {
            output.println(
                    drone.getX() + " "
                            + drone.getY() + " "
                            + drone.getOrientation()
            );
        }


    }
}