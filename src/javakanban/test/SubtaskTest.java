package javakanban.test;

import javakanban.data.Epic;
import javakanban.data.Status;
import javakanban.data.Subtask;
import javakanban.data.Task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {

    private Subtask subtask;
    private Epic epic;

    @BeforeEach
    void setUp() {
        epic = new Epic("Родительский эпик", "Описание эпика");
        epic.setId(1);

        subtask = new Subtask("Подзадача", "Описание подзадачи", Status.NEW, epic.getId());
        subtask.setId(2);
    }

    @Test
    void subtaskShouldInheritEqualsFromTask() {
        Subtask subtask1 = new Subtask("Подзадача 1", "Описание 1", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Подзадача 2", "Описание 2", Status.DONE, 2);

        subtask1.setId(10);
        subtask2.setId(10);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым id должны быть равны");
        assertEquals(subtask1.hashCode(), subtask2.hashCode(),
                "HashCode должен быть одинаковым");
    }

    @Test
    void subtasksWithDifferentIdsShouldNotBeEqual() {
        Subtask subtask1 = new Subtask("Подзадача", "Описание", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Подзадача", "Описание", Status.NEW, 1);

        subtask1.setId(1);
        subtask2.setId(2);

        assertNotEquals(subtask1, subtask2, "Подзадачи с разным id не должны быть равны");
    }

    @Test
    void subtaskSpecificGettersAndSettersShouldWork() {
        assertEquals(epic.getId(), subtask.getEpicId(),
                "EpicId должен соответствовать переданному в конструкторе");

        subtask.setEpicId(100);
        assertEquals(100, subtask.getEpicId(), "EpicId должен обновляться через сеттер");

        subtask.setEpicId(epic.getId());
    }

    @Test
    void toStringShouldContainEpicId() {
        String result = subtask.toString();

        assertTrue(result.contains("Subtask{"), "Должен начинаться с имени класса");
        assertTrue(result.contains("epicId=" + epic.getId()),
                "Должен содержать ID эпика");
        assertTrue(result.contains("title='Подзадача'"), "Должен содержать заголовок");
        assertTrue(result.contains("status=NEW"), "Должен содержать статус");
    }

    @Test
    void constructorShouldSetAllFieldsCorrectly() {
        Subtask newSubtask = new Subtask("Новая подзадача", "Описание", Status.IN_PROGRESS, 50);

        assertEquals("Новая подзадача", newSubtask.getTitle());
        assertEquals("Описание", newSubtask.getDescription());
        assertEquals(Status.IN_PROGRESS, newSubtask.getStatus());
        assertEquals(50, newSubtask.getEpicId());
    }


    @Test
    void subtaskShouldInheritTaskProperties() {
        subtask.setTitle("Новое название");
        subtask.setDescription("Новое описание");
        subtask.setStatus(Status.DONE);
        subtask.setId(99);

        assertEquals("Новое название", subtask.getTitle());
        assertEquals("Новое описание", subtask.getDescription());
        assertEquals(Status.DONE, subtask.getStatus());
        assertEquals(99, subtask.getId());
    }

    @Test
    void subtaskShouldBeEqualToTaskWithSameId() {
        Task task = new Task("Обычная задача", "Описание", Status.NEW);
        Subtask subtask = new Subtask("Подзадача", "Описание", Status.NEW, 1);

        task.setId(5);
        subtask.setId(5);

        assertEquals(task, subtask, "Задача и подзадача с одинаковым id должны быть равны");
        assertEquals(subtask, task, "Сравнение должно быть симметричным");
    }

    @Test
    void subtaskShouldNotBeEqualToNull() {
        assertNotEquals(null, subtask, "Подзадача не должна быть равна null");
        assertFalse(subtask.equals(null), "Метод equals должен возвращать false для null");
    }

    @Test
    void subtaskShouldNotBeEqualToDifferentClass() {
        assertNotEquals("строка", subtask, "Подзадача не должна быть равна строке");
        assertFalse(subtask.equals(new Object()), "Метод equals должен возвращать false для другого класса");
    }

    @Test
    void subtaskWithZeroEpicIdShouldWork() {
        Subtask subtaskWithZeroEpic = new Subtask("Подзадача", "Описание", Status.NEW, 0);

        assertEquals(0, subtaskWithZeroEpic.getEpicId(), "EpicId=0 должен быть допустимым");
        assertDoesNotThrow(() -> {
            subtaskWithZeroEpic.setEpicId(0); // Установка нулевого epicId
        }, "Установка epicId=0 не должна вызывать ошибок");
    }

    @Test
    void subtaskShouldBeEqualToItself() {
        assertEquals(subtask, subtask, "Подзадача должна быть равна самой себе");
        assertEquals(subtask, subtask, "Метод equals должен возвращать true для самого себя");
    }
}