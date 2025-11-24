import java.util.*;
import java.util.stream.Collectors;

public class TaskService {

    private TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public boolean markAsDone(int id) {
        return repository.update(id, "status", "DONE");
    }

    public List<Task> search(String query) {
        String lowerQuery = query.toLowerCase();
        return repository.listAll().stream()
                .filter(t -> t.getTitle().toLowerCase().contains(lowerQuery)
                        || t.getDescription().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public List<Task> listSortedByStatus() {
        List<Task> allTasks = repository.listAll();

        List<Task> newTasks = new ArrayList<>();
        List<Task> inProgressTasks = new ArrayList<>();
        List<Task> doneTasks = new ArrayList<>();

        for (Task t : allTasks) {
            switch (t.getStatus()) {
                case NEW -> newTasks.add(t);
                case IN_PROGRESS -> inProgressTasks.add(t);
                case DONE -> doneTasks.add(t);
            }
        }

        List<Task> sorted = new ArrayList<>(allTasks.size());
        sorted.addAll(newTasks);
        sorted.addAll(inProgressTasks);
        sorted.addAll(doneTasks);

        return sorted;
    }

    public List<Task> listAll() {
        return repository.listAll();
    }

    public void add(String title, String description) {
        repository.add(title, description);
    }

    public void delete(int id) {
        repository.delete(id);
    }

    public void update(int id, String field, String value) {
        repository.update(id, field, value);
    }

    public Task getById(int id) {
        return repository.getById(id);
    }

}
