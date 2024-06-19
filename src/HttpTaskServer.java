import com.sun.net.httpserver.HttpServer;
import handler.*;
import interfaces.TaskManager;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer server;

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();
    }

    public HttpTaskServer() throws IOException {
        TaskManager manager = new Managers().getDefault();
        server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/tasks", new TasksHandler(manager));
        server.createContext("/subtasks", new SubtasksHandler(manager));
        server.createContext("/epics", new EpicsHandler(manager));
        server.createContext("/history", new HistoryHandler());
        server.createContext("/prioritized", new PrioritizedTasksHandler());
    }

    public void start() {
        server.start();
        System.out.println("Server started on port 8080");
    }

    public void stop() {
        server.stop(0);
        System.out.println("Server stopped");
    }
}
