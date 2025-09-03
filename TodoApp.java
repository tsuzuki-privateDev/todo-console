import java.util.*;

public class TodoApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<String> tasks = new ArrayList<>();

        System.out.println("ToDoアプリ v1: add <内容> / list / delete <番号> / exit");

        while (true) {
            System.out.print("> ");
            String line = sc.nextLine();
            if (line == null) break;
            String trim = line.trim();
            if (trim.equalsIgnoreCase("exit")) break;
            if (trim.isEmpty()) continue;

            // コマンド判定
            if (trim.startsWith("add ")) {  // "add "で始まるとき
                String task = trim.substring(4).trim();
                if (task.isEmpty()) {
                    System.out.println("内容を入力してください。");
                } else {
                    tasks.add(task);
                    System.out.println("追加： " + task);
                }
            } else if (trim.equals("list")) {
                if (tasks.isEmpty()) {
                    System.out.println("(タスク無し)");
                } else {
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + tasks.get(i));
                    }
                }
            } else if (trim.startsWith("delete ")) {
                try {
                    int index = Integer.parseInt(trim.substring(7).trim());
                    if (index < 1 || index > tasks.size()) {
                        System.out.println("番号が不正です。");
                    } else {
                        String removed = tasks.remove(index - 1);
                        System.out.println("削除: " + removed);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("削除コマンドは数字を指定してください。");
                }
            } else {
                System.out.println("不明なコマンド： " + trim);
            }
        }
        sc.close();
        System.out.println("終了");
    }
}
