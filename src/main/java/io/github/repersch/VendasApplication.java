package io.github.repersch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class VendasApplication {

    @Value("${application.nome}") // informação que vem do application.properties
    private String apllicationName;

    public static void main(String[] args) {
        SpringApplication.run(VendasApplication.class, args);
    }

}
