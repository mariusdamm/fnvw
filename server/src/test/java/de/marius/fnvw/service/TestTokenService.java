package de.marius.fnvw.service;

import de.marius.fnvw.dao.AppUserRepository;
import de.marius.fnvw.dao.RoleRepository;
import de.marius.fnvw.entity.AppUser;
import de.marius.fnvw.entity.Role;
import de.marius.fnvw.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestTokenService {

    String testUserName = "Test User1";
    String testUserUsername = "Test Username1";
    String testUserPwd = "test_pwd1";
    List<Role> roles = new ArrayList<>();
    AppUser testUser;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @BeforeEach
    @Transactional
    void set_up() {
        Role role = roleRepository.findByAuthority("USER").orElseGet(() -> roleRepository.save(new Role("USER")));
        roles.add(role);

        testUser = appUserRepository.save(new AppUser(testUserName, testUserUsername, passwordEncoder.encode(testUserPwd), roles));
    }

    @Test
    @Transactional
    void test_generate_and_verify_token() {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(testUserUsername, testUserPwd));
        String token = tokenService.generateJwt(auth);
        try {
            AppUser resultUser = tokenService.getUserFromToken(token);
            String resultRoles = tokenService.getRolesFromToken(token);

            assertNotNull(resultUser);
            assertEquals(testUserUsername, resultUser.getUsername());
            assertEquals(testUserName, resultUser.getName());
            assertEquals(roles.stream().map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" ")), resultRoles);
        } catch (DataNotFoundException e) {
            fail("DataNotFoundException thrown, where no exception should be thrown. " + e.getMessage());
        }
    }
}
