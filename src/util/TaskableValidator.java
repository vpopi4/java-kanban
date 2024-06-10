package util;

import interfaces.model.Taskable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

public class TaskableValidator {
    public static boolean checkIntersectionClosestSearch(Taskable t1, TreeSet<Taskable> ts) {
        // find the closest elements
        Taskable floor = ts.floor(t1);
        Taskable ceiling = ts.ceiling(t1);

        if (floor != null && isIntersect(t1, floor)) {
            return true;
        }
        if (ceiling != null && isIntersect(t1, ceiling)) {
            return true;
        }
        return false;
    }

    public static boolean checkIntersectionLinear(Taskable t1, List<Taskable> ts) {
        for (Taskable t2 : ts) {
            if (isIntersect(t1, t2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIntersect(Taskable t1, Taskable t2) {
        LocalDateTime start1 = t1.getStartTime();
        LocalDateTime end1 = t1.getEndTime();

        LocalDateTime start2 = t2.getStartTime();
        LocalDateTime end2 = t2.getEndTime();

        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
