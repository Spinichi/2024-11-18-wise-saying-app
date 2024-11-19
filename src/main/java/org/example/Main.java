package org.example;

//import java.util.Scanner;
//
//public class Main {
//    public static void main(String[] args) {
//        boolean isRunning = true;
//        Scanner scanner = new Scanner(System.in);
//
//        while (isRunning) {
//            System.out.println("== 명언 앱 ==");
//            System.out.print("명령) ");
//            String command = scanner.nextLine();
//
//            if (command.equals("종료")) {
//                isRunning = false;
//            }
//        }
//
//        scanner.close();
//    }
//}

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }
}

class App {

    public void run() {
        System.out.println("== 명언 앱 ==");

        System.out.print("명령) ");
    }
}