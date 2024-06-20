package handler;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import interfaces.model.Taskable;
import model.Task;
import util.TaskConverter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

public class TasksHandler extends BaseHttpHandler {
    public TasksHandler(TaskManager manager) {
        super(manager);
    }

    private static Task extractTask(HttpExchange exchange) {
        try (InputStream requestBody = exchange.getRequestBody()) {
            String bodyString = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);

            Taskable taskable = TaskConverter.formJson(JsonParser
                    .parseString(bodyString)
                    .getAsJsonObject()
                    .get("task")
                    .getAsJsonObject()
                    .toString()
            );

            if (taskable instanceof Task) {
                return (Task) taskable;
            } else {
                throw new IllegalStateException("not a Task: " + taskable);
            }
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("json object was expected in request body");
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("could not resolve request body");
        }
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.matches("/tasks/\\d+")) {
            handleGetById(exchange);
        } else {
            handleGetAll(exchange);
        }
    }

    private void handleGetAll(HttpExchange exchange) throws IOException {
        List<Task> all = manager.getTaskService().getAll();

        sendPayload(exchange, "tasks", TaskConverter.toJson(all));
    }

    private void handleGetById(HttpExchange exchange)
            throws IllegalArgumentException, NoSuchElementException, IOException {
        Task task = manager.getTaskService().get(extractId(exchange));

        sendPayload(exchange, "task", TaskConverter.toJson(task));
    }

    @Override
    protected void handlePost(HttpExchange exchange)
            throws IllegalArgumentException, NoSuchElementException, IOException {
        Task task = extractTask(exchange);

        if (task.getId() == null) {
            task = manager.getTaskService().create(
                    task.getName(),
                    task.getDescription(),
                    task.getDuration(),
                    task.getStartTime()
            );
        } else {
            task = manager.getTaskService().update(task);
        }

        sendPayload(exchange, "task", TaskConverter.toJson(task));
    }

    @Override
    protected void handleDelete(HttpExchange exchange) throws IllegalArgumentException, IOException {
        manager.getTaskService().remove(extractId(exchange));
        sendOk(exchange);
    }
}
