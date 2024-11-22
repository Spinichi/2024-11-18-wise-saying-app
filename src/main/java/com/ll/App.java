package com.ll;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class App {
    private final File dir;
    private final Path lastIdPath;
    private final Path buildPath;
    private final Scanner scanner;
    private int lastId;

    App() {
        dir = new File("db/wiseSaying");
        lastIdPath = Paths.get("db/wiseSaying/lastId.txt");
        buildPath = Paths.get("db/wiseSaying/data.json");
        scanner = new Scanner(System.in);
        lastId = updateLastId();
    }

    public void run() {
        checkDbExist(); // db/wiseSaying 존재 여부 확인
        System.out.println("== 명언 앱 ==");

        while (true) {
            String cmd = userInput();

            if (cmd.equals("종료")) break;

            else if (cmd.equals("등록")) {
                System.out.print("명언 : ");
                String content = scanner.nextLine();

                System.out.print("작가 : ");
                String author = scanner.nextLine();
                int id = ++lastId;

                WiseSaying wiseSaying = new WiseSaying(id, content, author);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String json = gson.toJson(wiseSaying);
                Path path = Paths.get("db/wiseSaying/" + id + ".json");
                try {
                    Files.write(path, json.getBytes());
                    Files.write(lastIdPath, Integer.toString(id).getBytes());
                    System.out.println("%d번 명언이 등록되었습니다.".formatted(id));
                } catch (IOException e) {
                    System.out.println("파일 작성 중 오류가 발생했습니다.");
                }


            } else if (cmd.equals("목록")) {
                System.out.println("번호 / 작가 / 명언");
                System.out.println("----------------------");

                try {
                    Gson gson = new Gson();
                    for (int i = lastId; i >= 1; i--) {
                        Path path = Paths.get("db/wiseSaying/" + i + ".json");
                        if (!Files.exists(path)) continue;
                        List<String> lines = Files.readAllLines(path);
                        String content = String.join("", lines);

                        WiseSaying wiseSaying = gson.fromJson(content, WiseSaying.class);
                        wiseSaying.printWiseSaying();
                    }
                } catch (IOException e) {
                    System.out.println("파일 읽어오는 중 오류가 발생했습니다.");
                }

            } else if (cmd.startsWith("삭제?id=")) {
                int eraseId = Integer.parseInt(cmd.substring(6));
                Path path = Paths.get("db/wiseSaying/" + eraseId + ".json");
                if (Files.exists(path)) {
                    try {
                        Files.delete(path);
                        System.out.println("%d번 명언이 삭제되었습니다.".formatted(eraseId));
                    } catch (IOException e) {
                        System.out.println("파일을 삭제하는 중 오류가 발생했습니다.");
                    }
                } else System.out.println("%d번 명언은 존재하지 않습니다.".formatted(eraseId));

            } else if (cmd.startsWith("수정?id=")) {
                int fixId = Integer.parseInt(cmd.substring(6));
                Path path = Paths.get("db/wiseSaying/" + fixId + ".json");
                if (Files.exists(path)) {
                    try {
                        List<String> lines = Files.readAllLines(path);
                        String content = String.join("", lines);
                        Gson gsonRead = new Gson();
                        WiseSaying wiseSaying = gsonRead.fromJson(content, WiseSaying.class);

                        System.out.println("명언(기존) : %s".formatted(wiseSaying.getContent()));
                        System.out.print("명언 : ");
                        wiseSaying.fixContent(scanner.nextLine());

                        System.out.println("작가(기존) : %s".formatted(wiseSaying.getAuthor()));
                        System.out.print("작가 : ");
                        wiseSaying.fixAuthor(scanner.nextLine());

                        Gson gsonFix = new GsonBuilder().setPrettyPrinting().create();
                        String json = gsonFix.toJson(wiseSaying);

                        Files.write(path, json.getBytes());
                    } catch (IOException e) {
                        System.out.println("파일 수정 중 오류가 발생했습니다.");
                    }
                } else System.out.println("%d번 명언은 존재하지 않습니다.".formatted(fixId));
            } else if (cmd.equals("빌드")) {

                Path jsonDir = Paths.get("db/wiseSaying");
                Gson gson = new Gson();
                Gson build = new GsonBuilder().setPrettyPrinting().create();

                try (Stream<Path> stream = Files.list(jsonDir)) {
                    List<Path> jsonFiles;
                    jsonFiles = stream.filter(path -> path.toString().endsWith(".json") &&
                                    !path.toString().endsWith("data.json"))
                            .toList();


                    List<WiseSaying> wiseSayingList = new ArrayList<>();

                    for (Path jsonFile : jsonFiles) {
                        List<String> lines = Files.readAllLines(jsonFile);
                        String content = String.join("", lines);
                        wiseSayingList.add(gson.fromJson(content, WiseSaying.class));
                    }

                    String buildList = build.toJson(wiseSayingList);
                    Files.write(buildPath, buildList.getBytes());

                    System.out.println("data.json 파일의 내용이 갱신되었습니다.");

                } catch (IOException e) {
                    System.out.println("빌드 중 오류가 발생했습니다.");
                }
            }
        }
        scanner.close();
    }

    public boolean checkDbExist() {
        return dir.mkdirs();
    }

    public int updateLastId() {
        if (Files.exists(lastIdPath)) {
            try {
                List<String> lines = Files.readAllLines(lastIdPath);
                lastId = Integer.parseInt(String.join("", lines));
                return lastId;
            } catch (IOException e) {
                System.out.println("파일 읽어오는 중 오류가 발생했습니다.");
            }
        }
        return 0;
    }

    public String userInput() {
        System.out.print("명령) ");
        return scanner.nextLine();
    }

//    private WiseSaying makeWiseSaying() {
//        System.out.print("명언 : ");
//        String content = scanner.nextLine();
//
//        System.out.print("작가 : ");
//        String author = scanner.nextLine();
//        int id = ++lastId;
//
//        return new WiseSaying(id,content,author);
//    }

}
