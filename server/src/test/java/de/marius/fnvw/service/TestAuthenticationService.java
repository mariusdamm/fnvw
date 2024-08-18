package de.marius.fnvw.service;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class TestAuthenticationService {

    @BeforeEach
    @Transactional
    void set_up() {
        // still empty
    }
}
