package handler;

import com.sun.net.httpserver.HttpExchange;
import interfaces.TaskManager;
import util.TaskConverter;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    public HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    protected void handleGet(HttpExchange exchange) throws IOException {
        sendPayload(exchange, "history", TaskConverter.gson.toJson(manager.getHistory()));
    }
}
