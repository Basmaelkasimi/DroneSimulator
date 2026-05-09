package com.drone.simulator;
public class TestP1 {
    public static void main(String[] args) {
        System.out.println("Le dossier actuel est : " + System.getProperty("user.dir"));
        FileReader fr = new FileReader();
        fr.readConfiguration("drones.txt");
        Map map = new Map(fr.mapWidth, fr.mapHeight);
        map.addObstacles();

        System.out.println("Carte chargée : " + map.getWidth() + "x" + map.getHeight());
        map.displayMap(fr.drones);
    }
}