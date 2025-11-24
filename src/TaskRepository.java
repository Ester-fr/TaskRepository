import java.util.*;

public class TaskRepository {

    private List<Task> tasks;
    private String filePath;


    public TaskRepository(String filePath) {
        this.filePath = filePath;
        this.tasks = new ArrayList<>();
        load();
    }

    private void load() {
        List<Map<String, String>> rawList = JsonUtils.load(filePath);

        for (Map<String, String> map : rawList) {
            int id = Integer.parseInt(map.get("id"));
            String title = map.get("title");
            String description = map.get("description");
            Status status = Status.valueOf(map.get("status"));

            Task t = new Task(id, title, description, status);

            tasks.add(t);
        }
    }

    private void save() {
        List<Map<String, String>> rawList = new ArrayList<>();

        for (Task t : tasks) {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(t.getId()));
            map.put("title", t.getTitle());
            map.put("description", t.getDescription());
            map.put("status", t.getStatus().name());
            rawList.add(map);
        }

        JsonUtils.save(rawList, filePath);
    }

}
