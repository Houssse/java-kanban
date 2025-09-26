package javakanban.test;

import javakanban.data.Epic;
import javakanban.data.Status;
import javakanban.data.Subtask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EpicTest {

    private Epic epic;
    private Subtask subtask1;
    private Subtask subtask2;

    @BeforeEach
    void setUp() {
        epic = new Epic("Эпик", "Описание эпика");
        epic.setId(1);

        subtask1 = new Subtask("Подзадача 1", "Описание 1", Status.NEW, epic.getId());
        subtask1.setId(2);

        subtask2 = new Subtask("Подзадача 2", "Описание 2", Status.NEW, epic.getId());
        subtask2.setId(3);
    }

    @Test
    void epicShouldNotAddItselfAsSubtask() {
        assertThrows(IllegalArgumentException.class, () -> {
            epic.addSubtaskId(epic.getId());
        }, "Эпик не должен добавляться в самого себя как подзадача");
    }

    @Test
    void epicStatusShouldBeNewWhenNoSubtasks() {
        assertEquals(Status.NEW, epic.getStatus(),
                "Статус эпика без подзадач должен быть NEW");
        assertTrue(epic.getSubtaskIds().isEmpty(),
                "Список подзадач должен быть пустым");
    }

    @Test
    void epicShouldManageSubtaskIdsCorrectly() {
        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask2.getId());

        List<Integer> subtaskIds = epic.getSubtaskIds();
        assertEquals(2, subtaskIds.size(), "Эпик должен содержать 2 подзадачи");
        assertTrue(subtaskIds.contains(subtask1.getId()),
                "Эпик должен содержать подзадачу с id=2");
        assertTrue(subtaskIds.contains(subtask2.getId()),
                "Эпик должен содержать подзадачу с id=3");

        epic.removeSubtaskId(subtask1.getId());
        assertEquals(1, epic.getSubtaskIds().size(),
                "После удаления должна остаться 1 подзадача");
        assertFalse(epic.getSubtaskIds().contains(subtask1.getId()),
                "Подзадача с id=2 должна быть удалена");

        epic.clearSubtasks();
        assertTrue(epic.getSubtaskIds().isEmpty(),
                "После очистки список подзадач должен быть пуст");
    }

    @Test
    void epicShouldInheritEqualsFromTask() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик 2", "Описание 2");

        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2, "Эпики с одинаковым id должны быть равны");
        assertEquals(epic1.hashCode(), epic2.hashCode(),
                "HashCode должен быть одинаковым");
    }

    @Test
    void epicsWithDifferentIdsShouldNotBeEqual() {
        Epic epic1 = new Epic("Эпик", "Описание");
        Epic epic2 = new Epic("Эпик", "Описание"); // Те же данные

        epic1.setId(1);
        epic2.setId(2);

        assertNotEquals(epic1, epic2, "Эпики с разным id не должны быть равны");
    }

    @Test
    void toStringShouldContainSubtaskIds() {
        epic.addSubtaskId(10);
        epic.addSubtaskId(20);

        String result = epic.toString();

        assertTrue(result.contains("Epic{"), "Должен начинаться с имени класса");
        assertTrue(result.contains("subtaskIds=[10, 20]"),
                "Должен содержать список подзадач");
        assertTrue(result.contains("title='Эпик'"), "Должен содержать заголовок");
    }

    @Test
    void removingNonExistentSubtaskShouldNotFail() {
        epic.addSubtaskId(subtask1.getId());
        assertEquals(1, epic.getSubtaskIds().size());

        assertDoesNotThrow(() -> {
            epic.removeSubtaskId(999);
        }, "Удаление несуществующей подзадачи не должно вызывать исключений");

        assertEquals(1, epic.getSubtaskIds().size(),
                "Количество подзадач не должно измениться");
        assertTrue(epic.getSubtaskIds().contains(subtask1.getId()),
                "Оригинальная подзадача должна остаться");
    }

    @Test
    void addingDuplicateSubtaskShouldBeIgnored() {
        epic.addSubtaskId(subtask1.getId());
        epic.addSubtaskId(subtask1.getId());

        assertEquals(1, epic.getSubtaskIds().size(),
                "Дубликаты подзадач не должны добавляться");
        assertTrue(epic.getSubtaskIds().contains(subtask1.getId()));
    }

    @Test
    void epicConstructorShouldSetInitialStatusToNew() {
        Epic newEpic = new Epic("Новый эпик", "Описание");
        assertEquals(Status.NEW, newEpic.getStatus(),
                "Конструктор должен устанавливать статус NEW");
    }

    @Test
    void settersAndGettersShouldWorkCorrectly() {
        epic.setTitle("Новое название");
        epic.setDescription("Новое описание");
        epic.setStatus(Status.IN_PROGRESS);
        epic.setId(42);

        assertEquals("Новое название", epic.getTitle());
        assertEquals("Новое описание", epic.getDescription());
        assertEquals(Status.IN_PROGRESS, epic.getStatus());
        assertEquals(42, epic.getId());
    }
}