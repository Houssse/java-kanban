import javakanban.data.Epic;
import javakanban.data.Status;
import javakanban.data.Subtask;
import javakanban.data.Task;
import javakanban.manager.InMemoryTaskManager;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        System.out.println("СОЗДАНИЕ ЗАДАЧ");

        Task task1 = manager.createTask(new Task("Задача 1", "Описание задачи 1", Status.NEW));
        Task task2 = manager.createTask(new Task("Задача 2", "Описание задачи 2", Status.NEW));

        System.out.println("Созданы обычные задачи:");
        System.out.println("• " + task1.getTitle() + " (ID: " + task1.getId() + ", Статус: " + task1.getStatus() + ")");
        System.out.println("• " + task2.getTitle() + " (ID: " + task2.getId() + ", Статус: " + task2.getStatus() + ")");
        System.out.println();

        Epic epic1 = manager.createEpic(new Epic("Эпик 1", "Организовать праздник"));
        Subtask subtask1 = manager.createSubtask(
                new Subtask("Составить список гостей", "Определить кто придет", Status.NEW, epic1.getId())
        );
        Subtask subtask2 = manager.createSubtask(
                new Subtask("Заказать еду", "Выбрать меню", Status.NEW, epic1.getId())
        );

        Epic epic2 = manager.createEpic(new Epic("Эпик 2", "Купить квартиру"));
        Subtask subtask3 = manager.createSubtask(
                new Subtask("Найти риелтора", "Выбрать агентство недвижимости", Status.NEW, epic2.getId())
        );

        System.out.println("Созданы эпики и подзадачи:");
        System.out.println("• " + epic1.getTitle() + " (ID: " + epic1.getId() + ", Статус: " + epic1.getStatus() + ")");
        System.out.println("  - " + subtask1.getTitle() + " (ID: " + subtask1.getId() + ")");
        System.out.println("  - " + subtask2.getTitle() + " (ID: " + subtask2.getId() + ")");
        System.out.println("• " + epic2.getTitle() + " (ID: " + epic2.getId() + ", Статус: " + epic2.getStatus() + ")");
        System.out.println("  - " + subtask3.getTitle() + " (ID: " + subtask3.getId() + ")");
        System.out.println();

        System.out.println("ИСТОРИЯ ПРОСМОТРОВ ЗАДАЧ");

        System.out.println("\n1. Просматриваем задачу 1 и эпик 1:");
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        printHistory(manager.getHistory(), "После просмотра Задачи 1 и Эпика 1");

        System.out.println("\n2. Просматриваем подзадачи эпика 1:");
        manager.getSubtaskById(subtask1.getId());
        manager.getSubtaskById(subtask2.getId());
        printHistory(manager.getHistory(), "После просмотра подзадач Эпика 1");

        System.out.println("\n3. Просматриваем задачу 2 и эпик 2:");
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic2.getId());
        printHistory(manager.getHistory(), "После просмотра Задачи 2 и Эпика 2");

        System.out.println("\n4. Повторно просматриваем задачу 1 (должна переместиться в начало):");
        manager.getTaskById(task1.getId());
        printHistory(manager.getHistory(), "После повторного просмотра Задачи 1");

        System.out.println("ВЫВОД ВСЕХ ЗАДАЧ \n");

        System.out.println("Все обычные задачи");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println();

        System.out.println("Все эпики");
        for (Epic epic : manager.getAllEpics()) {
            System.out.println(epic);
        }
        System.out.println();

        System.out.println("Все подзадачи");
        for (Subtask subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }
        System.out.println();

        System.out.println("ИЗМЕНЕНИЕ СТАТУСОВ\n");

        System.out.println("До изменения:");
        System.out.println("Эпик 1 статус: " + manager.getEpicById(epic1.getId()).getStatus());
        System.out.println("Задача 1 статус: " + manager.getTaskById(task1.getId()).getStatus());
        System.out.println();

        subtask1.setStatus(Status.DONE);
        manager.updateSubtask(subtask1);

        task1.setStatus(Status.IN_PROGRESS);
        manager.updateTask(task1);

        System.out.println("После изменения:");
        System.out.println("Подзадача 1 статус: " + manager.getSubtaskById(subtask1.getId()).getStatus());
        System.out.println("Эпик 1 статус: " + manager.getEpicById(epic1.getId()).getStatus() + " (должен быть IN_PROGRESS)");
        System.out.println("Задача 1 статус: " + manager.getTaskById(task1.getId()).getStatus());
        System.out.println();

        System.out.println("История после изменения статусов:");
        printHistory(manager.getHistory(), "После обновления статусов");

        System.out.println("УДАЛЕНИЕ ЗАДАЧ\n");

        System.out.println("До удаления:");
        System.out.println("Задач: " + manager.getAllTasks().size());
        System.out.println("Эпиков: " + manager.getAllEpics().size());
        System.out.println("Подзадач: " + manager.getAllSubtasks().size());
        System.out.println();

        manager.deleteTaskById(task1.getId());
        manager.deleteEpicById(epic2.getId());

        System.out.println("После удаления:");
        System.out.println("Задач: " + manager.getAllTasks().size() + " (должно быть 1)");
        System.out.println("Эпиков: " + manager.getAllEpics().size() + " (должно быть 1)");
        System.out.println("Подзадач: " + manager.getAllSubtasks().size() + " (должно быть 2)");
        System.out.println();

        System.out.println("ИСТОРИЯ ПОСЛЕ УДАЛЕНИЯ ЗАДАЧ");

        System.out.println("Задача 1 и Эпик 2 удалены, но остались в истории:");
        printHistory(manager.getHistory(), "После удаления задач");

        System.out.println("\nТЕСТ ОГРАНИЧЕНИЯ ИСТОРИИ (МАКСИМУМ 10 ЗАДАЧ)");

        for (int i = 3; i <= 12; i++) {
            Task newTask = manager.createTask(new Task("Задача " + i, "Описание " + i, Status.NEW));
            manager.getTaskById(newTask.getId());
            System.out.println("Просмотрена Задача " + i);
        }

        System.out.println("\nИстория после добавления 10 новых задач:");
        List<Task> finalHistory = manager.getHistory();
        System.out.println("Размер истории: " + finalHistory.size() + " (максимум 10)");

        printHistory(finalHistory, "Финальная история просмотров");

        // Выводим итоговую статистику
        System.out.println("\nИТОГОВАЯ СТАТИСТИКА");

        System.out.println("Всего задач в системе: " + manager.getAllTasks().size());
        System.out.println("Всего эпиков в системе: " + manager.getAllEpics().size());
        System.out.println("Всего подзадач в системе: " + manager.getAllSubtasks().size());
        System.out.println("Задач в истории просмотров: " + finalHistory.size());
    }

    private static void printHistory(List<Task> history, String description) {
        System.out.println(description + ":");

        if (history.isEmpty()) {
            System.out.println("   История пуста");
            return;
        }

        for (int i = 0; i < history.size(); i++) {
            Task task = history.get(i);
            String type = getTaskType(task);
            System.out.printf("%2d. %-20s (ID: %2d, Тип: %s)%n",
                    i + 1, task.getTitle(), task.getId(), type);
        }
    }

    private static String getTaskType(Task task) {
        if (task instanceof Epic) {
            return "Epic";
        } else if (task instanceof Subtask) {
            return "Subtask";
        } else {
            return "Task";
        }
    }
}