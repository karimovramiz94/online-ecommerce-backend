package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Эта аннотация превращает обычный класс в мощный сервер
public class Main {
    public static void main(String[] args) {
        // Эта строка запускает весь механизм Spring Boot
        SpringApplication.run(Main.class, args);
    }
}