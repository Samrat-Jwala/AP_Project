package org.example.demo1.controller;


import java.io.File;

public class Session {
    private static String username;
    private static String role;
    private static String userFolderPath;

    // Base directory where user folders will be stored
    private static final String BASE_DIR = "D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\Users";

    public static void setUsername(String u) {
        username = u;
        setRoleFromUsername(u);
        setUserFolderPath(u);
    }

    private static void setRoleFromUsername(String username) {
        if (username == null) {
            role = null;
        } else if (username.startsWith("G")) {
            role = "guide";
        } else if (username.startsWith("T")) {
            role = "tourist";
        } else {
            role = "admin";
        }
    }

    private static void setUserFolderPath(String username) {
        if (username != null) {
            userFolderPath = BASE_DIR + File.separator + username;
            File userDir = new File(userFolderPath);
            if (!userDir.exists()) {
                userDir.mkdirs(); // Create the folder if it doesn't exist
            }
        }
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static String getUserFolderPath() {
        return userFolderPath;
    }

    public static boolean isTourist() {
        return "tourist".equals(role);
    }

    public static boolean isGuide() {
        return "guide".equals(role);
    }

    public static boolean isAdmin() {
        return "admin".equals(role);
    }

    public static void clearSession() {
        username = null;
        role = null;
        userFolderPath = null;
    }
}
