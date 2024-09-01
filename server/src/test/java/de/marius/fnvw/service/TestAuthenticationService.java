package de.marius.fnvw.service;

import de.marius.fnvw.dao.AppUserRepository;
import de.marius.fnvw.dao.RoleRepository;
import de.marius.fnvw.dto.LoginDto;
import de.marius.fnvw.dto.LoginResponseDto;
import de.marius.fnvw.dto.RegisterDto;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.Role;
import de.marius.fnvw.exception.ConstraintException;
import de.marius.fnvw.exception.DataNotFoundException;
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
        try {
            assertEquals(testUserName, tokenService.getUserFromToken(result.getToken()).getName());
        } catch (DataNotFoundException e) {
            fail("DataNotFoundException thrown, where no exception should be thrown. " + e.getMessage());
        }
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

    @Test
    @Transactional
    void test_register_user_happy_path() throws ConstraintException {
        String testRegisterUsername = "RegisteredUsername";
        String testRegisterPwd = "RegisteredUserPwd";
        String testRegisterName = "RegisterName";
        RegisterDto dto = new RegisterDto(testRegisterUsername, testRegisterPwd, testRegisterName);

        authenticationService.registerUser(dto);
        AppUser registeredUser = appUserRepository.findByUsername(testRegisterUsername).orElse(null);

        assertNotNull(registeredUser);
        assertEquals(testRegisterName, registeredUser.getName());
        assertEquals(testRegisterUsername, registeredUser.getUsername());
        appUserRepository.delete(registeredUser);
    }

    @Test
    @Transactional
    void test_register_user_with_already_existing_username() {
        String testRegisterPwd = "RegisteredUserPwd";
        String testRegisterName = "RegisterName";
        RegisterDto dto = new RegisterDto(testUserUsername, testRegisterPwd, testRegisterName);

        assertThrows(ConstraintException.class, () -> authenticationService.registerUser(dto));
    }

    @Test
    @Transactional
    void test_register_user_with_empty_username() {
        String testRegisterPwd = "RegisteredUserPwd";
        String testRegisterName = "RegisterName";
        RegisterDto dto = new RegisterDto("", testRegisterPwd, testRegisterName);

        assertThrows(IllegalArgumentException.class, () -> authenticationService.registerUser(dto));
    }
}
