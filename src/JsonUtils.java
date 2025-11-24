import java.io.*;
import java.util.*;

public class JsonUtils {

    public static <T> List<T> loadFromFile(String filePath, Class<T> clazz) {
        List<T> objects = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }
            reader.close();

            String content = jsonContent.toString();
            content = content.substring(1, content.length() - 1);
            String[] taskStrings = content.split("},\\{");
            for (String taskString : taskStrings) {
                taskString = taskString.replace("{", "").replace("}", "").replace("\"", "");
                String[] fields = taskString.split(",");
                int id = Integer.parseInt(fields[0].split(":")[1].trim());
                String title = fields[1].split(":")[1].trim();
                String description = fields[2].split(":")[1].trim();
                Status status = Status.valueOf(fields[3].split(":")[1].trim());
                objects.add(clazz.cast(new Task(id, title, description)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return objects;
    }

    public static <T> void saveToFile(List<T> objects, String filePath) {
        StringBuilder jsonContent = new StringBuilder("[");
        for (int i = 0; i < objects.size(); i++) {
            T object = objects.get(i);
            jsonContent.append("{")
                    .append("\"id\":").append(((Task) object).getId()).append(",")
                    .append("\"title\":\"").append(((Task) object).getTitle()).append("\",")
                    .append("\"description\":\"").append(((Task) object).getDescription()).append("\",")
                    .append("\"status\":\"").append(((Task) object).getStatus()).append("\"")
                    .append("}");
            if (i < objects.size() - 1) {
                jsonContent.append(",");
            }
        }
        jsonContent.append("]");

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.write(jsonContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
