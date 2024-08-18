package de.marius.fnvw.service;

import de.marius.fnvw.dao.AppUserRepository;
import de.marius.fnvw.dao.RoleRepository;
import de.marius.fnvw.dto.LoginDto;
import de.marius.fnvw.dto.LoginResponseDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestAuthenticationService {

    private final String testUserUsername = "Test Username1";
    private final String testUserPwd = "test_pwd1";
    private final String testUserName = "Test User1";
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private TokenService tokenService;

    @BeforeEach
    @Transactional
    void set_up() {
        Role role = roleRepository.findByAuthority("USER").orElseGet(() -> roleRepository.save(new Role("USER")));
        List<Role> roles = new ArrayList<>();
        roles.add(role);

        appUserRepository.save(new AppUser(testUserName, testUserUsername, passwordEncoder.encode(testUserPwd), roles));
    }

    @Test
    @Transactional
    void test_login_valid_username_valid_password() {
        LoginDto dto = new LoginDto(testUserUsername, testUserPwd);
        LoginResponseDto result = authenticationService.loginUser(dto);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals(testUserName, tokenService.getUserFromToken(result.getToken()).getName());
    }

    @Test
    @Transactional
    void test_login_valid_username_invalid_password() {
        LoginDto dto = new LoginDto(testUserUsername, testUserPwd + "ffff");
        assertThrows(AuthenticationException.class, () -> authenticationService.loginUser(dto));
    }

    @Test
    @Transactional
    void test_login_invalid_username_invalid_password() {
        LoginDto dto = new LoginDto(testUserUsername + "ffff", testUserPwd + "ffff");
        assertThrows(AuthenticationException.class, () -> authenticationService.loginUser(dto));
    }
}
