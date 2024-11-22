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
    private final Path lastIdTextPath;
    private final Path buildPath;
    private final String dbPath;
    private final String fileType;
    private final Scanner scanner;
    private final Gson jsonToWiseSaying;
    private final Gson createJsonFile;
    private int lastId;

    App() {
        dir = new File("db/wiseSaying");
        lastIdTextPath = Paths.get("db/wiseSaying/lastId.txt");
        buildPath = Paths.get("db/wiseSaying/data.json");
        dbPath = "db/wiseSaying/";
        fileType = ".json";
        scanner = new Scanner(System.in);
        jsonToWiseSaying = new Gson();
        createJsonFile = new GsonBuilder().setPrettyPrinting().create();
        lastId = updateLastId();
    }

    public void run() {
        System.out.println("== 명언 앱 ==");
        checkDbExist(); // db/wiseSaying 존재 여부 확인 및 생성

        while (true) {

            String cmd = userInput();

            if (cmd.equals("종료")) break;

            else if (cmd.equals("등록")) {

                addWiseSaying();

            } else if (cmd.equals("목록")) {

                showList();

            } else if (cmd.startsWith("삭제?id=")) {

                deleteWiseSaying(cmd);

            } else if (cmd.startsWith("수정?id=")) {

                updateWiseSaying(cmd);

            } else if (cmd.equals("빌드")) {

                buildData();

            }
        }
        scanner.close();
    }

    private boolean checkDbExist() {
        return dir.mkdirs();
    }

    private int updateLastId() {
        if (Files.exists(lastIdTextPath)) {
            try {
                List<String> lines = Files.readAllLines(lastIdTextPath);
                return Integer.parseInt(String.join("", lines));
            } catch (NumberFormatException e) {
                System.out.println("lastId는 숫자로 되어있어야 합니다.");
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

    private void addWiseSaying() {

        WiseSaying wiseSaying = makeWiseSaying();
        Path path = pathById(lastId);

        try {

            writeWiseSayingToPath(path, wiseSaying); // 명언번호.json 파일 생성 및 저장
            Files.write(lastIdTextPath, Integer.toString(lastId).getBytes()); // lastId.text 생성 및 갱신

            System.out.println("%d번 명언이 등록되었습니다.".formatted(lastId));

        } catch (IOException e) {
            System.out.println("파일 작성 중 오류가 발생했습니다.");
        }
    }

    private WiseSaying makeWiseSaying() {
        System.out.print("명언 : ");
        String content = scanner.nextLine();

        System.out.print("작가 : ");
        String author = scanner.nextLine();

        int id = ++lastId;

        return new WiseSaying(id, content, author);
    }

    private void showList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        try {

            for (int i = lastId; i >= 1; i--) {

                Path path = pathById(i);
                if (!Files.exists(path)) continue;

                WiseSaying wiseSaying = readWiseSaying(path);
                wiseSaying.printWiseSaying();

            }
        } catch (IOException e) {
            System.out.println("파일 읽어오는 중 오류가 발생했습니다.");
        }
    }

    private void updateWiseSaying(String cmd) {

        try {
            int updateId = Integer.parseInt(cmd.substring(6));
            Path path = Paths.get(dbPath + updateId + fileType);
            if (Files.exists(path)) {

                WiseSaying wiseSaying = readWiseSaying(path);

                wiseSaying.updateContent(scanner);
                wiseSaying.updateAuthor(scanner);

                writeWiseSayingToPath(path, wiseSaying);

            } else System.out.println("%d번 명언은 존재하지 않습니다.".formatted(updateId));
        } catch (NumberFormatException e) {
            System.out.println("id는 양의 정수여야 합니다.");
        } catch (IOException e) {
            System.out.println("파일 수정 중 오류가 발생했습니다.");
        }
    }


    private void deleteWiseSaying(String cmd) {
        try {
            int eraseId = Integer.parseInt(cmd.substring(6));
            Path path = pathById(eraseId);

            if (Files.exists(path)) {

                Files.delete(path);
                System.out.println("%d번 명언이 삭제되었습니다.".formatted(eraseId));

            } else System.out.println("%d번 명언은 존재하지 않습니다.".formatted(eraseId));

        } catch (NumberFormatException e) {
            System.out.println("id는 양의 정수여야 합니다.");
        } catch (IOException e) {
            System.out.println("파일을 삭제하는 중 오류가 발생했습니다.");
        }
    }

    private void buildData() {

        Path dbDir = Paths.get(dbPath);

        try (Stream<Path> stream = Files.list(dbDir)) {

            List<Path> jsonFilePaths = stream.filter(path -> path.toString().endsWith(fileType)
                    && !path.toString().endsWith("data.json")).toList();

            List<WiseSaying> wiseSayingList = new ArrayList<>();

            for (Path path : jsonFilePaths) {
                wiseSayingList.add(readWiseSaying(path));
            }

            writeWiseSayingToPath(buildPath, wiseSayingList);

            System.out.println("data.json 파일의 내용이 갱신되었습니다.");

        } catch (IOException e) {
            System.out.println("빌드 중 오류가 발생했습니다.");
        }
    }

    private Path pathById(int id) {
        return Paths.get(dbPath + id + fileType);
    }

    private WiseSaying readWiseSaying(Path path) throws IOException {

        List<String> lines = Files.readAllLines(path);
        String content = String.join("", lines);

        return jsonToWiseSaying.fromJson(content, WiseSaying.class);
    }

    private void writeWiseSayingToPath(Path path, WiseSaying wiseSaying) throws IOException {

        String jsonStrings = createJsonFile.toJson(wiseSaying);
        Files.write(path, jsonStrings.getBytes());

    }

    private void writeWiseSayingToPath(Path path, List<WiseSaying> wiseSayingList) throws IOException {

        String jsonStrings = createJsonFile.toJson(wiseSayingList);
        Files.write(path, jsonStrings.getBytes());

    }
}
