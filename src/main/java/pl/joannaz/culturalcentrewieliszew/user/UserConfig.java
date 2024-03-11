package pl.joannaz.culturalcentrewieliszew.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.UUID;

@Configuration
public class UserConfig {
    @Bean
    CommandLineRunner userRunner (UserRepository userRepository) {
        return args -> userRepository.saveAll(
            List.of(
                    new User( // admin
                            UUID.fromString("8781cef8-5532-43ec-b1b7-73df5c6f80ad"),
                            "Paweł",
                            "Zzz",
                            "123-456-789",
                            "p@gmail.com",
                            "$2a$10$vUQaRwWe6Km1cZk9T0.3OeVsgY/1X0NY.ngLlYOzkT91hubu3fDb.", // "test"
                            "1981-01-01",
                            Role.ADMIN
                    ),
                    new User( // parent1
                            UUID.fromString("0a11ea3f-69c8-42e9-b162-3d51f3e3a826"),
                            "Joanna",
                            "Zzz",
                            "123-456-789",
                            "j@gmail.com",
                            "$2a$10$vUQaRwWe6Km1cZk9T0.3OeVsgY/1X0NY.ngLlYOzkT91hubu3fDb.",
                            "1988-03-01",
                            Role.CLIENT
                    ),
                    new User( // child1.1
                            UUID.fromString("1474a8e1-209f-4c10-81f7-4e7cd4a52fc2"),
                            UUID.fromString("0a11ea3f-69c8-42e9-b162-3d51f3e3a826"),
                            "Iga",
                            "Zzz",
                            "j@gmail.com/IgaZzz",
                            "2010-04-01"
                    ),
                    new User( // child1.2
                            UUID.fromString("0b767555-3d43-41ce-a638-14ec3e9b69f3"),
                            UUID.fromString("0a11ea3f-69c8-42e9-b162-3d51f3e3a826"),
                            "Franek",
                            "Zzz",
                            "j@gmail.com/FranekZzz",
                            "2012-05-01"
                    ),
                    new User( // child1.3
                            UUID.fromString("544361c1-13d8-4e29-8631-d0a985ead644"),
                            UUID.fromString("0a11ea3f-69c8-42e9-b162-3d51f3e3a826"),
                            "Piotr",
                            "Zzz",
                            "j@gmail.com/PiotrZzz",
                            "2014-07-01"
                    ),
                    new User( // parent2
                            UUID.fromString("fd50915e-2a50-4302-9407-c886c0057936"),
                            "Szymon",
                            "Aaa",
                            "123-456-789",
                            "szymon@gmail.com",
                            "$2a$10$vUQaRwWe6Km1cZk9T0.3OeVsgY/1X0NY.ngLlYOzkT91hubu3fDb.",
                            "1975-12-20",
                            Role.CLIENT
                    ),
                    new User( // child2.1
                            UUID.fromString("c5c02518-ec95-49bf-b52b-ff6a670d58e4"),
                            UUID.fromString("fd50915e-2a50-4302-9407-c886c0057936"),
                            "Lukasz",
                            "Aaa",
                            "szymon@gmail.com/LukaszAaa",
                            "2000-03-15"
                    ),
                    new User( // child2.2
                            UUID.fromString("3e50d6d0-d41e-490d-ab40-1cb12e109865"),
                            UUID.fromString("fd50915e-2a50-4302-9407-c886c0057936"),
                            "Zofia",
                            "Aaa",
                            "szymon@gmail.com/ZofiaAaa",
                            "2004-11-25"
                    )
            )
        );
    }
}
