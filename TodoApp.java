import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class TodoApp {
    private static final String FILE_NAME = "tasks.txt";
    public static void main(String[] args) {        
        Scanner sc = new Scanner(System.in);
        List<Task> tasks =loadTasks();

        System.out.println("ToDoアプリ v3: add <内容> / list / delete <番号> / done <番号> / exit");

        while (true) {
            System.out.print("> ");
            String line = sc.nextLine();
            if (line == null) break;
            String trim = line.trim();
            if (trim.equalsIgnoreCase("exit")) break;
            if (trim.isEmpty()) continue;

            // コマンド判定
            if (trim.startsWith("add ")) {  // "add "で始まるとき
                String taskName = trim.substring(4).trim();
                if (taskName.isEmpty()) {
                    System.out.println("内容を入力してください。");
                } else {
                    tasks.add(new Task(taskName));
                    System.out.println("追加： " + taskName);
                    saveTasks(tasks);
                }
            } else if (trim.equals("list")) {
                if (tasks.isEmpty()) {
                    System.out.println("(タスク無し)");
                } else {
                    for (int i = 0; i < tasks.size(); i++) {
                        Task t = tasks.get(i);
                        String status = t.done ? "[x]" : "[ ]";
                        System.out.println((i + 1) + status + t.name);
                    }
                }
            } else if (trim.startsWith("delete ")) {
                try {
                    int index = Integer.parseInt(trim.substring(7).trim());
                    if (index < 1 || index > tasks.size()) {
                        System.out.println("番号が不正です。");
                    } else {
                        Task removed = tasks.remove(index - 1);
                        System.out.println("削除: " + removed.name);
                        saveTasks(tasks);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("削除コマンドは数字を指定してください。");
                }
            } else if (trim.startsWith("done ")) {
                try {
                    int index = Integer.parseInt(trim.substring(5).trim());
                    if (index < 1 || index > tasks.size()) {
                        System.out.println("番号が不正です。");
                    } else {
                        Task t = tasks.get(index - 1);
                        t.done = true;
                        System.out.println("完了： " + t.name);
                        saveTasks(tasks);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("doneコマンドは数字を指定してください。");
                }
            } else {
                System.out.println("不明なコマンド： " + trim);
            }
        }
        sc.close();
        System.out.println("終了");
    }

    // タスク保存
    private static void saveTasks(List<Task> tasks) {
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME, false)))) {
            for (Task t : tasks) {
                String status = t.done ? "[x]" : "[ ]";
                pw.println(status + " " + t.name);
            }
        } catch (IOException e) {
            System.out.println("保存エラー： " + e.getMessage());
        }
    }

    // タスク読み込み
    private static List<Task> loadTasks() {
        List<Task> tasks = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return tasks;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                boolean done = line.startsWith("[x]");
                String name = line.substring(3).trim();
                tasks.add(new Task(name, done));
            }
        } catch (IOException e) {
            System.out.println("読み込みエラー： " + e.getMessage());
        }
        return tasks;
    }
}

class Task {
    String name;
    boolean done;

    Task(String name) {
        this.name = name;
        this.done = false;
    }


    Task(String name, boolean done) {
        this.name = name;
        this.done = done;
    }
}
