import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        TaskRepository repository = new TaskRepository("tasks.json");

        TaskService service = new TaskService(repository);

        new ToDoUi(service);
    }
}
