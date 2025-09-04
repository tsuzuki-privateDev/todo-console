import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TaskRepository {
    private static final String FILE_NAME = "tasks.txt";
    private static final DateTimeFormatter DF = DateTimeFormatter.ISO_LOCAL_DATE;

    List<Task> load() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return tasks;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String raw = line.trim();
                if (raw.isEmpty()) continue;

                // v3以前の互換（"[x] タスク名"）
                if (!raw.contains("\t")) {
                    boolean done = raw.startsWith("[x]");
                    String name = raw.substring(3).trim();
                    tasks.add(new Task(name, done, null, Priority.MEDIUM));
                    continue;
                }

                // v4以降のタブ区切り: status \t name \t yyyy-mm-dd \t PRIORITY
                String[] sp = raw.split("\t", -1);
                boolean done = sp[0].trim().equals("[x]");
                String name = sp.length > 1 ? sp[1] : "";
                LocalDate due = null;
                if (sp.length > 2 && !sp[2].isEmpty()) { try { due = LocalDate.parse(sp[2], DF); } catch (Exception ignore) { }
                }
                Priority pr = Priority.MEDIUM;
                if (sp.length > 3 && !sp[3].isEmpty()) {
                    try {pr = Priority.valueOf(sp[3]); } catch (Exception ignore) {}
                }
                tasks.add(new Task(name, done, due, pr));
            }
        } catch (IOException e) {
            System.out.println("読み込みエラー： " + e.getMessage());
        }
        return tasks;
    }

    void save(List<Task> tasks) {
        File file = new File(FILE_NAME);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, false)))) {
            for (Task t : tasks) {
                String status = t.done ? "[x]" : "[ ]";
                String due = (t.due == null) ? "" : DF.format(t.due);
                pw.println(status + "\t" + t.name.replace("\t", " ") + "\t" + due + "\t" + t.priority.name());
            } 
        } catch (IOException e) {
            System.out.println("保存エラー： " + e.getMessage());
        }

    }
}
