package util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DoublyLinkedListTest {

    private DoublyLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new DoublyLinkedList<>();
    }

    @Test
    void shouldAddElementToEndOfList() {
        // Given
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        // When
        ArrayList<Integer> values = list.getValues();

        // Then
        assertEquals(3, values.size());
        assertEquals(1, values.get(0));
        assertEquals(2, values.get(1));
        assertEquals(3, values.get(2));
    }

    @Test
    void shouldReturnAllValuesInList() {
        // Given
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);

        // When
        ArrayList<Integer> values = list.getValues();

        // Then
        assertEquals(3, values.size());
        assertEquals(1, values.get(0));
        assertEquals(2, values.get(1));
        assertEquals(3, values.get(2));
    }

    @Test
    void shouldReturnEmptyListForEmptyList() {
        // When
        ArrayList<Integer> values = list.getValues();

        // Then
        assertTrue(values.isEmpty());
    }

    @Test
    void shouldAddElementsOfDifferentTypes() {
        // Given
        DoublyLinkedList<String> stringList = new DoublyLinkedList<>();
        stringList.addLast("first");
        stringList.addLast("second");
        stringList.addLast("third");

        // When
        ArrayList<String> values = stringList.getValues();

        // Then
        assertEquals(3, values.size());
        assertEquals("first", values.get(0));
        assertEquals("second", values.get(1));
        assertEquals("third", values.get(2));
    }

    @Test
    void shouldRemoveNodeFromList() {
        // Given
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        ArrayList<Integer> values;

        // When
        // Удаляем второй узел
        list.removeNode(list.head.next);

        // Then
        values = list.getValues();
        assertEquals(2, values.size());
        assertEquals(1, values.get(0));
        assertEquals(3, values.get(1));

        // When
        // Удаляем первый узел
        list.removeNode(list.head);

        // Then
        values = list.getValues();
        assertEquals(1, values.size());
        assertEquals(3, values.get(0));

        // When
        // Удаляем последний узел
        list.removeNode(list.head);

        // Then
        values = list.getValues();
        assertTrue(values.isEmpty());
    }
}
