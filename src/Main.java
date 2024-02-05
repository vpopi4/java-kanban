import dtos.TaskDTO;
import enums.TaskStatus;
import model.Epic;
import model.Subtask;
import model.Task;
import service.TaskManager;

public class Main {
    public static void main(String[] args) {
        TaskManager tm = new TaskManager();

        System.out.println("Creating entities...\n");

        Task firstTask = tm.createTask(
                new TaskDTO("first task", "lorem ipsum dollar")
        );
        Task secondTask = tm.createTask(
                new TaskDTO("Second task", "Рыбий текст - это круто!")
        );

        Epic firstEpic = tm.createEpic(
                new TaskDTO("first epic", "All the leaves are brown")
        );
        Subtask firstSubtaskOfFirstEpic = tm.createSubtask(
                new TaskDTO("first subtask of first epic", "And the sky is gray"),
                firstEpic.getId()
        );
        Subtask secondSubtaskOfFirstEpic = tm.createSubtask(
                new TaskDTO("second subtask of first epic", "I've been for a walk"),
                firstEpic.getId()
        );

        Epic socondEpic = tm.createEpic(
                new TaskDTO("second epic", "On a winter's day")
        );
        Subtask firstSubtaskOfSecondEpic = tm.createSubtask(
                new TaskDTO("first subtask of second epic", "I'd be safe and warm"),
                socondEpic.getId()
        );

        System.out.println("Tasks:\n" + tm.getAllTasks() + "\n");
        System.out.println("Epics:\n" + tm.getAllEpics() + "\n");
        System.out.println("Subtasks:\n" + tm.getAllSubtasks() + "\n");

        firstTask.setStatus(TaskStatus.IN_PROGRESS);
        firstTask = tm.updateTask(firstTask);
        System.out.println("Changing first task:\n" + firstTask + "\n");

        firstSubtaskOfFirstEpic.setStatus(TaskStatus.DONE);
        firstSubtaskOfFirstEpic = tm.updateSubtask(firstSubtaskOfSecondEpic);
        secondSubtaskOfFirstEpic.setStatus(TaskStatus.IN_PROGRESS);
        secondSubtaskOfFirstEpic = tm.updateSubtask(secondSubtaskOfFirstEpic);
        System.out.println("Changing subtasks of firstEpic:\n"
                + firstEpic + "\n" + firstEpic.getSubtasks() + "\n");

        secondSubtaskOfFirstEpic.setStatus(TaskStatus.DONE);
        secondSubtaskOfFirstEpic = tm.updateSubtask(secondSubtaskOfFirstEpic);
        System.out.println("Changing second subtask: " + secondSubtaskOfFirstEpic);
        System.out.println(firstEpic + "\n");

        System.out.println("Deleting first task...");
        tm.removeTask(firstTask.getId());
        System.out.println("Deleting first epic...");
        tm.removeEpic(firstEpic.getId());
        System.out.println("Printing all data:");

        System.out.println("Tasks:\n" + tm.getAllTasks() + "\n");
        System.out.println("Epics:\n" + tm.getAllEpics() + "\n");
        System.out.println("Subtasks:\n" + tm.getAllSubtasks() + "\n");
    }
}
