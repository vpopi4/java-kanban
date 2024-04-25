package util;

import java.util.ArrayList;

public class DoublyLinkedList<T> {
    public Node<T> head;
    public Node<T> tail;
    private int size;

    public DoublyLinkedList() {
        size = 0;
        head = tail = null;
    }

    public void addLast(T value) {
        Node<T> newNode = new Node<>(value);

        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }

        size++;
    }

    public ArrayList<T> getValues() {
        ArrayList<T> list = new ArrayList<>(this.size);

        Node<T> current = head;

        while(current != null) {
            list.add(current.value);
            current = current.next;
        }

        return list;
    }

    public void removeNode(Node<T> node) {
        if (node == null) {
            return;
        }

        if (node.prev == null) {
            head = node.next;
        } else {
            node.prev.next = node.next;
        }

        if (node.next == null) {
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
        }

        size--;
    }

    static class Node<T> {
        Node<T> prev;
        Node<T> next;
        T value;

        public Node(T value) {
            this.prev = null;
            this.next = null;
            this.value = value;
        }
    }
}
