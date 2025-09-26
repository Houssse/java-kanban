package javakanban.manager;

import javakanban.data.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private List<Task> history = new ArrayList<>();
    private static final int MAX_HISTORY_SIZE = 10;

    @Override
    public void add(Task task) {
        if (task == null) return;

        history.add(task);

        while (history.size() > MAX_HISTORY_SIZE) {
            history.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}