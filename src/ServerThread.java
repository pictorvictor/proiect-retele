import java.io.*;
import java.net.*;
import java.util.*;
import org.json.*;
import java.net.InetAddress;

public class ServerThread implements Runnable {
    private Socket clientSocket;
    private Map<String, String> localCache;

    public ServerThread(Socket clientSocket, Map<String, String> localCache) {
        this.clientSocket = clientSocket;
        this.localCache = localCache;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String request = in.readLine();
            System.out.println("Request: " + request);

            if (localCache.containsKey(request)) {
                out.println(localCache.get(request));
            } else {
                String response = contactResponsibleServer(request);
                if (response != null) {
                    localCache.put(request, response);
                    out.println(response);
                } else {
                    out.println("Not found");
                    localCache.put(request, "Not found");
                }
            }
            clientSocket.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private String contactResponsibleServer(String name) {
        try {
            InetAddress address = InetAddress.getByName(name);
            String response = address.getHostAddress();
            return response;
        } catch (UnknownHostException e) {
//            e.printStackTrace();
            return "Not found";
        }
    }
}
