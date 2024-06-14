package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdGeneratorTest {
    private IdGenerator idGenerator;

    @BeforeEach
    public void beforeEach() {
        idGenerator = new IdGenerator();
    }

    @Test
    public void testGenerationNewId_firstCall_return0() {
        int expected = 0;

        int actual = idGenerator.generateNewId();

        assertEquals(expected, actual);
    }

    @Test
    public void testGenerationNewId_2TimesCall_return1() {
        int expected = 1;

        idGenerator.generateNewId();
        int actual = idGenerator.generateNewId();

        assertEquals(expected, actual);
    }

    @Test
    public void testGenerationNewId_100TimesCall_return99() {
        int expected = 99;

        for (int i = 0; i < 99; i++) {
            idGenerator.generateNewId();
        }

        int actual = idGenerator.generateNewId();

        assertEquals(expected, actual);
    }
}
