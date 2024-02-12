package model;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(int id, TaskCreationData dto, Epic epic) {
        super(id, dto);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "\n\tid=" + id +
                ",\n\tname='" + name + '\'' +
                ",\n\tdescription='" + description + '\'' +
                ",\n\tstatus=" + status +
                ",\n\tepic.id=" + epic.getId() +
                "\n}";
    }
}
