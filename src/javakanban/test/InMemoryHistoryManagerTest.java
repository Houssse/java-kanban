package javakanban.test;

import javakanban.data.Status;
import javakanban.data.Task;
import javakanban.manager.InMemoryHistoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();

        task1 = new Task("Задача 1", "Описание 1", Status.NEW);
        task1.setId(1);

        task2 = new Task("Задача 2", "Описание 2", Status.IN_PROGRESS);
        task2.setId(2);

        task3 = new Task("Задача 3", "Описание 3", Status.DONE);
        task3.setId(3);
    }

    @Test
    void addShouldAddTaskToBeginningOfHistory() {
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0), "Последняя добавленная задача должна быть первой");
        assertEquals(task1, history.get(1), "Первая добавленная задача должна быть второй");
    }

    @Test
    void addShouldNotAddNullTask() {
        historyManager.add(null);

        assertTrue(historyManager.getHistory().isEmpty(),
                "История должна остаться пустой при добавлении null");
    }

    @Test
    void addShouldMoveExistingTaskToBeginning() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();
        assertEquals(List.of(task3, task2, task1), history);

        historyManager.add(task2);

        history = historyManager.getHistory();
        assertEquals(3, history.size(), "Размер истории не должен измениться");
        assertEquals(task2, history.get(0), "Задача должна переместиться в начало");
        assertEquals(task3, history.get(1));
        assertEquals(task1, history.get(2));
    }

    @Test
    void historyShouldNotExceedMaxSize() {
        for (int i = 1; i <= 15; i++) {
            Task task = new Task("Задача " + i, "Описание", Status.NEW);
            task.setId(i);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();
        assertEquals(10, history.size(), "История не должна превышать максимальный размер");

        assertEquals(15, history.get(0).getId(), "Первой должна быть задача 15");
        assertEquals(6, history.get(9).getId(), "Последней должна быть задача 6");
    }

    @Test
    void getHistoryShouldReturnCopyOfInternalList() {
        historyManager.add(task1);

        List<Task> firstCall = historyManager.getHistory();
        List<Task> secondCall = historyManager.getHistory();

        assertNotSame(firstCall, secondCall, "Должна возвращаться новая копия списка");

        firstCall.clear();
        assertEquals(1, historyManager.getHistory().size(),
                "Внутренний список не должен измениться при модификации копии");
    }

    @Test
    void emptyHistoryShouldReturnEmptyList() {
        assertTrue(historyManager.getHistory().isEmpty(),
                "Новая история должна быть пустой");
    }

    @Test
    void tasksWithSameIdShouldBeTreatedAsEqual() {
        Task original = new Task("Оригинал", "Описание", Status.NEW);
        original.setId(1);

        Task modified = new Task("Измененная", "Новое описание", Status.DONE);
        modified.setId(1);

        historyManager.add(original);
        historyManager.add(modified);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size(), "Задачи с одинаковым ID должны считаться одной задачей");
        assertEquals("Измененная", history.get(0).getTitle(),
                "В истории должна остаться последняя версия");
    }
}