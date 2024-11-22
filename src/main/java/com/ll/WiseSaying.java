package com.ll;

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
