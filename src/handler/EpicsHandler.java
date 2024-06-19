package handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import model.Epic;
import model.Task;
import util.TaskConverter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.NoSuchElementException;

public class EpicsHandler extends BaseHttpHandler {
    private final TaskManager manager;

    public EpicsHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        try {
            switch (method) {
                case "GET" -> {
                    if (path.matches("/epics/\\d+")) {
                        handleGetById(exchange);
                    } else {
                        handleGetAll(exchange);
                    }
                }
                case "POST" -> handlePost(exchange);
                case "DELETE" -> handleDelete(exchange);
                case null, default -> sendNotFound(exchange, "no such endpoint");
            }
        } catch (NoSuchElementException e) {
            sendNotFound(exchange, e.getMessage());
        } catch (IllegalArgumentException e) {
            sendBadRequest(exchange, e.getMessage());
        } catch (Exception e) {
            sendInternalServerError(exchange, "something went wrong");
            e.printStackTrace();
        }
    }

    private void handleGetAll(HttpExchange exchange) throws IOException {
        List<Epic> all = manager.getEpicService().getAll();

        sendPayload(exchange, "epics", TaskConverter.toJson(all));
    }

    private void handleGetById(HttpExchange exchange)
            throws NoSuchElementException, IllegalArgumentException, IOException {
        Task task = manager.getTaskService().get(extractId(exchange));

        sendPayload(exchange, "epic", TaskConverter.toJson(task));
    }

    private void handlePost(HttpExchange exchange) throws IllegalArgumentException, IOException {
        String name;
        String description;

        try (InputStream is = exchange.getRequestBody()) {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject body = JsonParser.parseString(json).getAsJsonObject();

            name = extractField(body, "name");
            description = extractField(body, "description");
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("json object was expected in request body");
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("could not resolve request body");
        }

        Epic epic = manager.getEpicService().create(name, description);
        sendPayload(exchange, "epic", TaskConverter.toJson(epic));
    }

    private void handleDelete(HttpExchange exchange) throws IllegalArgumentException, IOException {
        manager.getEpicService().remove(extractId(exchange));
        sendOk(exchange);
    }

    private Integer extractId(HttpExchange exchange) throws IllegalArgumentException {
        try {
            String secondInPath = exchange
                    .getRequestURI()
                    .getPath()
                    .split("/")
                    [2];
            return Integer.parseInt(secondInPath);
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            throw new IllegalArgumentException("id must be integer");
        }
    }

    private String extractField(JsonObject json, String fieldName) {
        if (!json.has(fieldName)) {
            throw new IllegalArgumentException("key \"" + fieldName + "\" is missing");
        }

        if (json.get(fieldName).isJsonNull()) {
            throw new IllegalArgumentException("key \"" + fieldName + "\" is null");
        }

        return json.get(fieldName).getAsString();
    }
}
