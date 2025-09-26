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

        // Удаляем задачу, если она уже есть в истории (чтобы избежать дубликатов)
        history.remove(task);

        // Добавляем задачу в начало списка
        history.add(0, task);

        // Удаляем самые старые задачи, если превышен лимит
        while (history.size() > MAX_HISTORY_SIZE) {
            history.remove(history.size() - 1);
        }
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history); // возвращаем копию
    }
}