package util;

import java.util.ArrayList;

public class DoublyLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
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
