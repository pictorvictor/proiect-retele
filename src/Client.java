import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import org.json.*;

public class Client {
    private final String serverAddress;
    private final int serverPort;
    private final Map<String, String> localCache;

    public Client(String serverAddress, int serverPort, String cacheFile) throws IOException {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        localCache = CacheManager.loadCache(cacheFile);
    }

    public String query(String name) {
        if (localCache.containsKey(name)) {
            return localCache.get(name);
        }
        try {
            Socket socket = new Socket(serverAddress, serverPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(name);
            String response = in.readLine();
            socket.close();
            localCache.put(name, response);
            CacheManager.saveCache(localCache, "src/cache_client.json");
            return response;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client("127.0.0.1", 9000, "src/cache_client.json");
            String name1 = "domain1.com";
            System.out.println("Querying " + name1 + ": " + client.query(name1));
            String name2 = "domain2.com";
            System.out.println("Querying " + name2 + ": " + client.query(name2));
            String name3 = "google.com";
            System.out.println("Querying " + name3 + ": " + client.query(name3));
            String name4 = "yahoo.com";
            System.out.println("Querying " + name4 + ": " + client.query(name4));
            String name5 = "notfounddomain1234123.com";
            System.out.println("Querying " + name5 + ": " + client.query(name5));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
