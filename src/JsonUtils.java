import java.io.*;
import java.util.*;

public class JsonUtils {

    public static List<Map<String, String>> load(String filePath) {
        List<Map<String, String>> result = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder json = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
            reader.close();

            String content = json.toString().trim();
            if (content.length() < 2) return result;

            content = content.substring(1, content.length() - 1);

            if (content.trim().isEmpty()) return result;

            String[] objects = content.split("}\\s*,\\s*\\{");
            for (String obj : objects) {
                obj = obj.replace("{", "").replace("}", "").trim();
                Map<String, String> map = new HashMap<>();

                for (String field : obj.split(",")) {
                    String[] kv = field.split(":");
                    if (kv.length == 2) {
                        map.put(kv[0].replace("\"", "").trim(),
                                kv[1].replace("\"", "").trim());
                    }
                }
                result.add(map);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void save(List<Map<String, String>> objects, String filePath) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < objects.size(); i++) {
            Map<String, String> obj = objects.get(i);

            json.append("{");
            int j = 0;
            for (Map.Entry<String, String> entry : obj.entrySet()) {
                json.append("\"").append(entry.getKey()).append("\":\"")
                        .append(entry.getValue()).append("\"");
                if (j < obj.size() - 1)
                    json.append(",");
                j++;
            }
            json.append("}");
            if (i < objects.size() - 1)
                json.append(",");
        }
        json.append("]");

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.write(json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
