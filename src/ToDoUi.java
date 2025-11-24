import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ToDoUi {

    private TaskService service;

    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;

    public ToDoUi(TaskService service) {
        this.service = service;
        initialize();
        loadTasks();
    }

    private void initialize() {
        frame = new JFrame("Todo List");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Description", "Status"}, 0);
        table = new JTable(tableModel);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton addBtn = new JButton("Add Task");
        JButton deleteBtn = new JButton("Delete Task");
        JButton doneBtn = new JButton("Mark DONE");
        JButton searchBtn = new JButton("Search");

        panel.add(addBtn);
        panel.add(deleteBtn);
        panel.add(doneBtn);
        panel.add(searchBtn);

        frame.add(panel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addTask());
        deleteBtn.addActionListener(e -> deleteTask());
        doneBtn.addActionListener(e -> markDone());
        searchBtn.addActionListener(e -> searchTasks());

        frame.setVisible(true);
    }

    private void loadTasks() {
        tableModel.setRowCount(0);
        List<Task> tasks = service.listAll();
        for (Task t : tasks) {
            tableModel.addRow(new Object[]{t.getId(), t.getTitle(), t.getDescription(), t.getStatus()});
        }
    }

    private void addTask() {
        String title = JOptionPane.showInputDialog(frame, "Enter Title:");
        if (title == null || title.isEmpty()) return;
        String desc = JOptionPane.showInputDialog(frame, "Enter Description:");
        if (desc == null) return;

        service.add(title, desc);
        loadTasks();
    }

    private void deleteTask() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        service.delete(id);
        loadTasks();
    }

    private void markDone() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        service.markAsDone(id);
        loadTasks();
    }

    private void searchTasks() {
        String query = JOptionPane.showInputDialog(frame, "Enter search query:");
        if (query == null) return;
        List<Task> results = service.search(query);
        tableModel.setRowCount(0);
        for (Task t : results) {
            tableModel.addRow(new Object[]{t.getId(), t.getTitle(), t.getDescription(), t.getStatus()});
        }
    }
}
