package util;

public class IdGenerator {
    private Integer seq;

    public IdGenerator() {
        seq = 0;
    }

    public Integer generateNewId() {
        return seq++;
    }
}
