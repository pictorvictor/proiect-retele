import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CacheManager {

    public static Map<String, String> loadCache(String cacheFile) throws IOException {
        Map<String, String> cache = new HashMap<>();
        File file = new File(cacheFile);
        if (file.exists()) {
            String content = new String(Files.readAllBytes(Paths.get(cacheFile)));
            JSONObject jsonCache = new JSONObject(content);
            for (String key : jsonCache.keySet()) {
                cache.put(key, jsonCache.getString(key));
            }
        }
        return cache;
    }

    public static void saveCache(Map<String, String> localCache, String pathname) throws IOException {
        JSONObject jsonCache = new JSONObject(localCache);
        Files.write(Paths.get(pathname), jsonCache.toString(4).getBytes());
    }
}
