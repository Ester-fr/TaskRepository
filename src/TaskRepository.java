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

    public void add(Task task) {
        tasks.add(task);
        save();
    }

    public boolean update(int id, String field, String newValue) {
        for (Task t : tasks) {
            if (t.getId() == id) {
                switch (field.toLowerCase()) {
                    case "title":
                        t.setTitle(newValue);
                        break;
                    case "description":
                        t.setDescription(newValue);
                        break;
                    case "status":
                        t.setStatus(Status.valueOf(newValue.toUpperCase()));
                        break;
                    default:
                        return false;
                }
                save();
                return true;
            }
        }
        return false;
    }


    public boolean delete(int id) {
        Iterator<Task> it = tasks.iterator();
        while (it.hasNext()) {
            if (it.next().getId() == id) {
                it.remove();
                save();
                return true;
            }
        }
        return false;
    }

    public Task getById(int id) {
        for (Task t : tasks) {
            if (t.getId() == id)
                return t;
        }
        return null;
    }

    public List<Task> listAll() {
        return new ArrayList<>(tasks);
    }
}
