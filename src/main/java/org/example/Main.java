package org.example;



import java.util.List;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller(new Model(), new View(), new RepositoryJSON("toys.json"));
        controller.start();
    }
}