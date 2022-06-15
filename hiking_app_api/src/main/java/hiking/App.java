package hiking;

import hiking.security.GenerateKeys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        try {
            GenerateKeys.makeFiles("keys");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SpringApplication.run(App.class, args);

    }
}
