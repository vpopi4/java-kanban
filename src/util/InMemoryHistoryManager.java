package util;

import interfaces.HistoryManager;
import model.Task;

import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final HashMap<Integer, DoublyLinkedList.Node<Task>> map;
    private final DoublyLinkedList<Task> list;

    public InMemoryHistoryManager() {
        list = new DoublyLinkedList<>();
        map = new HashMap<>();
    }

    @Override
    public void add(Task item) {
        if (item == null) {
            return;
        }

        int id = item.getId();

        if (map.containsKey(id)) {
            var node = map.get(id);
            list.removeNode(node);
        }

        list.addLast(item);
        map.put(id, list.tail);
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Task> getHistory() {
        return list.getValues();
    }
}
