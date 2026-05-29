package com.drone.simulator.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DroneServer {

    private static final int PORT = 5000;

    public void startServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Serveur démarré sur le port " + port);
        listenForConnections(serverSocket);
    }

    public void listenForConnections(ServerSocket serverSocket) throws IOException {
        while (true) {
            System.out.println("En attente de connexion client...");
            Socket clientSocket = serverSocket.accept();

            System.out.println("Client connecté : " + clientSocket.getInetAddress());

            ServerConnectionHandler handler =
                    new ServerConnectionHandler(clientSocket);

            new Thread(handler).start();
        }
    }

    public static void main(String[] args) throws IOException {
        DroneServer server = new DroneServer();
        server.startServer(PORT);
    }
}