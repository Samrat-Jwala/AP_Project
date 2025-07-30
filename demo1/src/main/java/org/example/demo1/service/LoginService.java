package org.example.demo1.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoginService {

    private final String basePath;

    public LoginService(String basePath) {
        this.basePath = basePath;
    }

    public boolean authenticate(String username, String password) {
        File credentialsFile = new File(basePath + "\\" + username + "," + password + "\\credentials.txt");

        if (!credentialsFile.exists()) {
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(credentialsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
