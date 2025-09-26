package javakanban.test;

import javakanban.data.*;
import javakanban.manager.InMemoryTaskManager;
import javakanban.manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    // ========== ТЕСТЫ ДЛЯ ЗАДАЧ (TASK) ==========

    @Test
    void createTaskShouldAssignUniqueIdAndStoreTask() {
        Task task = new Task("Test Task", "Description", Status.NEW);
        Task created = taskManager.createTask(task);

        assertNotNull(created.getId(), "Созданной задаче должен быть присвоен ID");
        Task retrieved = taskManager.getTaskById(created.getId());
        assertEquals(created, retrieved, "Задача должна быть доступна по ID после создания");
    }

    @Test
    void getAllTasksShouldReturnEmptyListWhenNoTasks() {
        assertTrue(taskManager.getAllTasks().isEmpty(),
                "Список задач должен быть пустым при инициализации");
    }

    @Test
    void getAllTasksShouldReturnAllCreatedTasks() {
        Task task1 = taskManager.createTask(new Task("Task 1", "Desc 1", Status.NEW));
        Task task2 = taskManager.createTask(new Task("Task 2", "Desc 2", Status.IN_PROGRESS));

        List<Task> tasks = taskManager.getAllTasks();
        assertEquals(2, tasks.size(), "Должны вернуться все созданные задачи");
        assertTrue(tasks.contains(task1) && tasks.contains(task2));
    }

    @Test
    void getTaskByIdShouldReturnNullForNonExistentId() {
        assertNull(taskManager.getTaskById(999),
                "Должен вернуться null для несуществующего ID");
    }

    @Test
    void updateTaskShouldModifyExistingTask() {
        Task original = taskManager.createTask(new Task("Original", "Desc", Status.NEW));
        Task updated = new Task("Updated", "New Desc", Status.DONE);
        updated.setId(original.getId());

        taskManager.updateTask(updated);
        Task retrieved = taskManager.getTaskById(original.getId());

        assertEquals("Updated", retrieved.getTitle());
        assertEquals("New Desc", retrieved.getDescription());
        assertEquals(Status.DONE, retrieved.getStatus());
    }

    @Test
    void updateTaskShouldIgnoreNonExistentTask() {
        Task nonExistent = new Task("Non-existent", "Desc", Status.NEW);
        nonExistent.setId(999);

        assertDoesNotThrow(() -> taskManager.updateTask(nonExistent),
                "Обновление несуществующей задачи не должно вызывать исключений");
    }

    @Test
    void deleteTaskByIdShouldRemoveTask() {
        Task task = taskManager.createTask(new Task("Task", "Desc", Status.NEW));
        taskManager.deleteTaskById(task.getId());

        assertNull(taskManager.getTaskById(task.getId()), "Задача должна быть удалена");
        assertTrue(taskManager.getAllTasks().isEmpty());
    }

    @Test
    void deleteAllTasksShouldClearTaskStorage() {
        taskManager.createTask(new Task("Task 1", "Desc 1", Status.NEW));
        taskManager.createTask(new Task("Task 2", "Desc 2", Status.DONE));

        taskManager.deleteAllTasks();
        assertTrue(taskManager.getAllTasks().isEmpty(), "Все задачи должны быть удалены");
    }

    // ========== ТЕСТЫ ДЛЯ ЭПИКОВ (EPIC) ==========

    @Test
    void createEpicShouldStoreEpicWithoutSubtasks() {
        Epic epic = new Epic("Test Epic", "Description");
        Epic created = taskManager.createEpic(epic);

        assertNotNull(created.getId(), "Эпику должен быть присвоен ID");
        assertTrue(created.getSubtaskIds().isEmpty(), "Новый эпик должен быть без подзадач");
        assertEquals(Status.NEW, created.getStatus(), "Статус нового эпика должен быть NEW");
    }

    @Test
    void getAllEpicsShouldReturnAllCreatedEpics() {
        Epic epic1 = taskManager.createEpic(new Epic("Epic 1", "Desc 1"));
        Epic epic2 = taskManager.createEpic(new Epic("Epic 2", "Desc 2"));

        List<Epic> epics = taskManager.getAllEpics();
        assertEquals(2, epics.size(), "Должны вернуться все созданные эпики");
        assertTrue(epics.contains(epic1) && epics.contains(epic2));
    }

    @Test
    void getEpicByIdShouldReturnNullForNonExistentId() {
        assertNull(taskManager.getEpicById(999),
                "Должен вернуться null для несуществующего ID эпика");
    }

    @Test
    void updateEpicShouldModifyOnlyTitleAndDescription() {
        Epic original = taskManager.createEpic(new Epic("Original", "Desc"));
        Subtask subtask = taskManager.createSubtask(
                new Subtask("Subtask", "Desc", Status.NEW, original.getId()));

        Epic updated = new Epic("Updated", "New Desc");
        updated.setId(original.getId());
        updated.setStatus(Status.DONE);

        taskManager.updateEpic(updated);
        Epic retrieved = taskManager.getEpicById(original.getId());

        assertEquals("Updated", retrieved.getTitle());
        assertEquals("New Desc", retrieved.getDescription());
        assertEquals(1, retrieved.getSubtaskIds().size(), "Подзадачи должны сохраниться");
        assertNotEquals(Status.DONE, retrieved.getStatus(),
                "Статус эпика не должен меняться при прямом обновлении");
    }

    @Test
    void deleteEpicByIdShouldRemoveEpicAndItsSubtasks() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Desc"));
        Subtask subtask = taskManager.createSubtask(
                new Subtask("Subtask", "Desc", Status.NEW, epic.getId()));

        taskManager.deleteEpicById(epic.getId());

        assertNull(taskManager.getEpicById(epic.getId()), "Эпик должен быть удален");
        assertNull(taskManager.getSubtaskById(subtask.getId()), "Подзадача эпика должна быть удалена");
    }

    @Test
    void deleteAllEpicsShouldRemoveAllEpicsAndSubtasks() {
        Epic epic1 = taskManager.createEpic(new Epic("Epic 1", "Desc"));
        taskManager.createSubtask(new Subtask("Sub 1", "Desc", Status.NEW, epic1.getId()));

        Epic epic2 = taskManager.createEpic(new Epic("Epic 2", "Desc"));
        taskManager.createSubtask(new Subtask("Sub 2", "Desc", Status.DONE, epic2.getId()));

        taskManager.deleteAllEpics();

        assertTrue(taskManager.getAllEpics().isEmpty(), "Все эпики должны быть удалены");
        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Все подзадачи должны быть удалены");
    }

    // ========== ТЕСТЫ ДЛЯ ПОДЗАДАЧ (SUBTASK) ==========

    @Test
    void createSubtaskShouldAddItToEpicAndUpdateEpicStatus() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Desc"));
        Subtask subtask = new Subtask("Subtask", "Desc", Status.NEW, epic.getId());

        Subtask created = taskManager.createSubtask(subtask);

        assertEquals(epic.getId(), created.getEpicId(), "Подзадача должна быть привязана к эпику");
        Epic updatedEpic = taskManager.getEpicById(epic.getId());
        assertTrue(updatedEpic.getSubtaskIds().contains(created.getId()));
        assertEquals(Status.NEW, updatedEpic.getStatus());
    }

    @Test
    void createSubtaskShouldThrowExceptionForNonExistentEpic() {
        Subtask subtask = new Subtask("Subtask", "Desc", Status.NEW, 999);

        assertThrows(IllegalArgumentException.class, () -> taskManager.createSubtask(subtask),
                "Создание подзадачи для несуществующего эпика должно вызывать исключение");
    }

    @Test
    void getAllSubtasksShouldReturnAllCreatedSubtasks() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Desc"));
        Subtask sub1 = taskManager.createSubtask(new Subtask("Sub 1", "Desc", Status.NEW, epic.getId()));
        Subtask sub2 = taskManager.createSubtask(new Subtask("Sub 2", "Desc", Status.DONE, epic.getId()));

        List<Subtask> subtasks = taskManager.getAllSubtasks();
        assertEquals(2, subtasks.size(), "Должны вернуться все созданные подзадачи");
        assertTrue(subtasks.contains(sub1) && subtasks.contains(sub2));
    }

    @Test
    void getSubtaskByIdShouldReturnNullForNonExistentId() {
        assertNull(taskManager.getSubtaskById(999),
                "Должен вернуться null для несуществующего ID подзадачи");
    }

    @Test
    void updateSubtaskShouldModifyExistingSubtaskAndUpdateEpicStatus() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Desc"));
        Subtask original = taskManager.createSubtask(
                new Subtask("Original", "Desc", Status.NEW, epic.getId()));

        Subtask updated = new Subtask("Updated", "New Desc", Status.DONE, epic.getId());
        updated.setId(original.getId());

        taskManager.updateSubtask(updated);
        Subtask retrieved = taskManager.getSubtaskById(original.getId());

        assertEquals("Updated", retrieved.getTitle());
        assertEquals("New Desc", retrieved.getDescription());
        assertEquals(Status.DONE, retrieved.getStatus());
        assertEquals(Status.DONE, taskManager.getEpicById(epic.getId()).getStatus(),
                "Статус эпика должен обновиться после изменения подзадачи");
    }

    @Test
    void deleteSubtaskByIdShouldRemoveSubtaskAndUpdateEpicStatus() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Desc"));
        Subtask subtask = taskManager.createSubtask(new Subtask("Sub", "Desc", Status.DONE, epic.getId()));

        assertEquals(Status.DONE, taskManager.getEpicById(epic.getId()).getStatus());

        taskManager.deleteSubtaskById(subtask.getId());
        assertNull(taskManager.getSubtaskById(subtask.getId()), "Подзадача должна быть удалена");
        assertEquals(Status.NEW, taskManager.getEpicById(epic.getId()).getStatus(),
                "Статус эпика должен стать NEW после удаления подзадачи");
    }

    @Test
    void deleteAllSubtasksShouldClearAllSubtasksAndUpdateEpics() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Desc"));
        taskManager.createSubtask(new Subtask("Sub", "Desc", Status.DONE, epic.getId()));

        taskManager.deleteAllSubtasks();

        assertTrue(taskManager.getAllSubtasks().isEmpty(), "Все подзадачи должны быть удалены");
        assertEquals(Status.NEW, taskManager.getEpicById(epic.getId()).getStatus(),
                "Статус эпика должен стать NEW после удаления всех подзадач");
    }

    // ========== ТЕСТЫ СТАТУСОВ ЭПИКОВ ==========

    @Test
    void epicStatusShouldBeNewWhenNoSubtasks() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Desc"));
        assertEquals(Status.NEW, epic.getStatus(),
                "Статус эпика без подзадач должен быть NEW");
    }

    @Test
    void epicStatusShouldBeNewWhenAllSubtasksNew() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Desc"));
        taskManager.createSubtask(new Subtask("Sub 1", "Desc", Status.NEW, epic.getId()));
        taskManager.createSubtask(new Subtask("Sub 2", "Desc", Status.NEW, epic.getId()));

        assertEquals(Status.NEW, taskManager.getEpicById(epic.getId()).getStatus(),
                "Статус эпика должен быть NEW когда все подзадачи NEW");
    }

    @Test
    void epicStatusShouldBeDoneWhenAllSubtasksDone() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Desc"));
        taskManager.createSubtask(new Subtask("Sub 1", "Desc", Status.DONE, epic.getId()));
        taskManager.createSubtask(new Subtask("Sub 2", "Desc", Status.DONE, epic.getId()));

        assertEquals(Status.DONE, taskManager.getEpicById(epic.getId()).getStatus(),
                "Статус эпика должен быть DONE когда все подзадачи DONE");
    }

    @Test
    void epicStatusShouldBeInProgressWhenMixedSubtasks() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Desc"));
        taskManager.createSubtask(new Subtask("Sub 1", "Desc", Status.NEW, epic.getId()));
        taskManager.createSubtask(new Subtask("Sub 2", "Desc", Status.DONE, epic.getId()));

        assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus(),
                "Статус эпика должен быть IN_PROGRESS при смешанных статусах подзадач");
    }

    @Test
    void epicStatusShouldBeInProgressWhenSubtaskInProgress() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Desc"));
        taskManager.createSubtask(new Subtask("Sub", "Desc", Status.IN_PROGRESS, epic.getId()));

        assertEquals(Status.IN_PROGRESS, taskManager.getEpicById(epic.getId()).getStatus(),
                "Статус эпика должен быть IN_PROGRESS когда есть подзадача IN_PROGRESS");
    }

    // ========== ТЕСТЫ ПОЛУЧЕНИЯ ПОДЗАДАЧ ЭПИКА ==========

    @Test
    void getSubtasksByEpicIdShouldReturnCorrectList() {
        Epic epic1 = taskManager.createEpic(new Epic("Epic 1", "Desc"));
        Epic epic2 = taskManager.createEpic(new Epic("Epic 2", "Desc"));

        Subtask sub1 = taskManager.createSubtask(new Subtask("Sub 1", "Desc", Status.NEW, epic1.getId()));
        Subtask sub2 = taskManager.createSubtask(new Subtask("Sub 2", "Desc", Status.DONE, epic1.getId()));
        Subtask sub3 = taskManager.createSubtask(new Subtask("Sub 3", "Desc", Status.NEW, epic2.getId()));

        List<Subtask> epic1Subtasks = taskManager.getSubtasksByEpicId(epic1.getId());
        assertEquals(2, epic1Subtasks.size(), "Должны вернуться только подзадачи epic1");
        assertTrue(epic1Subtasks.contains(sub1) && epic1Subtasks.contains(sub2));
        assertFalse(epic1Subtasks.contains(sub3));
    }

    @Test
    void getSubtasksByEpicIdShouldReturnEmptyListForNonExistentEpic() {
        List<Subtask> subtasks = taskManager.getSubtasksByEpicId(999);
        assertTrue(subtasks.isEmpty(),
                "Для несуществующего эпика должен возвращаться пустой список");
    }

    @Test
    void getSubtasksByEpicIdShouldReturnEmptyListForEpicWithoutSubtasks() {
        Epic epic = taskManager.createEpic(new Epic("Epic", "Desc"));
        List<Subtask> subtasks = taskManager.getSubtasksByEpicId(epic.getId());
        assertTrue(subtasks.isEmpty(),
                "Для эпика без подзадач должен возвращаться пустой список");
    }
}