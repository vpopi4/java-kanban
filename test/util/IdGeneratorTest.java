package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorTest {
    private IdGenerator idGenerator;

    @BeforeEach
    public void beforeEach() {
        idGenerator = new IdGenerator();
    }

    @Test
    public void shouldReturn0AfterCallGenerateNewId() {
        int expected = 0;

        int actual = idGenerator.generateNewId();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturn1After2TimesCallGenerateNewId() {
        int expected = 1;

        idGenerator.generateNewId();
        int actual = idGenerator.generateNewId();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturn99After100TimesCallGenerateNewId() {
        int expected = 99;

        for (int i = 0; i < 99; i++) {
            idGenerator.generateNewId();
        }

        int actual = idGenerator.generateNewId();

        assertEquals(expected, actual);
    }
}
