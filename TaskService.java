import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class TaskService {
    private final List<Task> tasks;
    private final TaskRepository repo;
    private static final DateTimeFormatter DF = DateTimeFormatter.ISO_LOCAL_DATE;

    TaskService(TaskRepository repo) {
        this.repo = repo;
        this.tasks = repo.load();
    }

    // 読み取り
    List<Task> listAll() { return new ArrayList<>(tasks); }
    List<Task> listTodo() {
        List<Task> v = listAll(); v.removeIf(t -> t.done); return v;
    }
    List<Task> listDone() {
        List<Task> v = listAll(); v.removeIf(t -> !t.done); return v;
    }
    List<Task> listSortDue() {
        List<Task> v = listAll();
        v.sort(Comparator
            .comparing((Task t) -> t.due == null)
            .thenComparing(t -> t.due, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparingInt(t -> t.priority.order));
        return v;
    }
    List<Task> listSortPrio() {
        List<Task> v = listAll();
        v.sort(Comparator
            .comparingInt((Task t) -> t.priority.order)
            .thenComparing(t -> t.due, Comparator.nullsLast(Comparator.naturalOrder())));
        return v;
    }

    // 変更系（保存込み）
    void add(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("内容を入力してください。");
        tasks.add(new Task(name.trim()));
        repo.save(tasks);
    }

    Task delete(int oneBasedIndex) {
        int idx = index(oneBasedIndex);
        Task removed = tasks.remove(idx);
        repo.save(tasks);
        return removed;
    }

    void setDone(int oneBasedIndex, boolean done) {
        Task t = tasks.get(index(oneBasedIndex));
        t.done = done;
        repo.save(tasks);
    }

    void setDue(int oneBasedIndex, String yyyyMmDd) {
        Task t = tasks.get(index(oneBasedIndex));
        t.due = parseDate(yyyyMmDd);
        repo.save(tasks);
    }

    void clearDue(int oneBasedIndex) {
        Task t = tasks.get(index(oneBasedIndex));
        t.due = null;
        repo.save(tasks);
    }

    void setPrio(int oneBasedIndex, String prio) {
        Task t = tasks.get(index(oneBasedIndex));
        t.priority = Priority.from(prio);
        repo.save(tasks);
    }

    // ユーティリティ
    static String fmt(Task t) {
        String status = t.done ? "[x]" : "[ ]";
        String due = (t.due == null) ? "-" : DF.format(t.due);
        return status + " " + t.name + "  (due: " + due + ", prio: " + t.priority + ")";
    }

    private int index(int oneBased) {
        if (oneBased < 1 || oneBased > tasks.size()) {
            throw new IllegalArgumentException("番号が範囲外です。1〜" + tasks.size());
        }
        return oneBased - 1;
    }

    private static LocalDate parseDate(String s) {
        try { return LocalDate.parse(s, DF); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("日付は YYYY-MM-DD 形式で。例: 2025-09-04"); }
    }
}
