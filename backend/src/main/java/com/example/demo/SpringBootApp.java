package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringBootApp implements CommandLineRunner {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // antes de ejecutar el programa, genera contraseñas y las encripta, luego las pasamos al .sql para que las cree
        // y las añada a al BD
        String password = "12345";
        for (int i = 0; i < 4; i++) {
            String passwordBcrypt = passwordEncoder.encode(password);
            System.out.println(passwordBcrypt);
        }

        String password2 = "holaquetal";
        for (int i = 0; i < 1; i++) {
            String passwordBcrypt = passwordEncoder.encode(password2);
            System.out.println("marc pw " + passwordBcrypt);
        }


    }
}
