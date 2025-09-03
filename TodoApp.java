import java.util.*;

public class TodoApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();

        System.out.println("ToDoアプリ v2: add <内容> / list / delete <番号> / done <番号> / exit");

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
}

class Task {
    String name;
    boolean done;

    Task(String name) {
        this.name = name;
        this.done = false;
    }
}
