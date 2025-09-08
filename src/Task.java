public class Task {
    private int id;
    private String name;
    private String description;
    private Status status;

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            System.out.println("Имя не может быть пустым");
        }

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            System.out.println("Описание не может быть пустым");
        }

        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }
}
