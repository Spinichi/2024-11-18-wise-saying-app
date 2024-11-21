package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}

class App {
    public void run() {
        System.out.println("== 명언 앱 ==");

        Scanner scanner = new Scanner(System.in);
        int lastId = 0;
        ArrayList<WiseSaying> wiseSayingList = new ArrayList<>();

        while (true) {
            System.out.print("명령) ");
            String cmd = scanner.nextLine();

            if (cmd.equals("종료")) break;

            else if (cmd.equals("등록")) {
                System.out.print("명언 : ");
                String content = scanner.nextLine();

                System.out.print("작가 : ");
                String author = scanner.nextLine();
                int id = ++lastId;

                WiseSaying wiseSaying = new WiseSaying(id, content, author);
                wiseSayingList.add(wiseSaying);

                System.out.println("%d번 명언이 등록되었습니다.".formatted(id));

            } else if (cmd.equals("목록")) {
                System.out.println("번호 / 작가 / 명언");
                System.out.println("----------------------");
                for (int i = wiseSayingList.size() - 1; i >= 0; i--) {
                    wiseSayingList.get(i).printWiseSaying();
                }

            } else if (cmd.startsWith("삭제?id=")) {
                int eraseId = Integer.parseInt(cmd.substring(6));
                boolean isRemoved = wiseSayingList.removeIf(value -> value.selectWiseSaying(eraseId));
                if (isRemoved) {
                    System.out.println("%d번 명언이 삭제되었습니다.".formatted(eraseId));
                } else {
                    System.out.println("%d번 명언은 존재하지 않습니다.".formatted(eraseId));
                }

            } else if (cmd.startsWith("수정?id=")) {
                int fixId = Integer.parseInt(cmd.substring(6));
                for (WiseSaying wiseSaying : wiseSayingList) {
                    if (wiseSaying.selectWiseSaying(fixId)) {
                        System.out.println("명언(기존) : %s".formatted(wiseSaying.getContent()));
                        System.out.print("명언 : ");
                        wiseSaying.fixContent(scanner.nextLine());

                        System.out.println("작가(기존) : %s".formatted(wiseSaying.getAuthor()));
                        System.out.print("작가 : ");
                        wiseSaying.fixAuthor(scanner.nextLine());
                    } else {
                        System.out.println("%d번 명언은 존재하지 않습니다.".formatted(fixId));
                    }
                }
            }

        }

        scanner.close();
    }

}

class WiseSaying {
    private int id;
    private String content;
    private String author;

    WiseSaying(int id, String content, String author) {
        this.id = id;
        this.content = content;
        this.author = author;
    }

    public void printWiseSaying() {
        System.out.println("%d / %s / %s".formatted(id, author, content));
    }

    public boolean selectWiseSaying(int id) {
        return this.id == id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public void fixContent(String content) {
        this.content = content;
    }

    public void fixAuthor(String Author) {
        this.author = Author;
    }
}

