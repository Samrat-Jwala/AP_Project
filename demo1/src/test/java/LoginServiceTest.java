package org.example.demo1.service;

import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {

    private final String testUserDir = "D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\Users\\testuser,testpass";

    @BeforeEach
    public void setup() throws IOException {
        File dir = new File(testUserDir);
        dir.mkdirs();

        File file = new File(dir, "credentials.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("testuser,testpass");
        }
    }

    @Test
    public void testValidLogin() {
        LoginService service = new LoginService("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\Users");
        boolean result = service.authenticate("testuser", "testpass");
        assertTrue(result);
    }

    @Test
    public void testInvalidLogin() {
        LoginService service = new LoginService("D:\\College\\General\\2Y2S\\Adv Programming\\project - Copy\\demo1\\src\\main\\Users");
        boolean result = service.authenticate("wronguser", "wrongpass");
        assertFalse(result);
    }

    @AfterEach
    public void cleanUp() {
        File dir = new File(testUserDir);
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                file.delete();
            }
            dir.delete();
        }
    }
}
