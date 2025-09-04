import java.time.LocalDate;

class Task {
    String name;
    boolean done;
    LocalDate due;
    Priority priority;

    Task(String name) { this(name, false, null, Priority.MEDIUM); }
    Task(String name, boolean done) { this(name, done, null, Priority.MEDIUM); }
    Task(String name, boolean done, LocalDate due, Priority prio) {
        this.name = name;
        this.done = done;
        this.due = due;
        this.priority = (prio == null) ? Priority.MEDIUM : prio;
    }
}
