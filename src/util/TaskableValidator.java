package util;

import interfaces.model.Taskable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

public class TaskableValidator {
    public static void checkIntersectionClosestSearch(Taskable t1, TreeSet<Taskable> ts) throws IntersectionException {
        // find the closest elements
        Taskable floor = ts.floor(t1);
        Taskable ceiling = ts.ceiling(t1);

        if (floor != null && isIntersect(t1, floor)) {
            throw new IntersectionException("intersection has occurred", floor);
        }
        if (ceiling != null && isIntersect(t1, ceiling)) {
            throw new IntersectionException("intersection has occurred", ceiling);
        }
    }

    public static void checkIntersectionLinear(Taskable t1, List<Taskable> ts) throws IntersectionException {
        for (Taskable t2 : ts) {
            if (isIntersect(t1, t2)) {
                throw new IntersectionException("intersection has occurred", t2);
            }
        }
    }

    public static boolean isIntersect(Taskable t1, Taskable t2) {
        LocalDateTime start1 = t1.getStartTime();
        LocalDateTime end1 = t1.getEndTime();

        LocalDateTime start2 = t2.getStartTime();
        LocalDateTime end2 = t2.getEndTime();

        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public static class IntersectionException extends IllegalArgumentException {
        Taskable taskable;

        public IntersectionException(String message, Taskable taskable) {
            super(message);
            this.taskable = taskable;
        }
    }
}
