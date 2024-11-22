package com.ll;

import java.util.Scanner;

public class WiseSaying {
    private final int id;
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

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getAuthor() {
        return author;
    }

    public void updateContent(Scanner scanner) {
        System.out.println("명언(기존) : %s".formatted(this.getContent()));
        System.out.print("명언 : ");
        this.content = scanner.nextLine();
    }

    public void updateAuthor(Scanner scanner) {
        System.out.println("작가(기존) : %s".formatted(this.getAuthor()));
        System.out.print("작가 : ");
        this.author = scanner.nextLine();
    }
}
