package brc;

import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Starter {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Bus routes data file path is expected as first argument");
            System.exit(-1);
        }

        RouteRegistry registry = new RouteRegistry();
        loadRegistry(registry, new File(args[0]));

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8088), 0);
        httpServer.createContext("/api/direct", new RouteRegistryHandler(registry));
        httpServer.start();
    }

    public static void loadRegistry(RouteRegistry registry, File busRoutesDataFile) {
        try (Scanner sc = new Scanner(new BufferedInputStream(new FileInputStream(busRoutesDataFile)))) {
            int n = Integer.parseInt(sc.nextLine());
            for (int i = 0; i < n; i++) {
                String[] routeLine = sc.nextLine().split(" ");
                Integer routeId = Integer.valueOf(routeLine[0]);
                List<Integer> stations = new ArrayList<>();
                for (int s = 1; s < routeLine.length; s++) {
                    stations.add(Integer.valueOf(routeLine[s]));
                }
                registry.addRoute(routeId, stations);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Bus routes data file does not exist: " + busRoutesDataFile.getAbsolutePath());
            System.exit(-1);
        }
    }
}
