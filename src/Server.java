import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import org.json.*;

public class Server {
    private final int port;
    private final Map<String, String> localCache;

    public Server(int port, String cacheFile) throws IOException {
        this.port = port;
        localCache = CacheManager.loadCache(cacheFile);
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server running on port " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                CacheManager.saveCache(localCache, "src/cache_server.json");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(new ServerThread(clientSocket, localCache)).start();
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(9000, "src/cache_server.json");
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
