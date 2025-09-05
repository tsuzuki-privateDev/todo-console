import java.util.*;

public class TodoApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        TaskRepository repo = new TaskRepository();
        TaskService svc = new TaskService(repo);

        System.out.println("ToDo (分割版):");
        System.out.println(" add <内容>");
        System.out.println(" list | list todo | list done | list sort due | list sort prio");
        System.out.println(" find <キーワード>");
        System.out.println(" edit <番号> <タイトル>");
        System.out.println(" delete <番号> | done <番号> | undone <番号>");
        System.out.println(" setdue <番号> <YYYY-MM-DD> | cleardue <番号>");
        System.out.println(" setprio <番号> <LOW|MEDIUM|HIGH>");
        System.out.println(" exit");

        while (true) {
            System.out.print("> ");
            String line = sc.nextLine();
            if (line == null) break;
            String trim = line.trim();
            if (trim.equalsIgnoreCase("exit")) break;
            if (trim.isEmpty()) continue;

            try {
                if (trim.startsWith("add ")) {
                    String name = trim.substring(4).trim();
                    svc.add(name);
                    System.out.println("追加: " + name);

                } else if (trim.equals("list") || trim.startsWith("list ")) {
                    handleList(trim, svc);

                } else if (trim.startsWith("find ")) {
                    String[] sp = trim.split("\\s+", 2);   // 1個以上の空白で区切る
                    if (sp.length != 2 || sp[1].isBlank()) throw new IllegalArgumentException("使い方: find <キーワード>");
                    String kw = sp[1].toLowerCase();
                    List<Task> all = svc.listAll();
                    List<Task> hit = new ArrayList<>();
                    for (Task t : all) {
                        if (t.name.toLowerCase().contains(kw)) hit.add(t);
                    }
                    if (hit.isEmpty()) {
                        System.out.println("(該当なし)");
                    } else {
                        for (int i = 0; i < hit.size(); i++) {
                            System.out.println((i + 1) + ". " + TaskService.fmt(hit.get(i)));
                        }
                    }

                } else if (trim.startsWith("edit ")) {
                    String[] sp = trim.split("\\s+", 3);    // タイトルは空白が入ることがあり得るので、limitは3にする
                    if (sp.length != 3 || sp[2].isBlank()) throw new IllegalArgumentException("使い方: edit <番号> <タイトル>");
                    int n = Integer.parseInt(sp[1]);
                    System.out.println("編集： " + n + " -> " + svc.edit(n, sp[2]).name);

                } else if (trim.startsWith("delete ")) {
                    int n = Integer.parseInt(trim.substring(7).trim());
                    System.out.println("削除: " + svc.delete(n).name);

                } else if (trim.startsWith("done ")) {
                    int n = Integer.parseInt(trim.substring(5).trim());
                    svc.setDone(n, true);
                    System.out.println("完了: " + n);

                } else if (trim.startsWith("undone ")) {
                    int n = Integer.parseInt(trim.substring(7).trim());
                    svc.setDone(n, false);
                    System.out.println("未完了に戻した: " + n);

                } else if (trim.startsWith("setdue ")) {
                    String[] sp = trim.split("\\s+");
                    if (sp.length != 3) throw new IllegalArgumentException("使い方: setdue <番号> <YYYY-MM-DD>");
                    svc.setDue(Integer.parseInt(sp[1]), sp[2]);
                    System.out.println("締切設定: " + sp[1] + " -> " + sp[2]);

                } else if (trim.startsWith("cleardue ")) {
                    String[] sp = trim.split("\\s+");
                    if (sp.length != 2) throw new IllegalArgumentException("使い方: cleardue <番号>");
                    svc.clearDue(Integer.parseInt(sp[1]));
                    System.out.println("締切解除: " + sp[1]);

                } else if (trim.startsWith("setprio ")) {
                    String[] sp = trim.split("\\s+");
                    if (sp.length != 3) throw new IllegalArgumentException("使い方: setprio <番号> <LOW|MEDIUM|HIGH>");
                    svc.setPrio(Integer.parseInt(sp[1]), sp[2]);
                    System.out.println("優先度設定: " + sp[1] + " -> " + sp[2]);

                } else {
                    System.out.println("不明なコマンド: " + trim);
                }

            } catch (NumberFormatException e) {
                System.out.println("番号は整数で指定してください。");
            } catch (IllegalArgumentException e) {
                System.out.println("入力エラー: " + e.getMessage());
            }
        }

        sc.close();
        System.out.println("終了。");
    }

    private static void handleList(String cmd, TaskService svc) {
        List<Task> view;
        if (cmd.equals("list")) {
            view = svc.listAll();
        } else {
            String opt = cmd.substring(4).trim();
            switch (opt) {
                case "todo": view = svc.listTodo(); break;
                case "done": view = svc.listDone(); break;
                case "sort due": view = svc.listSortDue(); break;
                case "sort prio": view = svc.listSortPrio(); break;
                default:
                    System.out.println("list の使い方: list / list todo / list done / list sort due / list sort prio");
                    return;
            }
        }
        if (view.isEmpty()) { System.out.println("(タスクなし)"); return; }
        for (int i = 0; i < view.size(); i++) {
            System.out.println((i + 1) + ". " + TaskService.fmt(view.get(i)));
        }
    }
}
