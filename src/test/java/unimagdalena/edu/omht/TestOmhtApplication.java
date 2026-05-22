package unimagdalena.edu.omht; 

import org.springframework.boot.SpringApplication;

public class TestOmhtApplication {

    public static void main(String[] args) {
        SpringApplication.from(OmhtApplication::main)
                         .with(TestcontainersConfiguration.class)
                         .run(args);
    }
}