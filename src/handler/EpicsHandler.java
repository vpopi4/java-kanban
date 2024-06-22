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
    public EpicsHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();

        if (path.matches("/epics/\\d+")) {
            handleGetById(exchange);
        } else {
            handleGetAll(exchange);
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

    @Override
    protected void handlePost(HttpExchange exchange) throws IllegalArgumentException, IOException {
        String name;
        String description;

        try (InputStream is = exchange.getRequestBody()) {
            String json = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            JsonObject epicFieldInBody = JsonParser
                    .parseString(json)
                    .getAsJsonObject()
                    .get("epic")
                    .getAsJsonObject();

            name = extractField(epicFieldInBody, "name");
            description = extractField(epicFieldInBody, "description");
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

    @Override
    protected void handleDelete(HttpExchange exchange) throws IllegalArgumentException, IOException {
        manager.getEpicService().remove(extractId(exchange));
        sendOk(exchange);
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
