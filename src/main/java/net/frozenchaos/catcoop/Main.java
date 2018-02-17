/**
 * Main class that initializes the CatCoop server.
 * @author imahilus
 */
package net.frozenchaos.catcoop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Main {
    public static void main(String[] varargs) {
        SpringApplication.run(Main.class, varargs);
    }
}
