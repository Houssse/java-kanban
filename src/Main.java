public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

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
    }
}