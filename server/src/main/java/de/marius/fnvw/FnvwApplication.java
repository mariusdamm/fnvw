package de.marius.fnvw;

import de.marius.fnvw.dao.RoleRepository;
import de.marius.fnvw.entity.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FnvwApplication {

    public static void main(String[] args) {
        SpringApplication.run(FnvwApplication.class, args);
    }

    @Bean
    CommandLineRunner run(RoleRepository roleRepository) {
        return args -> {
            if (!roleRepository.findAll().isEmpty()) {
                return;
            }

            roleRepository.save(new Role(0, "ADMIN"));
            roleRepository.save(new Role(0, "USER"));
        };

    }
}
