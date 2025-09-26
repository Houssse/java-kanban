package javakanban.test;

import javakanban.data.Status;
import javakanban.data.Task;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskTest {

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        task1 = new Task("Задача 1", "Описание задачи 1", Status.NEW);
        task2 = new Task("Задача 2", "Описание задачи 2", Status.IN_PROGRESS);
    }

    @Test
    void tasksWithSameIdShouldBeEqual() {
        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2, "Задачи с одинаковым id должны быть равны");

        assertEquals(task1.hashCode(), task2.hashCode(),
                "HashCode должен быть одинаковым для равных задач");
    }

    @Test
    void tasksWithDifferentIdsShouldNotBeEqual() {
        Task task1 = new Task("Задача", "Описание", Status.NEW);
        Task task2 = new Task("Задача", "Описание", Status.NEW);

        task1.setId(1);
        task2.setId(2);

        Assertions.assertNotEquals(task1, task2, "Задачи с разным id не должны быть равны");
    }

    @Test
    void taskShouldNotBeEqualToNull() {
        task1.setId(1);

        Assertions.assertNotEquals(null, task1, "Задача не должна быть равна null");
        Assertions.assertFalse(task1.equals(null), "Метод equals должен возвращать false для null");
    }

    @Test
    void taskShouldNotBeEqualToDifferentClass() {
        task1.setId(1);

        Assertions.assertNotEquals("Это строка, а не задача", task1,
                "Задача не должна быть равна объекту другого класса");
        Assertions.assertFalse(task1.equals("строка"), "Метод equals должен возвращать false для другого класса");
    }

    @Test
    void taskShouldBeEqualToItself() {
        task1.setId(1);

        assertEquals(task1, task1, "Задача должна быть равна самой себе");
        Assertions.assertTrue(task1.equals(task1), "Метод equals должен возвращать true для самого себя");
    }

    @Test
    void equalityShouldBeSymmetric() {
        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2, "task1 должна быть равна task2");
        assertEquals(task2, task1, "task2 должна быть равна task1 (симметричность)");
    }

    @Test
    void settersAndGettersShouldWorkCorrectly() {
        assertEquals("Задача 1", task1.getTitle());
        assertEquals("Описание задачи 1", task1.getDescription());
        assertEquals(Status.NEW, task1.getStatus());

        task1.setTitle("Новое название");
        task1.setDescription("Новое описание");
        task1.setStatus(Status.DONE);
        task1.setId(42);

        assertEquals("Новое название", task1.getTitle());
        assertEquals("Новое описание", task1.getDescription());
        assertEquals(Status.DONE, task1.getStatus());
        assertEquals(42, task1.getId());
    }

    @Test
    void toStringShouldReturnInformativeString() {
        task1.setId(1);
        String result = task1.toString();

        Assertions.assertTrue(result.contains("Task{"), "toString должен начинаться с имени класса");
        Assertions.assertTrue(result.contains("id=1"), "toString должен содержать id");
        Assertions.assertTrue(result.contains("title='Задача 1'"), "toString должен содержать заголовок");
        Assertions.assertTrue(result.contains("status=NEW"), "toString должен содержать статус");
    }


    @Test
    void tasksWithDefaultIdShouldBeEqual() {
        Task task3 = new Task("Задача 3", "Описание 3", Status.NEW);
        Task task4 = new Task("Задача 4", "Описание 4", Status.DONE);

        assertEquals(task3, task4, "Задачи с id=0 по умолчанию должны быть равны");
    }
}