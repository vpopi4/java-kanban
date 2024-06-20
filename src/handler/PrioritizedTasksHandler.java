package handler;

import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import util.TaskConverter;

import java.io.IOException;

public class PrioritizedTasksHandler extends BaseHttpHandler {
    public PrioritizedTasksHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        sendPayload(exchange, "history", TaskConverter.gson.toJson(manager.getPrioritizedTasks()));
    }
}
